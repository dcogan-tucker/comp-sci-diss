package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;
import utils.FileUtils;

public class Sphere extends GameObject
{

	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "src/resources/borderSquare.png";

	/**
	 * Mesh component of a cube.
	 */
	private static Mesh mesh = FileUtils.loadOBJFile("/resources/sphere.obj");

	/**
	 * Material component of a cube.
	 */
	private static Material material = new Material();
	
	private Sphere(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector3f scale, float mass)
	{
		super(mesh, material, pos, rot, scale, mass);
	}

	public static Sphere create(Vector3f pos, Vector3f rot, Vector3f scale, float mass)
	{
		material.texturePath = texturePath;
		return new Sphere(mesh, material, pos, rot, scale, mass);
	}
}
