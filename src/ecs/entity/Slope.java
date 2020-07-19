package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;

/**
 * A CollidableGameObject with defined vertex data and texture to form a slope.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Slope extends CollidableGameObject
{
	/**
	 * The vertex positions of the slope.
	 */
	private static final float[] vertexPositions =
	{ -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,

			-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f,

			-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

			-0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f,

			-0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f };

	/**
	 * The texture coords of the slope.
	 */
	private static final float[] textureCoords =
	{ 0, 0.5f, 0, 1, 0.5f, 1,

			0, 0.5f, 0, 1, 0.5f, 1,

			0, 0, 0, 0.5f, 0.5f, 0.5f, 0.5f, 0,

			0, 0, 0, 0.5f, 0.5f, 0.5f, 0.5f, 0,

			0, 0, 0, 0.5f, 0.5f, 0.5f, 0.5f, 0 };

	/**
	 * The index ordering of the vertex positions and texture coords.
	 */
	private static final int[] indicies =
	{ 0, 1, 2, 3, 5, 4, 6, 7, 9, 9, 7, 8, 10, 13, 11, 11, 13, 12, 14, 15, 17, 15, 16, 17 };

	/**
	 * Mesh component of a slope.
	 */
	private static Mesh mesh = new Mesh();

	/**
	 * Material component of a slope.
	 */
	private static Material material = new Material();

	/**
	 * Constructs a slope with the given position, rotation and scale. Private 
	 * constructor as to ensure that the static create method is used to create
	 * a new slope entity.
	 * 
	 * @param pos The position of the slope in the scene.
	 * @param rot The rotation of the slope in the scene.
	 * @param scale The scale of the slope in the scene.
	 */
	private Slope(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector3f scale, float weight)
	{
		super(mesh, material, pos, rot, scale, weight);
	}

	/**
	 * Returns a slope entity with the given position, rotation and scale.
	 * 
	 * @param pos The position of the slope.
	 * @param rot The rotation of the slope.
	 * @param scale The scale of the slope.
	 * @return A slope with the given parameters.
	 */
	public static Slope create(Vector3f pos, Vector3f rot, Vector3f scale, float weight)
	{
		mesh.vertices = vertexPositions;
		mesh.indices = indicies;
		mesh.textures = textureCoords;
		material.texturePath = "src/resources/borderSquareAtlas.png";
		return new Slope(mesh, material, pos, rot, scale, weight);
	}
}
