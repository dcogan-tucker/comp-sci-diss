package ecs.system.physics.collision.contactGeneration;

import org.joml.Vector3f;

import ecs.system.physics.collision.narrowphase.SupportPoint;

public class Triangle {

	public SupportPoint points[] = new SupportPoint[3];
	public Vector3f normal;
	
	public Triangle(SupportPoint a, SupportPoint b, SupportPoint c)
	{
		points[0] = a;
		points[1] = b;
		points[2] = c;
		normal = (new Vector3f(b.v).sub(a.v)).cross(new Vector3f(c.v).sub(a.v)).normalize();
	}
}
