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
	
	/**
	 * Constructs a Sphere at a given position within the scene with a given
	 * rotation, scale and material.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The rotation of the sphere.
	 * @param scale The scale of the sphere.
	 * @param weight The weight of the sphere.
	 */
	private Sphere(Vector3f pos, Vector3f rot, float scale, float mass)
	{
		super(mesh, material, pos, rot, new Vector3f(scale), mass);
		Weight w = this.getComponent(Weight.class);
		w.inertia = (2.0f / 5) * mass * (39.5f * scale) * (39.5f * scale);
		w.inverseInertia = 1.0f / w.inertia;
	}

	/**
	 * Constructs a Sphere with the given position, rotation, scale and weight.
	 * constructor as to ensure that the static create method is used to create a new 
	 * Sphere entity.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The sphere's rotation.
	 * @param scale The scale of the sphere.
	 * @param weight The weight of the sphere.
	 * @return A Sphere with the given parameters.
	 */
	public static Sphere create(Vector3f pos, Vector3f rot, float scale, float mass)
	{
		material.texturePath = texturePath;
		return new Sphere(pos, rot, scale, mass);
	}
}
