package system.physics.collision.contactGeneration;

import org.joml.Vector3f;

import system.physics.collision.detection.narrowphase.SupportPoint;

/**
 * Holds the 3 points of a triangle and the normal to the edges AB and AC.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Triangle
{
	/**
	 * The three points of a triangle.
	 */
	private SupportPoint points[] = new SupportPoint[3];
	
	/**
	 * The normal to the edges AB and AC.
	 */
	private Vector3f normal;
	
	/**
	 * Constructs a triangle with the points a, b and c. Calculating the normal
	 * to the edges AB and AC.
	 * 
	 * @param a Point A of the triangle.
	 * @param b Point B of the triangle.
	 * @param c Point C of the triangle.
	 */
	public Triangle(SupportPoint a, SupportPoint b, SupportPoint c)
	{
		update(a, b, c);
	}
	
	/**
	 * Updates the points of the triangle and the normal.
	 * 
	 * @param a Point A of the triangle.
	 * @param b Point B of the triangle.
	 * @param c Point C of the triangle.
	 */
	public void update(SupportPoint a, SupportPoint b, SupportPoint c)
	{
		points[0] = a;
		points[1] = b;
		points[2] = c;
		normal = (new Vector3f(b.v).sub(a.v)).cross(new Vector3f(c.v).sub(a.v)).normalize();
	}
	
	/**
	 * Returns point A of the triangle.
	 * 
	 * @return Point A of the triangle.
	 */
	public SupportPoint getPointA()
	{
		return points[0];
	}
	
	/**
	 * Returns point B of the triangle.
	 * 
	 * @return Point B of the triangle.
	 */
	public SupportPoint getPointB()
	{
		return points[1];
	}
	
	/**
	 * Returns point C of the triangle.
	 * 
	 * @return Point C of the triangle.
	 */
	public SupportPoint getPointC()
	{
		return points[2];
	}
	
	/**
	 * Returns the normal of the edges AB and AC of the triangle.
	 * 
	 * @return The normal to edges AB and AC.
	 */
	public Vector3f getNormal()
	{
		return normal;
	}
}
