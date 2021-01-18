package uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.contactGeneration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.detection.narrowphase.NarrowPhaseDetector;
import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.detection.narrowphase.Simplex;
import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.detection.narrowphase.SupportPoint;

/**
 * Generates the contact data of a collision using the Expanding Polytope algorithm.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class ContactPointGenerator
{
	private ContactPoint data;
	private List<Triangle> triangles = new ArrayList<>();
	private List<Edge> edges = new ArrayList<>();
	private int currentIteration = 0;
	
	private static final float EXIT_THRESHOLD = 0.001f;
	private static final int ITERATION_LIMIT = 50;
	
	/**
	 * Constructs a generator for the given simplex, setting the initial
	 * triangles of the polytope.
	 * 
	 * @param sim The simplex of the collision to generate contact data for.
	 */
	public ContactPointGenerator(Simplex sim)
	{
		triangles.add(new Triangle(sim.a, sim.b, sim.c));
		triangles.add(new Triangle(sim.a, sim.c, sim.d));
		triangles.add(new Triangle(sim.a, sim.d, sim.b));
		triangles.add(new Triangle(sim.b, sim.d, sim.c));
	}
	
	/**
	 * Attempts to generate collision data and returns true if the collision
	 * data was successfully generated.
	 * 
	 * @return true if collision data was successfully generated.
	 */
	public boolean generateCollisionData()
	{
		data = new ContactPoint();
		while (true)
		{
			if (currentIteration++ >= ITERATION_LIMIT)
			{
				return false;
			}
			
			float currentDistance = Float.POSITIVE_INFINITY;
			Triangle closestTriangle = null;
			for (Triangle triangle : triangles)
			{
				float distance = Math.abs(new Vector3f(triangle.getNormal()).dot(triangle.getPointA().v));
				if (distance < currentDistance)
				{
					currentDistance = distance;
					closestTriangle = triangle;
				}
			}
			if (closestTriangle == null)
			{
				break;
			}
			SupportPoint sup = NarrowPhaseDetector.generateSupport(new Vector3f(closestTriangle.getNormal()));
			
			if((new Vector3f(closestTriangle.getNormal()).dot(sup.v) - currentDistance < EXIT_THRESHOLD))
			{
				generateContactInformation(closestTriangle);
				break;
			}
			
			for (Iterator<Triangle> iter = triangles.iterator(); iter.hasNext();)
			{
				Triangle triangle  = iter.next();
				if (new Vector3f(triangle.getNormal()).dot(new Vector3f(sup.v).sub(triangle.getPointA().v)) > 0)
				{
					processEdge(triangle.getPointA(), triangle.getPointB());
					processEdge(triangle.getPointB(), triangle.getPointC());
					processEdge(triangle.getPointC(), triangle.getPointA());
					iter.remove();
				}
			}
			
			for (Edge edge : edges)
			{
				 triangles.add(new Triangle(sup, edge.getPointA(), edge.getPointB()));
			}
			
			edges.clear();
		}
		return true;
	}

	/**
	 * Returns the generated contact point data.
	 * 
	 * @return The contatc point data generated.
	 * @throws Exception Exception thrown if no collision data generated.
	 */
	public ContactPoint getContactPoint() throws Exception
	{
		if (data == null)
		{
			throw new Exception("Please ensure data is generated first by calling generateCollisionData().");
		}
		return data;
	}
	
	/**
	 * Processes the given edge between support point a and b.
	 * If there exists an edge with opposite start and end points
	 * then the corresponding edge is removed from the list. Otherwise
	 * this new edge is added.
	 * 
	 * @param a The start of the edge.
	 * @param b The end of the edge.
	 */
	private void processEdge(SupportPoint a, SupportPoint b)
	{
		for (Iterator<Edge> iter = edges.iterator(); iter.hasNext();)
		{
			Edge edge = iter.next();
			if (edge.getPointA() == b && edge.getPointB() == a)
			{
				iter.remove();
				return;
			}
		}
		edges.add(new Edge(a, b));
	}
	
	/**
	 * Generates the contact information from the contact triangle.
	 * 
	 * @param triangle The triangle where the contact is taking place.
	 */
	private void generateContactInformation(Triangle triangle)
	{
		float distanceFromO = new Vector3f(triangle.getNormal()).dot(triangle.getPointA().v);
		float[] barycentric = barycentric(new Vector3f(triangle.getNormal()).mul(distanceFromO), 
				triangle.getPointA().v, triangle.getPointB().v, triangle.getPointC().v);
		// Position in the scene where the point of contact is.
		data.worldPoint = new Vector3f(triangle.getPointA().a).mul(barycentric[0])
								.add(new Vector3f(triangle.getPointB().a).mul(barycentric[1]))
								.add(new Vector3f(triangle.getPointC().a).mul(barycentric[2]));
		// The normal direction to this contact point.
		data.worldNormal = new Vector3f(triangle.getNormal()).negate();
		// The depth of penetration between the two entities in contact.
		data.penDepth = Math.abs(new Vector3f(triangle.getNormal()).dot(triangle.getPointA().v));
	}
	
	/*
	 * Calculates barycentric coordinates of the origin projected onto closest triangle.
	 */
	private float[] barycentric(Vector3f p, Vector3f a, Vector3f b, Vector3f c)
	{
		// Adapted from Jacob Tynall's use of Crister Erickson's code from his
		// Real-Time Collision Detection.
		Vector3f v0 = new Vector3f(b).sub(a);
		Vector3f v1 = new Vector3f(c).sub(a);
		Vector3f v2 = new Vector3f(p).sub(a);
		
		float d00 = new Vector3f(v0).dot(v0);
		float d01 = new Vector3f(v0).dot(v1);
		float d11 = new Vector3f(v1).dot(v1);
		float d20 = new Vector3f(v2).dot(v0);
		float d21 = new Vector3f(v2).dot(v1);
		float denom = d00 * d11 - d01 * d01;
		
		float[] barycentric = new float[3];
		barycentric[0] = (d11 * d20 - d01 * d21) / denom;
		barycentric[1] = (d00 * d21 - d01 * d20) / denom;
		barycentric[2] = 1.0f - barycentric[0] - barycentric[1];
		return barycentric;
	}
}
