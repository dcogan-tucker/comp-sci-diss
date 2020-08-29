package ecs.system.physics.collision.contactGeneration;

import ecs.system.physics.collision.narrowphase.SupportPoint;

public class Edge
{
	public SupportPoint points[] = new SupportPoint[2];
	
	public Edge(SupportPoint a, SupportPoint b)
	{
		points[0] = a;
		points[1] = b;
	}
}
