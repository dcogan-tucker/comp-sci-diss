package ecs.system.physics.collision.narrowphase;

import org.joml.Vector3f;

import ecs.component.*;
import ecs.system.physics.collision.Collision;

/**
 * Class containing static methods used during narrow phase detection to
 * determine if two entities are colliding.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class NarrowPhaseDetector
{
	private static ConvexHull hullA;
	private static ConvexHull hullB;
	private static Collision collision;
	private static Vector3f currentDir;
	
	private static final int EXIT_ITERATION = 1000;
	private static int currentIteration;
	
	/**
	 * Private constructor to ensure that the class isn't initialised unnecessarily. 
	 */
	private NarrowPhaseDetector()
	{
		
	}
	
	/**
	 * Returns whether two given entities are intersecting. If the entities
	 * are intersecting then a simplex surrounding the origin has been
	 * successfully created.
	 * 
	 * @param a The first of the given entities.
	 * @param b The second of the given entities.
	 * @return true if the entities are intersecting.
	 */
	public static boolean areIntersecting(Collision col)
	{
		init(col);
		return createSimplex();
	}
	
	/**
	 * Initialise the values for the current pair of entities being checked
	 * for collision.
	 * 
	 * @param a The first entity.
	 * @param b The second entity.
	 */
	private static void init(Collision col)
	{
		collision = col;
		hullA = collision.a.getComponent(Collidable.class).hull;
		hullB = collision.b.getComponent(Collidable.class).hull;
		if (collision.sim == null)
		{
			collision.sim = new Simplex();
		}
		currentDir = new Vector3f(1, 0, 0);
	}
	
	/**
	 * Creates a simplex using the GJK algorithm that surround the 
	 * origin if two entities are colliding and returns true. If the 
	 * entities aren't colliding then the method will return false.
	 * 
	 * @return true if the entities are colliding.
	 */
	private static boolean createSimplex()
	{
		SupportPoint sup = minkowskiDifference();
		if (Math.abs(new Vector3f(currentDir).dot(sup.v)) >= sup.v.length()*0.8f)
		{
			currentDir = new Vector3f(0, 1, 0);
			sup = minkowskiDifference();
		}
		collision.sim.push(sup);
		currentDir = new Vector3f(collision.sim.a.v).negate();
		currentIteration = 0;
		
		while (true)
		{
			SupportPoint newSup = minkowskiDifference();
			if (new Vector3f(newSup.v).dot(currentDir) < 0 || currentIteration++ > EXIT_ITERATION)
			{
				return false;
			}
			
			collision.sim.push(newSup);
			
			if (collision.sim.getNumberOfPoints() == 2)
			{
				Vector3f ab = new Vector3f(collision.sim.b.v).sub(collision.sim.a.v);
				Vector3f ao = new Vector3f(collision.sim.a.v).negate();
				
				currentDir = new Vector3f(ab).cross(ao).cross(ab);
				continue;
			}
			
			if (collision.sim.getNumberOfPoints() == 3)
			{
				Vector3f ab = new Vector3f(collision.sim.b.v).sub(collision.sim.a.v);
				Vector3f ac = new Vector3f(collision.sim.c.v).sub(collision.sim.a.v);
				Vector3f ao = new Vector3f(collision.sim.a.v).negate();
				Vector3f abc = new Vector3f(ab).cross(ac);
				
				if (simplexTest(new Vector3f(ab).cross(abc)))
				{
					collision.sim.set(collision.sim.a, collision.sim.b);
					currentDir = new Vector3f(ab).cross(ao).cross(ab);
					continue;
				}
				
				if (simplexTest(new Vector3f(abc).cross(ac)))
				{
					collision.sim.set(collision.sim.a, collision.sim.c);
					currentDir = new Vector3f(ac).cross(ao).cross(ac);
					continue;
				}
				
				if (simplexTest(new Vector3f(abc)))
				{
					currentDir = new Vector3f(abc);
					continue;
				}
				
				collision.sim.set(collision.sim.a, collision.sim.c, collision.sim.b);
				currentDir = new Vector3f(abc).negate();
				continue;
			}
			
			if (collision.sim.getNumberOfPoints() == 4)
			{
				Vector3f ab = new Vector3f(collision.sim.b.v).sub(collision.sim.a.v);
				Vector3f ac = new Vector3f(collision.sim.c.v).sub(collision.sim.a.v);
				
				if (simplexTest(new Vector3f(ab).cross(ac)))
				{
					faceCheck();
					continue;
				}
				
				Vector3f ad = new Vector3f(collision.sim.d.v).sub(collision.sim.a.v);
				
				if (simplexTest(new Vector3f(ac).cross(ad)))
				{
					collision.sim.set(collision.sim.a, collision.sim.c, collision.sim.d);
					faceCheck();
					continue;
				}
				
				if (simplexTest(new Vector3f(ad).cross(ab)))
				{
					collision.sim.set(collision.sim.a, collision.sim.d, collision.sim.b);
					faceCheck();
					continue;
				}
			}
			
			break;
		}
		return true;
	}
	
	/**
	 * A helper method for multiple cases when the simplex has 4 points.
	 */
	private static void faceCheck()
	{
		Vector3f ab = new Vector3f(collision.sim.b.v).sub(collision.sim.a.v);
		Vector3f ac = new Vector3f(collision.sim.c.v).sub(collision.sim.a.v);
		Vector3f ao = new Vector3f(collision.sim.a.v).negate();
		Vector3f abc = new Vector3f(ab).cross(ac);
		
		if (simplexTest(new Vector3f(ab).cross(abc)))
		{
			collision.sim.set(collision.sim.a, collision.sim.b);
			currentDir = new Vector3f(ab).cross(ao).cross(ab);
		}
		else if (simplexTest(new Vector3f(abc).cross(ac)))
		{
			collision.sim.set(collision.sim.a, collision.sim.c);
			currentDir = new Vector3f(ac).cross(ao).cross(ac);
		}
		else
		{
			collision.sim.set(collision.sim.a, collision.sim.b, collision.sim.c);
			currentDir = new Vector3f(abc);
		}
	}
	
	/**
	 * Returns whether a given dotted with the vector AO (where A is the
	 * most recent addition to the simplex and O is the origin) is greater
	 * than zero.
	 * 
	 * @param v The vector to check.
	 * @return true if the dotproduct is greater than 0.
	 */
	public static boolean simplexTest(Vector3f v)
	{
		return new Vector3f(v).dot(new Vector3f(collision.sim.a.v).negate()) > 0;
	}
	
	/**
	 * Returns a SupportPoint holding the values of the support point for
	 * entity a and b in the current direction and the resulting Minkowski
	 * difference.
	 * 
	 * @return A SupportPoint for the current direction.
	 */
	private static SupportPoint minkowskiDifference()
	{
		return generateSupport(currentDir);
	}
	
	/**
	 * Returns a SupportPoint holding the values of the support point for
	 * entity a and b in the given direction and the resulting Minkowski
	 * difference.
	 * 
	 * @return A SupportPoint for the current direction.
	 */
	public static SupportPoint generateSupport(Vector3f direction)
	{
		direction.normalize();
		SupportPoint sup = new SupportPoint();
		sup.a = hullA.generateSupportPoint(currentDir);
		sup.b = hullB.generateSupportPoint(new Vector3f(currentDir).negate());
		sup.v = new Vector3f(sup.a).sub(sup.b);
		return sup;
	}
}
