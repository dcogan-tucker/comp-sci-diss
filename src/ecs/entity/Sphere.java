package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;
import ecs.component.Weight;
import utils.FileUtils;

public class Sphere extends MoveableCollidableGameObject
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
	
	private Sphere(Mesh mesh, Material material, Vector3f pos, Vector3f rot, float scale, float mass)
	{
		super(mesh, material, pos, rot, new Vector3f(scale), mass);
		Weight w = ((Weight) this.getComponent(Weight.class));
		w.inertia = (2.0f / 5) * mass * (39.5f * scale) * (39.5f * scale);
		w.inverseInertia = 1.0f / w.inertia;
	}

	public static Sphere create(Vector3f pos, Vector3f rot, float scale, float mass)
	{
		material.texturePath = texturePath;
		return new Sphere(mesh, material, pos, rot, scale, mass);
	}
}
