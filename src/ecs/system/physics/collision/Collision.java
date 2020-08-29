package ecs.system.physics.collision;

import ecs.component.Moveable;
import ecs.entity.Entity;
import ecs.system.physics.collision.contactGeneration.ContactPoint;
import ecs.system.physics.collision.narrowphase.Simplex;

public class Collision implements Comparable<Collision>
{
	public Entity a;
	public Entity b;
	public Simplex sim;
	public ContactPoint contact;
	
	public Collision(Entity a, Entity b)
	{
		if (a.hasComponent(Moveable.class))
		{
			this.a = a;
			this.b = b;
		}
		else
		{
			this.a = b;
			this.b = a;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		return (a.equals(((Collision) o).a) && b.equals(((Collision) o).b)) || 
				(a.equals(((Collision) o).b) && b.equals(((Collision) o).a));
	}
	

	@Override
	public int compareTo(Collision col)
	{
		if (this.equals(col))
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	@Override
	public String toString()
	{
		return "Entities, " + a + " and " + b + " are colliding";
	}
}
