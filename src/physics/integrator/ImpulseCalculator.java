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
	
	private ImpulseCalculator(Collision collision)
	{
		this.a = collision.a;
		this.b = collision.b;
		this.data = collision.data;
		average = new ContactPoint();
		for (ContactPoint contact : data.contacts)
		{
			average.worldNormal.add(contact.worldNormal);
			average.worldPoint.add(contact.worldPoint);
			average.penDepth += contact.penDepth;
		}
		
		average.worldNormal.normalize();
		average.worldPoint.div(4);
		average.penDepth /= 4;
	}
	
	public static void calculate(Collision collision)
	{
		ImpulseCalculator impc = new ImpulseCalculator(collision);
		impc.resultantForce();
	}
	
	private void resultantForce()
	{
		if (a.hasComponent(Moveable.class) && b.hasComponent(Moveable.class))
		{
			resultantForce(a);
			resultantForce(b);
		}
		else if (a.hasComponent(Moveable.class))
		{
			resultantForce(a);
		}
		else if (b.hasComponent(Moveable.class))
		{
			resultantForce(b);
		}	
	}
	
	
	private void resultantForce(Entity a)
	{
		float mass = ((Weight) a.getComponent(Weight.class)).mass;
		Vector3f gravitationlForce = new Vector3f(0, -GRAVITAIONAL_ACCELERATION, 0).mul(mass);
		Vector3f dirOfMotion = new Vector3f(((Moveable) a.getComponent(Moveable.class)).momentum).normalize();
		float angle = new Vector3f(average.worldNormal).angle(dirOfMotion);
		System.out.println(average);
		Vector3f resultant = new Vector3f(average.worldNormal).normalize().mul((float) (gravitationlForce.length() * Math.sin(angle))).mul(100);
		((Moveable) a.getComponent(Moveable.class)).force.set(resultant);
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
