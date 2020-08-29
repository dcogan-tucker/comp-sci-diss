package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;
import ecs.component.Weight;
import utils.FileUtils;

/**
 * A MoveableCollidableGameObject with defined vertex data and texture to form a cube.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class MoveableCollidableBox extends MoveableCollidableGameObject
{
	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "src/resources/wood.png";

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
	 * @param weight The weight of the cube.
	 */
	private MoveableCollidableBox(Vector3f pos, Vector3f rot, float scale, float weight)
	{
		super(mesh, material, pos, rot, new Vector3f(scale), weight);
		Weight w =  this.getComponent(Weight.class);
		w.inertia = (1.0f / 6) * weight * scale * scale;
		w.inverseInertia = 1.0f / w.inertia;
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
	public static MoveableCollidableBox create(Vector3f pos, Vector3f rot, float scale, float weight)
	{
		material.texturePath = texturePath;
		return new MoveableCollidableBox(pos, rot, scale, weight);
	}
}
