package physics.collisionDetection.narrowphase;

import org.joml.Vector3f;

import ecs.entity.Entity;
import ecs.component.*;

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
	private static Simplex sim;
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
	public static boolean areIntersecting(Entity a, Entity b)
	{
		init(a, b);
		return createSimplex();
	}
	
	/**
	 * Returns the current simplex in the NarrowPhaseDetector.
	 * 
	 * @return The current simplex.
	 */
	public static Simplex getSimplex()
	{
		return sim;
	}
	
	/**
	 * Initialise the values for the current pair of entities being checked
	 * for collision.
	 * 
	 * @param a The first entity.
	 * @param b The second entity.
	 */
	private static void init(Entity a, Entity b)
	{
		hullA = a.getComponent(Collidable.class).hull;
		hullB = b.getComponent(Collidable.class).hull;
		sim = new Simplex();
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
		sim.push(sup);
		currentDir = new Vector3f(sim.a.v).negate();
		currentIteration = 0;
		
		while (true)
		{
			SupportPoint newSup = minkowskiDifference();
			if (new Vector3f(newSup.v).dot(currentDir) < 0 || currentIteration++ > EXIT_ITERATION)
			{
				return false;
			}
			
			sim.push(newSup);
			
			if (sim.getNumberOfPoints() == 2)
			{
				Vector3f ab = new Vector3f(sim.b.v).sub(sim.a.v);
				Vector3f ao = new Vector3f(sim.a.v).negate();
				
				currentDir = new Vector3f(ab).cross(ao).cross(ab);
				continue;
			}
			
			if (sim.getNumberOfPoints() == 3)
			{
				Vector3f ab = new Vector3f(sim.b.v).sub(sim.a.v);
				Vector3f ac = new Vector3f(sim.c.v).sub(sim.a.v);
				Vector3f ao = new Vector3f(sim.a.v).negate();
				Vector3f abc = new Vector3f(ab).cross(ac);
				
				if (simplexTest(new Vector3f(ab).cross(abc)))
				{
					sim.set(sim.a, sim.b);
					currentDir = new Vector3f(ab).cross(ao).cross(ab);
					continue;
				}
				
				if (simplexTest(new Vector3f(abc).cross(ac)))
				{
					sim.set(sim.a, sim.c);
					currentDir = new Vector3f(ac).cross(ao).cross(ac);
					continue;
				}
				
				if (simplexTest(new Vector3f(abc)))
				{
					currentDir = new Vector3f(abc);
					continue;
				}
				
				sim.set(sim.a, sim.c, sim.b);
				currentDir = new Vector3f(abc).negate();
				continue;
			}
			
			if (sim.getNumberOfPoints() == 4)
			{
				Vector3f ab = new Vector3f(sim.b.v).sub(sim.a.v);
				Vector3f ac = new Vector3f(sim.c.v).sub(sim.a.v);
				
				if (simplexTest(new Vector3f(ab).cross(ac)))
				{
					faceCheck();
					continue;
				}
				
				Vector3f ad = new Vector3f(sim.d.v).sub(sim.a.v);
				
				if (simplexTest(new Vector3f(ac).cross(ad)))
				{
					sim.set(sim.a, sim.c, sim.d);
					faceCheck();
					continue;
				}
				
				if (simplexTest(new Vector3f(ad).cross(ab)))
				{
					sim.set(sim.a, sim.d, sim.b);
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
		Vector3f ab = new Vector3f(sim.b.v).sub(sim.a.v);
		Vector3f ac = new Vector3f(sim.c.v).sub(sim.a.v);
		Vector3f ao = new Vector3f(sim.a.v).negate();
		Vector3f abc = new Vector3f(ab).cross(ac);
		
		if (simplexTest(new Vector3f(ab).cross(abc)))
		{
			sim.set(sim.a, sim.b);
			currentDir = new Vector3f(ab).cross(ao).cross(ab);
		}
		else if (simplexTest(new Vector3f(abc).cross(ac)))
		{
			sim.set(sim.a, sim.c);
			currentDir = new Vector3f(ac).cross(ao).cross(ac);
		}
		else
		{
			sim.set(sim.a, sim.b, sim.c);
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
		return new Vector3f(v).dot(new Vector3f(sim.a.v).negate()) > 0;
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
