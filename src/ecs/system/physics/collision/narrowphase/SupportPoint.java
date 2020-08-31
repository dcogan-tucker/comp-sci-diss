package ecs.system.physics.collision.narrowphase;

import org.joml.Vector3f;

/**
 * A supportPoint holds the value of the Minkowski difference and the value
 * of the support points of the two entities used to calculate it.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class SupportPoint
{
	/**
	 * The Minkowski difference value.
	 */
	public Vector3f v;
	
	/**
	 * Support point of entity a.
	 */
	public Vector3f a;
	
	/**
	 * Support point of entity b.
	 */
	public Vector3f b;
	
	/**
	 * Returns whether this support point is equal to another support point.
	 * Two SupportPoint objects are equal if the Minkowski difference value (v)
	 * of each are the same.
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(SupportPoint other)
	{
		return v.equals(other.v);
	}
}
