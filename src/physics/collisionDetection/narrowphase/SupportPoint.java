package physics.collisionDetection.narrowphase;

import org.joml.Vector3f;

/**
 * A supportPoint holds the value of the Minkowski difference and the value
 * of the support points of the two entities used to calculate it.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class SupportPoint{

	public Vector3f v;
	public Vector3f a;
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
