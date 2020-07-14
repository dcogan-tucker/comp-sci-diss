package ecs.component;

import openGLObjects.Vao;

/**
 * The mesh component of an entity. Holds information about the entity's
 * mesh data.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Mesh extends Component
{
	/**
	 * The local vertex positions of the mesh.
	 */
	public float[] vertices;
	
	/**
	 * The normal values of the mesh vertices.
	 */
	public float[] normals;
	
	/**
	 * The texture coordinates of the mesh.
	 */
	public float[] textures;
	
	/**
	 * The index ordering of the mesh vertices.
	 */
	public int[] indices;
	
	/**
	 * The VAO associated with this mesh.
	 */
	public Vao vao;
}
