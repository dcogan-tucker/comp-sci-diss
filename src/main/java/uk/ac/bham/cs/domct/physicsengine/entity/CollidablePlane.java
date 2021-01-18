package uk.ac.bham.cs.domct.physicsengine.entity;

import org.joml.Vector2f;
import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.component.Material;
import uk.ac.bham.cs.domct.physicsengine.component.Mesh;
import uk.ac.bham.cs.domct.physicsengine.utils.FileUtils;

/**
 * 
 * A two dimensional collidable plane with infinite mass. 
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class CollidablePlane extends CollidableGameObject
{
	/**
	 * The path to the texture png file.
	 */
	private static String texturePath = "resources/textures/wall.png";

	/**
	 * Mesh component of a cube.
	 */
	private static Mesh mesh = FileUtils.loadOBJFile("cube.obj");

	/**
	 * Material component of the plane.
	 */
	private static Material material = new Material();

	/**
	 * Constructs a plane at a given position within the scene that is collidable with a given
	 * rotation, scale and material.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The rotation of the plane.
	 * @param scale The scale of the plane.
	 */
	private CollidablePlane(Vector3f pos, Vector3f rot, Vector2f scale)
	{
		super(mesh, material, pos, rot, new Vector3f(scale.x, 0f, scale.y), Float.POSITIVE_INFINITY);
	}
	
	/**
	 * Constructs a Plane with the given position, rotation and scale.
	 * constructor as to ensure that the static create method is used to create a new 
	 * Plane entity.
	 * 
	 * @param pos The position in the scene.
	 * @param rot The plane's rotation.
	 * @param scale The scale of the plane.
	 * @return A Plane Object with the given parameters.
	 */
	public static CollidablePlane create(Vector3f pos, Vector3f rot, Vector2f scale)
	{
		material.texturePath = texturePath;
		return new CollidablePlane(pos, rot, scale);
	}

}
