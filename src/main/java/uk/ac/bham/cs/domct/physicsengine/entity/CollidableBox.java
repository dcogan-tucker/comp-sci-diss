package uk.ac.bham.cs.domct.physicsengine.entity;

import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.component.Mass;
import uk.ac.bham.cs.domct.physicsengine.component.Material;
import uk.ac.bham.cs.domct.physicsengine.component.Mesh;
import uk.ac.bham.cs.domct.physicsengine.utils.FileUtils;

/**
 * A CollidableBox extends CollidableGameObject and can be collided with by other collidable entities.
 * The CollidableBox is a stationary entity so will not be moved by collisions.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class CollidableBox extends CollidableGameObject
{
	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "resources/textures/concrete.png";

	/**
	 * Mesh component of a cube.
	 */
	private static Mesh mesh = FileUtils.loadOBJFile("cube.obj");

	/**
	 * Material component of a cube.
	 */
	private static Material material = new Material();

	/**
	 * Constructs a CollidableBox at a given position within the scene with a given
	 * rotation, scale and material.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The rotation of the cube.
	 * @param scale The scale of the cube.
	 * @param weight The weight of the cube.
	 */
	private CollidableBox(Vector3f pos, Vector3f rot, float scale, float weight)
	{
		super(mesh, material, pos, rot, new Vector3f(scale), weight);
		Mass w =  this.getComponent(Mass.class);
		w.inertia = (1.0f / 6) * weight * scale * scale;
		w.inverseInertia = 1.0f / w.inertia;
	}

	/**
	 * Constructs a CollidableBox with the given position, rotation, scale and weight.
	 * constructor as to ensure that the static create method is used to create a new 
	 * CollidableCube entity.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The cube's rotation.
	 * @param scale The scale of the cube.
	 * @param weight The weight of the cube.
	 * @return A CollidableCube with the given parameters.
	 */
	public static CollidableBox create(Vector3f pos, Vector3f rot, float scale, float weight)
	{
		material.texturePath = texturePath;
		return new CollidableBox(pos, rot, scale, weight);
	}
}
