package physics.integrator;

import javax.xml.crypto.Data;

import org.joml.Vector3f;

import ecs.component.Moveable;
import ecs.component.Weight;
import ecs.entity.Entity;
import physics.collisionDetection.collisionData.CollisionResolver;
import physics.collisionDetection.collisionData.ContactData;
import physics.collisionDetection.narrowphase.Simplex;

public class ImpulseCalculator
{
	public static final float G = 6.67E-11f;
	public static final float R = 6.38E6f;
	public static final float M = 5.97E24f;
	
	private Entity a;
	private Entity b;
	private Simplex sim;
	private ContactData data;
	
	private ImpulseCalculator(Entity a, Entity b, Simplex sim)
	{
		this.a = a;
		this.b = b;
		this.sim = sim;
	}
	
	public static void calculate(Entity a, Entity b, Simplex sim)
	{
		ImpulseCalculator impc = new ImpulseCalculator(a, b, sim);
		//return impc.calculate();
	}
	
	private void calculate()
	{
		CollisionResolver cr = new CollisionResolver(sim);
		if (cr.generateCollisionData())
		{
			data = cr.getContactData();
		}
		
		Vector3f na = normalForce(a);
		Vector3f nb = normalForce(b);
		
		if (a.hasComponent(Moveable.class) && b.hasComponent(Moveable.class))
		{
		}
		else if (a.hasComponent(Moveable.class))
		{
			
		}
		else if (b.hasComponent(Moveable.class))
		{
			
		}
	}
	
	private Vector3f normalForce(Entity e)
	{
		double angle = new Vector3f(data.worldNormal).negate()
				.angle(new Vector3f(((Moveable) e.getComponent(Moveable.class)).momentum).normalize());
		return new Vector3f(0, 9.81f, 0)
				.mul(((Weight) e.getComponent(Weight.class)).mass)
				.mul((float) Math.cos(angle));
	}
}
