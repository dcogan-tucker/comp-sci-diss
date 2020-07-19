package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;
import utils.FileUtils;

/**
 * A MoveableCollidableGameObject with defined vertex data and texture to form a cube.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class CollidableCube extends MoveableCollidableGameObject
{
	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "src/resources/borderSquare.png";

	/**
	 * Mesh component of a cube.
	 */
	private static Mesh mesh = FileUtils.loadOBJFile("/resources/cube.obj");

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
	 * @param material The cube material.
	 */
	private CollidableCube(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector3f scale, float weight)
	{
		super(mesh, material, pos, rot, scale, weight);
	}

	/**
	 * Constructs a CollidableCube with the given position, rotation and scale. Private 
	 * constructor as to ensure that the static create method is used to create a new 
	 * CollidableCube entity.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The cube's rotation.
	 * @param scale The scale of the cube.
	 * @return A CollidableCube.
	 */
	public static CollidableCube create(Vector3f pos, Vector3f rot, Vector3f scale, float weight)
	{
		material.texturePath = texturePath;
		return new CollidableCube(mesh, material, pos, rot, scale, weight);
	}
}
