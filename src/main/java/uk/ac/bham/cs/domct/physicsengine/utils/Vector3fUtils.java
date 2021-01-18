package uk.ac.bham.cs.domct.physicsengine.utils;

import org.joml.Vector3f;

public class Vector3fUtils
{

	public static Vector3f toRadians(Vector3f vec)
	{
		return new Vector3f((float) Math.toRadians(vec.x), 
				(float) Math.toRadians(vec.y), (float) Math.toRadians(vec.z));
	}
	
	public static Vector3f toDegrees(Vector3f vec)
	{
		return new Vector3f((float) Math.toDegrees(vec.x), 
				(float) Math.toDegrees(vec.y), (float) Math.toDegrees(vec.z));
	}
}
