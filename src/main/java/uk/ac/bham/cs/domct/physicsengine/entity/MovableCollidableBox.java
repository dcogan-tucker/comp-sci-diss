package uk.ac.bham.cs.domct.physicsengine.entity;

import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.component.Mass;
import uk.ac.bham.cs.domct.physicsengine.component.Material;
import uk.ac.bham.cs.domct.physicsengine.component.Mesh;
import uk.ac.bham.cs.domct.physicsengine.utils.FileUtils;

/**
 * A MoveableCollidableGameObject with defined vertex data and texture to form a cube.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class MovableCollidableBox extends MovableCollidableGameObject
{
	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "resources/textures/wood.png";

	/**
	 * Mesh component of a cube.
	 */
	private static Mesh mesh = FileUtils.loadOBJFile("cube.obj");

	/**
	 * Material component of a cube.
	 */
	private static Material material = new Material();

	/**
	 * Constructs a CollidableCube at a given position within the scene with a given
	 * rotation, scale and material.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The rotation of the cube.
	 * @param scale The scale of the cube.
	 * @param weight The weight of the cube.
	 */
	private MovableCollidableBox(Vector3f pos, Vector3f rot, float scale, float weight)
	{
		super(mesh, material, pos, rot, new Vector3f(scale), weight);
		Mass m =  this.getComponent(Mass.class);
		m.inertia = (1.0f / 6) * weight * scale * scale;
		m.inverseInertia = 1.0f / m.inertia;
	}

	/**
	 * Constructs a CollidableCube with the given position, rotation, scale and weight.
	 * constructor as to ensure that the static create method is used to create a new 
	 * CollidableCube entity.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The cube's rotation.
	 * @param scale The scale of the cube.
	 * @param weight The weight of the cube.
	 * @return A CollidableCube with the given parameters.
	 */
	public static MovableCollidableBox create(Vector3f pos, Vector3f rot, float scale, float weight)
	{
		material.texturePath = texturePath;
		return new MovableCollidableBox(pos, rot, scale, weight);
	}
}
