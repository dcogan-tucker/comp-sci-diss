package physics.collisionDetection.collisionData;

import org.joml.Vector3f;

public class ContactPoint implements Cloneable
{
	public Vector3f worldPoint = new Vector3f();
	
	public Vector3f worldNormal = new Vector3f();
	
	public float penDepth = 0;
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	@Override
	public String toString()
	{
		return worldPoint + " - " + worldNormal + " - " + penDepth;
	}
}
