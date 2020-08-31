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
		hullA = collision.getEntityA().getComponent(Collidable.class).hull;
		hullB = collision.getEntityB().getComponent(Collidable.class).hull;
		if (collision.getSimplex() == null)
		{
			collision.setSimplex(new Simplex());
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
		collision.getSimplex().push(sup);
		currentDir = new Vector3f(collision.getSimplex().a.v).negate();
		currentIteration = 0;
		
		while (true)
		{
			SupportPoint newSup = minkowskiDifference();
			if (new Vector3f(newSup.v).dot(currentDir) < 0 || currentIteration++ > EXIT_ITERATION)
			{
				return false;
			}
			
			collision.getSimplex().push(newSup);
			
			if (collision.getSimplex().getNumberOfPoints() == 2)
			{
				Vector3f ab = new Vector3f(collision.getSimplex().b.v).sub(collision.getSimplex().a.v);
				Vector3f ao = new Vector3f(collision.getSimplex().a.v).negate();
				
				currentDir = new Vector3f(ab).cross(ao).cross(ab);
				continue;
			}
			
			if (collision.getSimplex().getNumberOfPoints() == 3)
			{
				Vector3f ab = new Vector3f(collision.getSimplex().b.v).sub(collision.getSimplex().a.v);
				Vector3f ac = new Vector3f(collision.getSimplex().c.v).sub(collision.getSimplex().a.v);
				Vector3f ao = new Vector3f(collision.getSimplex().a.v).negate();
				Vector3f abc = new Vector3f(ab).cross(ac);
				
				if (simplexTest(new Vector3f(ab).cross(abc)))
				{
					collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().b);
					currentDir = new Vector3f(ab).cross(ao).cross(ab);
					continue;
				}
				
				if (simplexTest(new Vector3f(abc).cross(ac)))
				{
					collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().c);
					currentDir = new Vector3f(ac).cross(ao).cross(ac);
					continue;
				}
				
				if (simplexTest(new Vector3f(abc)))
				{
					currentDir = new Vector3f(abc);
					continue;
				}
				
				collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().c, collision.getSimplex().b);
				currentDir = new Vector3f(abc).negate();
				continue;
			}
			
			if (collision.getSimplex().getNumberOfPoints() == 4)
			{
				Vector3f ab = new Vector3f(collision.getSimplex().b.v).sub(collision.getSimplex().a.v);
				Vector3f ac = new Vector3f(collision.getSimplex().c.v).sub(collision.getSimplex().a.v);
				
				if (simplexTest(new Vector3f(ab).cross(ac)))
				{
					faceCheck();
					continue;
				}
				
				Vector3f ad = new Vector3f(collision.getSimplex().d.v).sub(collision.getSimplex().a.v);
				
				if (simplexTest(new Vector3f(ac).cross(ad)))
				{
					collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().c, collision.getSimplex().d);
					faceCheck();
					continue;
				}
				
				if (simplexTest(new Vector3f(ad).cross(ab)))
				{
					collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().d, collision.getSimplex().b);
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
		Vector3f ab = new Vector3f(collision.getSimplex().b.v).sub(collision.getSimplex().a.v);
		Vector3f ac = new Vector3f(collision.getSimplex().c.v).sub(collision.getSimplex().a.v);
		Vector3f ao = new Vector3f(collision.getSimplex().a.v).negate();
		Vector3f abc = new Vector3f(ab).cross(ac);
		
		if (simplexTest(new Vector3f(ab).cross(abc)))
		{
			collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().b);
			currentDir = new Vector3f(ab).cross(ao).cross(ab);
		}
		else if (simplexTest(new Vector3f(abc).cross(ac)))
		{
			collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().c);
			currentDir = new Vector3f(ac).cross(ao).cross(ac);
		}
		else
		{
			collision.getSimplex().set(collision.getSimplex().a, collision.getSimplex().b, collision.getSimplex().c);
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
		return new Vector3f(v).dot(new Vector3f(collision.getSimplex().a.v).negate()) > 0;
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
