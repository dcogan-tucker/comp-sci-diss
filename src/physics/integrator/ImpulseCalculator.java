package physics.integrator;

import java.util.Arrays;
import org.joml.Vector3f;

import ecs.component.Moveable;
import ecs.component.State;
import ecs.component.Weight;
import ecs.entity.Entity;
import physics.collisionDetection.collisionData.Collision;
import physics.collisionDetection.collisionData.ContactData;
import physics.collisionDetection.collisionData.ContactPoint;

public class ImpulseCalculator
{
	private static final float G = 6.67E-11f;
	private static final float R = 6.38E6f;
	private static final float M = 5.97E24f;
	
	public static final float GRAVITAIONAL_ACCELERATION = (G * M) / (R * R);
	
	private Entity a;
	private Entity b;
	private ContactData data;
	private ContactPoint average;
	private float dt;
	
	private ImpulseCalculator(Collision collision, float dt)
	{
		this.a = collision.a;
		this.b = collision.b;
		this.data = collision.data;
		this.dt = dt;
		average = new ContactPoint();
		average.worldPoint = data.contacts[0].worldPoint;
		average.worldNormal = data.contacts[0].worldNormal;
		average.penDepth = data.contacts[0].penDepth;
//		for (ContactPoint contact : data.contacts)
//		{
//			average.worldNormal.add(contact.worldNormal);
//			average.worldPoint.add(contact.worldPoint);
//			average.penDepth += contact.penDepth;
//		}
//		
//		average.worldNormal.normalize();
//		average.worldPoint.div(4);
//		average.penDepth /= 4;
	}
	
	public static void calculate(Collision collision, float dt)
	{
		ImpulseCalculator impc = new ImpulseCalculator(collision, dt);
		impc.resultantForce();
	}
	
	private void resultantForce()
	{
		if (a.hasComponent(Moveable.class) && b.hasComponent(Moveable.class))
		{
			generateResultantForce(a);
			generateTorque(a);
			generateResultantForce(b);
			generateTorque(b);
		}
		else if (a.hasComponent(Moveable.class))
		{
			generateResultantForce(a);
			generateTorque(a);
		}
		else if (b.hasComponent(Moveable.class))
		{
			generateResultantForce(b);
			generateTorque(b);
		}	
	}
	
	
	private void generateResultantForce(Entity a)
	{
		Moveable mov = ((Moveable) a.getComponent(Moveable.class));
		Vector3f dirOfMotion = new Vector3f(mov.momentum).normalize();
		if (mov.momentum.equals(new Vector3f()))
		{
			dirOfMotion.set(0, -1, 0);
		}
		if (dirOfMotion.dot(average.worldNormal) > 0)
		{
			average.worldNormal.negate();
		}
		Vector3f axis = new Vector3f(dirOfMotion).cross(average.worldNormal);
		float angle = new Vector3f(dirOfMotion).angle(average.worldNormal);
		if (angle >= Math.PI / 2)
		{
			angle = 0;
		}
		Vector3f resultant = new Vector3f(mov.momentum).negate()
				.add(new Vector3f(average.worldNormal).normalize().rotateAxis(-angle, axis.x, axis.y, axis.z)
						.mul(mov.momentum.length()).mul(1f)).div(dt);
		if (resultant.equals(new Vector3f(Float.NaN)))
		{
			resultant.set(0);
		}
		if(resultant.dot(dirOfMotion) > 0)
		{
			resultant.negate();
		}
		((Moveable) a.getComponent(Moveable.class)).force.set(resultant);
	}
	
	private void generateTorque(Entity a)
	{
		Moveable mov = ((Moveable) a.getComponent(Moveable.class));
		Vector3f dirOfMotion = new Vector3f(mov.momentum).normalize();
		State state = ((State) a.getComponent(State.class));
		Vector3f rotatedAxis = new Vector3f();
		if (!state.rotation.equals(new Vector3f()))
		{
			rotatedAxis.set(state.rotation).normalize();
		}
		float angleToNormal = state.rotation.length();
		Vector3f torque;
		if (dirOfMotion.dot(average.worldNormal) < -0.95f 
				|| average.worldNormal
				.dot(new Vector3f(0, 1, 0)
						.rotateAxis((float) Math.toRadians(angleToNormal), rotatedAxis.x, rotatedAxis.y, rotatedAxis.z)) > 0.9999f)
		{
			torque = new Vector3f();
		}
		else
		{
			torque = new Vector3f(average.worldNormal).cross(dirOfMotion).normalize().mul(9.81f);
		}
		((Moveable) a.getComponent(Moveable.class)).torque.set(torque);
	}
	
	public Entity getEntityA()
	{
		return a;
	}
	
	public Entity getEntityB()
	{
		return b;
	}
}
