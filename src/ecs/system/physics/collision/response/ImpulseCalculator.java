package ecs.system.physics.collision.response;

import org.joml.Vector3f;

import ecs.component.Movable;
import ecs.component.State;
import ecs.component.Collidable;
import ecs.component.Mass;
import ecs.entity.Entity;
import ecs.entity.MovableCollidableBall;
import ecs.system.physics.collision.Collision;
import ecs.system.physics.collision.contactGeneration.ContactPoint;
import utils.Vector3fUtils;

/**
 * Class that calculates the impulse generated in a collision and applies
 * it to the appropriate entities involved.
 * 
 * @author Dominic Cogan-Tucker;
 *
 */
public class ImpulseCalculator
{
	/**
	 * The Gravitational Constat G.
	 */
	private static final float G = 6.67E-11f;
	
	/**
	 * The radius of the earth.
	 */
	private static final float R = 6.38E6f;
	
	/**
	 * The mass of the earth.
	 */
	private static final float M = 5.97E24f;
	
	/**
	 * The value of small g using G, R and M.
	 */
	public static final float GRAVITAIONAL_ACCELERATION = (G * M) / (R * R);
	
	/**
	 * Entity a in the collision.
	 */
	private Entity a;
	
	/**
	 * Entity b in a collision.
	 */
	private Entity b;
	
	/**
	 * The contact point of the collison.
	 */
	private ContactPoint contact;
	
	/**
	 * The time of a frame.
	 */
	private float dt;
	
	/**
	 * The direction of motion of the current entity.
	 */
	private Vector3f dirOfMotion = new Vector3f();
	
	/**
	 * Constructs an ImpulseCalculator for the given collision. Private to ensure the static calculate
	 * method is called to perform initiation.
	 * 
	 * @param collision The collision to calculate impulse for.
	 * @param dt The frame time.
	 */
	private ImpulseCalculator(Collision collision, float dt)
	{
		this.a = collision.getEntityA();
		this.b = collision.getEntityB();
		this.contact = collision.getContact();
		this.dt = dt;
	}
	
	/**
	 * Constructs a ImpulseCalculator then initiates the resultant force calculations.
	 * 
	 * @param collision The collision to calculate impusle for.S
	 * @param dt The frame time.
	 */
	public static void calculate(Collision collision, float dt)
	{
		ImpulseCalculator impc = new ImpulseCalculator(collision, dt);
		impc.generateForces();
	}
	
	/**
	 * Generates the resultant force and torque of the entities in the collision.
	 */
	private void generateForces()
	{
		// Checks which entities in the collision can be moved and calculates forces accordingly.
		if (a.hasComponent(Movable.class) && b.hasComponent(Movable.class))
		{
			generateResultantForce(0);
			generateTorque(a);
			generateResultantForce(1);
			generateTorque(b);

		}
		else if (a.hasComponent(Movable.class))
		{
			generateResultantForce(0);
			generateTorque(a);
			generateSlidingForce(0);

		}
		else if (b.hasComponent(Movable.class))
		{
			generateResultantForce(1);
			generateTorque(b);
			generateSlidingForce(1);
		}
	}
	
	/**
	 * Generates the resultant force for the given entity in the collision.
	 * 
	 * @param entity An integer representing either entity 0 or 1 (a or b).
	 */
	private void generateResultantForce(int entity)
	{
		Entity a;
		Entity b;
		if (entity == 0)
		{
			a = this.a;
			b = this.b;
		}
		else
		{
			a = this.b;
			b = this.a;
		}
		Movable mov = a.getComponent(Movable.class);
		Collidable colA = a.getComponent(Collidable.class);
		Collidable colB = b.getComponent(Collidable.class);
		if (Math.abs(mov.momentum.length()) < 1E-2f)
		{
			mov.momentum.set(0);
		}
		dirOfMotion.set(mov.momentum).normalize();
		if (mov.momentum.equals(new Vector3f()))
		{
			dirOfMotion.set(0, -1, 0);
		}
		if (dirOfMotion.dot(contact.worldNormal) > 0)
		{
			contact.worldNormal.negate();
		}
		else if (dirOfMotion.equals(new Vector3f(0, 1, 0)) || dirOfMotion.equals(new Vector3f(0, -1, 0)) && (Math.abs(dirOfMotion.dot(contact.worldNormal)) < 0.55f))
		{
			contact.worldNormal.set(dirOfMotion).negate();
		}
		Vector3f axis = new Vector3f(dirOfMotion).cross(contact.worldNormal);
		float angle = new Vector3f(dirOfMotion).angle(contact.worldNormal);
		if (angle >= Math.PI / 2)
		{
			angle = 0;
		}
		Vector3f resultant = new Vector3f(contact.worldNormal.x * mov.momentum.x, contact.worldNormal.y * mov.momentum.y, contact.worldNormal.z * mov.momentum.z).negate()
				.add(new Vector3f(contact.worldNormal).normalize().rotateAxis(-angle, axis.x, axis.y, axis.z)
						.mul(mov.momentum.length()).mul((colA.restitution + colB.restitution) / 2)).div(dt);
		if (resultant.equals(new Vector3f(Float.NaN)))
		{
			resultant.set(0);
		}
		if(resultant.dot(dirOfMotion) > 0)
		{
			resultant.negate();
		}
		mov.force.set(resultant);
	}
	
	/**
	 * Generates the torque in the S for the given entity.
	 * 
	 * @param a The entity to calculate the torque for.
	 */
	private void generateTorque(Entity a)
	{
		Movable mov = a.getComponent(Movable.class);
		State state = a.getComponent(State.class);
		Vector3f torqueDir = new Vector3f(contact.worldNormal).cross(dirOfMotion);
		Vector3f entityOrientation = new Vector3f(0, 1, 0)
				.rotateX((float) Math.toRadians(state.rotation.x ))
				.rotateY((float) Math.toRadians(state.rotation.y))
				.rotateZ((float) Math.toRadians(state.rotation.z));
		Vector3f torque = new Vector3f();
		if ((torqueDir.x != 0 || torqueDir.y != 0 || torqueDir.z != 0) && contact.worldNormal.dot(entityOrientation) < 0.9985f && contact.worldNormal.dot(entityOrientation) > -0.9985f)
		{
			torque.set(torqueDir.normalize().mul(9.81f * a.getComponent(Mass.class).mass));
		}
		mov.torque.set(torque);
		if (a instanceof MovableCollidableBall)
		{
			mov.torque.mul(5);
		}
	}
	
	/**
	 * Generates a sliding force for the given entity. Sliding occurs when a movable entity is
	 * on the surface of an immovable object.
	 * 
	 * @param entity An integer representing the entity to generate a sliding force for.
	 */
	private void generateSlidingForce(int entity)
	{
		Entity a;
		Entity b;
		if (entity == 0)
		{
			a = this.a;
			b = this.b;
		}
		else
		{
			a = this.b;
			b = this.a;
		}
		if (!b.hasComponent(Movable.class) && contact.worldNormal.angle(new Vector3f(0, 1, 0)) > 0.1f)
		{
			State state = a.getComponent(State.class);
			Mass massA = a.getComponent(Mass.class);
			
			Collidable colA = a.getComponent(Collidable.class);
			Collidable colB = b.getComponent(Collidable.class);
			float totalCoeffFriction = (colA.friction + colB.friction) / 2;
			
			Vector3f resultant = new Vector3f(contact.worldNormal)
										.normalize()
										.mul(massA.mass * 9.81f)
										.div((float) (totalCoeffFriction * Math.sin(Math.toRadians(state.rotation.length()))  + Math.cos(Math.toRadians(state.rotation.length()))));
			Vector3f rotationAxis = new Vector3f(state.rotation)
										.normalize();			
			Vector3f frictionDir = new Vector3f(resultant)
										.cross(rotationAxis)
										.normalize();
			Vector3f friction = new Vector3f(frictionDir)
										.mul(resultant.length() * totalCoeffFriction);
			Vector3f gravComponent = new Vector3f(frictionDir)
										.normalize()
										.negate()
										.mul((float) (massA.mass * 9.81f * Math.sin(Math.toRadians(state.rotation.length()))));
			if (!frictionDir.equals(new Vector3f(Float.NaN)))
			{
				if (gravComponent.length() > friction.length())
				{
					state.position
							.add(new Vector3f(friction)
							.add(gravComponent)
							.mul(dt)
							.mul(massA.inverseMass));
				}
				if (a instanceof MovableCollidableBall && colA.friction != 0 && colB.friction != 0)
				{
					Vector3f torque = new Vector3f(new Vector3f(rotationAxis)
											.mul(friction.length()));
					state.rotation
							.add(Vector3fUtils.toDegrees(new Vector3f(torque)
																.mul(massA.inverseInertia * dt * 0.1f)));
				}
			}
		}
	}
}
