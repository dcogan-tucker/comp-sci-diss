package ecs.system.physics.collision.contactGeneration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;

import ecs.system.physics.collision.narrowphase.NarrowPhaseDetector;
import ecs.system.physics.collision.narrowphase.Simplex;
import ecs.system.physics.collision.narrowphase.SupportPoint;

public class CollisionResolver
{
	private ContactPoint data = new ContactPoint();
	private List<Triangle> triangles = new ArrayList<>();
	private List<Edge> edges = new ArrayList<>();
	private int currentIteratioj = 0;
	
	private static final float EXIT_THRESHOLD = 0.001f;
	private static final int ITERATION_LIMIT = 50;
	
	
	public CollisionResolver(Simplex sim)
	{
		triangles.add(new Triangle(sim.a, sim.b, sim.c));
		triangles.add(new Triangle(sim.a, sim.c, sim.d));
		triangles.add(new Triangle(sim.a, sim.d, sim.b));
		triangles.add(new Triangle(sim.b, sim.d, sim.c));
	}
	
	public boolean generateCollisionData()
	{
		while (true)
		{
			if (currentIteratioj++ >= ITERATION_LIMIT)
			{
				return false;
			}
			
			float currentDistance = Float.POSITIVE_INFINITY;
			Triangle closestTriangle = null;
			for (Iterator<Triangle> iter = triangles.iterator(); iter.hasNext();)
			{
				Triangle triangle = iter.next();
				float distance = Math.abs(new Vector3f(triangle.normal).dot(triangle.points[0].v));
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
			SupportPoint sup = NarrowPhaseDetector.generateSupport(new Vector3f(closestTriangle.normal));
			
			if((new Vector3f(closestTriangle.normal).dot(sup.v) - currentDistance < EXIT_THRESHOLD))
			{
				generateContactInformation(closestTriangle);
				break;
			}
			
			for (Iterator<Triangle> iter = triangles.iterator(); iter.hasNext();)
			{
				Triangle triangle  = iter.next();
				if (new Vector3f(triangle.normal).dot(new Vector3f(sup.v).sub(triangle.points[0].v)) > 0)
				{
					processEdge(triangle.points[0], triangle.points[1]);
					processEdge(triangle.points[1], triangle.points[2]);
					processEdge(triangle.points[2], triangle.points[0]);
					iter.remove();
				}
			}
			
			for (Edge edge : edges)
			{
				 triangles.add(new Triangle(sup, edge.points[0], edge.points[1]));
			}
			
			edges.clear();
		}
		return true;
	}

	public ContactPoint getContactPoint()
	{
		return data;
	}
	
	
	private void processEdge(SupportPoint a, SupportPoint b)
	{
		for (Iterator<Edge> iter = edges.iterator(); iter.hasNext();)
		{
			Edge edge = iter.next();
			if (edge.points[0] == b && edge.points[1] == a)
			{
				iter.remove();
				return;
			}
		}
		edges.add(new Edge(a, b));
	}
	
	private void generateContactInformation(Triangle triangle)
	{
		float distanceFromO = new Vector3f(triangle.normal).dot(triangle.points[0].v);
		float[] barycentric = barycentric(new Vector3f(triangle.normal).mul(distanceFromO), 
				triangle.points[0].v, triangle.points[1].v, triangle.points[2].v);
		
		data.worldPoint = new Vector3f(triangle.points[0].a).mul(barycentric[0])
								.add(new Vector3f(triangle.points[1].a).mul(barycentric[1]))
								.add(new Vector3f(triangle.points[2].a).mul(barycentric[2]));
		
		data.worldNormal = new Vector3f(triangle.normal).negate();
		
		data.penDepth = Math.abs(new Vector3f(triangle.normal).dot(triangle.points[0].v));
	}
	
	private float[] barycentric(Vector3f p, Vector3f a, Vector3f b, Vector3f c)
	{
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
