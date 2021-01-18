package uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.contactGeneration;

import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.detection.narrowphase.SupportPoint;

/**
 * Class to hold data for an edge.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Edge
{
	/**
	 * The two points of the edge.
	 */
	private SupportPoint[] points = new SupportPoint[2];

	public Edge(SupportPoint a, SupportPoint b)
	{
		points[0] = a;
		points[1] = b;
	}
	
	/**
	 * Gets the start point of the edge.
	 * 
	 * @return The start of the edge.
	 */
	public SupportPoint getPointA()
	{
		return points[0];
	}
	
	/**
	 * Gets the end point of the edge.
	 * 
	 * @return The end of the edge.
	 */
	public SupportPoint getPointB()
	{
		return points[1];
	}
}
