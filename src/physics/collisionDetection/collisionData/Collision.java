package physics.collisionDetection.collisionData;

import ecs.entity.Entity;

public class Collision
{
	public Entity a;
	public Entity b;
	public ContactData data;
	
	@Override
	public boolean equals(Object o)
	{
		return (a.equals(((Collision) o).a) && b.equals(((Collision) o).b)) || 
				(a.equals(((Collision) o).b) && b.equals(((Collision) o).a));
	}
}
