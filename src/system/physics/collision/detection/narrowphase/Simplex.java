package system.physics.collision.detection.narrowphase;

/**
 * A simplex is the generalisation of a tetrahedral shape to n dimensions. This class stores a simplex
 * from 0 to 3 dimensions, forming a point, line segment, triangle and tetrahedron respectively.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Simplex
{
	/**
	 * The number of points in the simplex.
	 */
	private int num;
	
	/**
	 * Point a of the simplex.
	 */
	public SupportPoint a;
	
	/**
	 * Point b of the simplex.
	 */
	public SupportPoint b;
	
	/**
	 * Point c of the simplex.
	 */
	public SupportPoint c;
	
	/**
	 * Point d of the simplex.
	 */
	public SupportPoint d;
	
	/**
	 * Sets the simplex as a tetrahedron (3-simplex) with the given points a, b, c, and d.
	 *
	 * @param a Point a of the tetraheadron.
	 * @param b Point b of the tetraheadron.
	 * @param c Point c of the tetraheadron.
	 * @param d Point d of the tetraheadron.
	 */
	public void set(SupportPoint a, SupportPoint b, SupportPoint c, SupportPoint d)
	{
		num = 4;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	/**
	 * Sets the simplex as a triangle (2-simplex) with the given points a, b, and d.
	 *
	 * @param a Point a of the triangle.
	 * @param b Point b of the triangle.
	 * @param c Point c of the triangle.
	 */
	public void set(SupportPoint a, SupportPoint b, SupportPoint c)
	{
		num = 3;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	/**
	 * Sets the simplex as a line segment (1-simplex) with the given points a, and b.
	 *
	 * @param a Point a of the line segment.
	 * @param b Point b of the line segment.
	 */
	public void set(SupportPoint a, SupportPoint b)
	{
		num = 2;
		this.a = a;
		this.b = b;
	}
	
	/**
	 * Sets the simplex as a point (0-simplex) with the given point a.
	 *
	 * @param a The single point of the simplex.
	 */
	public void set(SupportPoint a)
	{
		num = 1;
		this.a = a;
	}
	
	/**
	 * Pushes a new point onto the simplex. Every point is pushed one along and the new
	 * point is place as point a i.e d = c, c = b, b = a, a = new. Thus if the simplex is
	 * already max size the last point is removed.
	 * 
	 * @param point The point to push to the simplex.
	 */
	public void push(SupportPoint point)
	{
		num = Math.min(num+1, 4);
		d = c;
		c = b;
		b = a;
		a = point;
	}
	
	/**
	 * Clears the simplex to contain no points.
	 */
	public void clear()
	{
		num = 0;
		a = b = c = d = null;
	}
	
	/**
	 * Returns the number of points currently in the simplex.
	 * 
	 * @return The number of points in the simplex.
	 */
	public int getNumberOfPoints()
	{
		return num;
	}
	
	@Override
	public String toString()
	{
		String result = "";
		if (num > 0)
		{
			result += "A: " + a.v;
		}
		if (num > 1)
		{
			result += " B: " + b.v;
		}
		if (num > 2)
		{
			result += " C: " + c.v;
		}
		if (num > 3)
		{
			result += " D: " + d.v;
		}
		return result;
	}
}
