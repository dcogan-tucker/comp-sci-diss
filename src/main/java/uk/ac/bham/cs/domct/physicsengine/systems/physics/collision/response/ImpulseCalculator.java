package uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.response;

import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.component.Collidable;
import uk.ac.bham.cs.domct.physicsengine.component.Mass;
import uk.ac.bham.cs.domct.physicsengine.component.Movable;
import uk.ac.bham.cs.domct.physicsengine.component.State;
import uk.ac.bham.cs.domct.physicsengine.entity.Entity;
import uk.ac.bham.cs.domct.physicsengine.entity.MovableCollidableBall;
import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.Collision;
import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.contactGeneration.ContactPoint;

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
	 * The combined coefficient of restitutuion of the collision.
	 */
	private float restitution;
	
	/**
	 * The combined coefficient of friction of the collision.
	 */
	private float friction;
	
	/**
	 * The total inverse mass of the collision.
	 */
	private float inverseMassTotal;
	
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
		Collidable colA = a.getComponent(Collidable.class);
		Collidable colB = b.getComponent(Collidable.class);
		restitution = Math.min(1, colA.restitution * colB.restitution);
		friction = Math.min(1, (colA.friction + colB.friction) / 2);
		Mass massA = a.getComponent(Mass.class);
		Mass massB = b.getComponent(Mass.class);
		inverseMassTotal = massA.inverseMass + massB.inverseMass;
	}
	
	/**
	 * Constructs a ImpulseCalculator then initiates the resultant force calculations.
	 * 
	 * @param collision The collision to calculate impulse for.S
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
			generateResultantForce(a);
			generateTorque(a);
			generateResultantForce(b);
			generateTorque(b);

		}
		else if (a.hasComponent(Movable.class))
		{
			generateResultantForce(a);
			generateTorque(a);
			applyFriction(a);

		}
		else if (b.hasComponent(Movable.class))
		{
			generateResultantForce(b);
			generateTorque(b);
			applyFriction(b);
		}
	}
	
	/**
	 * Generates the resultant force for the given entity in the collision.
	 * 
	 * @param e The entity to generate the forces for.
	 */
	private void generateResultantForce(Entity e)
	{
		Movable mov = e.getComponent(Movable.class);
		if (Math.abs(mov.momentum.length()) < 1E-2f)
		{
			mov.momentum.set(0);
		}
		dirOfMotion.set(mov.momentum).normalize();
		if (mov.momentum.equals(new Vector3f()))
		{
			dirOfMotion.set(0, -1, 0);
		}
		// Normal to collision should be away from direction of motion.
		if (dirOfMotion.dot(contact.worldNormal) > 0)
		{
			contact.worldNormal.negate();
		}
		else if (dirOfMotion.equals(new Vector3f(0, 1, 0)) || dirOfMotion.equals(new Vector3f(0, -1, 0)) && (Math.abs(dirOfMotion.dot(contact.worldNormal)) < 0.55f))
		{
			contact.worldNormal.set(dirOfMotion).negate();
		}
		
		Vector3f relativeVelocity = new Vector3f(mov.velocity).mul(restitution + 1).negate();
		float normalVelocity = relativeVelocity.dot(contact.worldNormal);
		float impulseMagnitude = normalVelocity / inverseMassTotal;
		Vector3f resultant = new Vector3f(contact.worldNormal).mul(impulseMagnitude).div(dt);
		
		if (resultant.equals(new Vector3f(Float.NaN)))
		{
			resultant.set(0);
		}
		else if (resultant.length() <= 0.001f)
		{
			mov.momentum.set(0);
			mov.velocity.set(0);
			resultant.set(0);
		}
		else if(resultant.dot(dirOfMotion) > 0)
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
	 * Applies friction to entities moving along fixed surfaces.
	 * 
	 * @param e The entity to apply friction to.
	 */
	private void applyFriction(Entity e)
	{
		Movable mov = e.getComponent(Movable.class);
		if (Math.abs(new Vector3f(dirOfMotion).dot(contact.worldNormal)) < 0.25)
		{
			float angle = (float) (Math.PI - new Vector3f(0, -1, 0).angle(contact.worldNormal));
			Vector3f resultant = new Vector3f(contact.worldNormal)
					.mul((float) (e.getComponent(Mass.class).mass * 9.81f * Math.cos(angle)));
			Vector3f frictionalForce = new Vector3f(dirOfMotion).negate().mul(resultant.length() * friction);
			if (frictionalForce.length() != 0)
			{
				mov.force.set(0);
				mov.momentum.set(0);
			}
			else if (new Vector3f(mov.force).add(frictionalForce).dot(new Vector3f(0, -1, 0)) <= 0 || contact.worldNormal.length() == 0)
			{
				mov.force.add(frictionalForce);
			}
			
		}
	}
}