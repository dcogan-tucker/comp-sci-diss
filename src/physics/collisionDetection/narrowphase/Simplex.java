package physics.collisionDetection.narrowphase;

/**
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Simplex {

	private int num;
	
	public SupportPoint a;
	public SupportPoint b;
	public SupportPoint c;
	public SupportPoint d;
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
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
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	public void set(SupportPoint a, SupportPoint b, SupportPoint c)
	{
		num = 3;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 */
	public void set(SupportPoint a, SupportPoint b)
	{
		num = 2;
		this.a = a;
		this.b = b;
	}
	
	/**
	 * 
	 * @param a
	 */
	public void set(SupportPoint a)
	{
		num = 1;
		this.a = a;
	}
	
	/**
	 * 
	 * @param point
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
	 * 
	 */
	public void clear()
	{
		num = 0;
	}
	
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
