package systems.physics.collision.contactGeneration;

import org.joml.Vector3f;

/**
 * The point of contact of a collision.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class ContactPoint implements Cloneable
{
	/**
	 * The world position of the collision.
	 */
	public Vector3f worldPoint = new Vector3f();
	
	/**
	 * The normal direction to the collision.
	 */
	public Vector3f worldNormal = new Vector3f();
	
	/**
	 * The depth of the penetration during the collision.
	 */
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
