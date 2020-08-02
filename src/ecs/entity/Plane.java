package ecs.entity;

import org.joml.Vector2f;
import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;
import utils.FileUtils;

public class Plane extends CollidableGameObject
{
	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "src/resources/wall.png";

	/**
	 * Mesh component of a cube.
	 */
	private static Mesh mesh = FileUtils.loadOBJFile("/resources/cube.obj");

	/**
	 * Material component of the plane.
	 */
	private static Material material = new Material();

	private Plane(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector2f scale)
	{
		super(mesh, material, pos, rot, new Vector3f(scale.x, 0.2f, scale.y), Float.POSITIVE_INFINITY);
	}
	
	public static Plane create(Vector3f pos, Vector3f rot, Vector2f scale)
	{
		material.texturePath = texturePath;
		return new Plane(mesh, material, pos, rot, scale);
	}

}
