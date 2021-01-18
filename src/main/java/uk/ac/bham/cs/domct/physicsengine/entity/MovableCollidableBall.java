package uk.ac.bham.cs.domct.physicsengine.entity;

import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.component.Mass;
import uk.ac.bham.cs.domct.physicsengine.component.Material;
import uk.ac.bham.cs.domct.physicsengine.component.Mesh;
import uk.ac.bham.cs.domct.physicsengine.utils.FileUtils;

public class MovableCollidableBall extends MovableCollidableGameObject
{

	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "resources/textures/bball.png";

	/**
	 * Mesh component of a cube.
	 */
	private static Mesh mesh = FileUtils.loadOBJFile("sphere.obj");

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
	 * @param mass The weight of the sphere.
	 */
	private MovableCollidableBall(Vector3f pos, Vector3f rot, float scale, float mass)
	{
		super(mesh, material, pos, rot, new Vector3f(scale), mass);
		Mass m = this.getComponent(Mass.class);
		m.inertia = (2.0f / 5) * mass * (39.5f * scale) * (39.5f * scale);
		m.inverseInertia = 1.0f / m.inertia;
	}

	/**
	 * Constructs a Sphere with the given position, rotation, scale and weight.
	 * constructor as to ensure that the static create method is used to create a new 
	 * Sphere entity.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The sphere's rotation.
	 * @param scale The scale of the sphere.
	 * @param mass The weight of the sphere.
	 * @return A Sphere with the given parameters.
	 */
	public static MovableCollidableBall create(Vector3f pos, Vector3f rot, float scale, float mass)
	{
		material.texturePath = texturePath;
		return new MovableCollidableBall(pos, rot, scale, mass);
	}
}
