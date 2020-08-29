package ecs.system.rendering.openGLObjects;

import static org.lwjgl.opengl.GL30.*;

/**
 * A vertex array object which contains a position vbo, texture vbo and an ibo
 * and stores the information for a complete rendered object.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Vao
{
	private int id;
	private Vbo positionVbo, textureVbo, ibo;

	/**
	 * Constructs a Vao and allocates it an id.
	 */
	public Vao()
	{
		id = glGenVertexArrays();
	}

	/**
	 * Binds the array object, setting it as active.
	 */
	public void bind()
	{
		glBindVertexArray(id);
	}

	/**
	 * Unbinds the array object, setting the default vao as active.
	 */
	public void unbind()
	{
		glBindVertexArray(0);
	}
	
	/**
	 * Stores the data into vbo which are then bound to this vao.
	 * 
	 * @param position The vertices position data to be stored.
	 * @param indicies The indices data to be stored.
	 */
	public void storeData(float[] position, int[] indicies)
	{
		bind();
		positionVbo = new Vbo(GL_ARRAY_BUFFER);
		positionVbo.bind();
		positionVbo.storeData(position);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		positionVbo.unbind();
		
		ibo = new Vbo(GL_ELEMENT_ARRAY_BUFFER);
		ibo.bind();
		ibo.storeData(indicies);
	}

	/**
	 * Stores the data into vbo which are then bound to this vao.
	 * 
	 * @param position The vertices position data to be stored.
	 * @param texture The vertices texture data to be stored.
	 * @param indicies The indices data to be stored.
	 */
	public void storeData(float[] position, float[] texture, int[] indicies)
	{
		storeData(position, indicies);

		textureVbo = new Vbo(GL_ARRAY_BUFFER);
		textureVbo.bind();
		textureVbo.storeData(texture);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		textureVbo.unbind();

		//unbind();
	}

	/**
	 * Deletes the vao and all its vbos from memory.
	 */
	public void delete()
	{
		positionVbo.delete();
		ibo.delete();
		textureVbo.delete();
		glDeleteVertexArrays(id);
	}

	/**
	 * Gets the id of this vao.
	 * 
	 * @return The id of this vao.
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * Gets the indices buffer object.
	 * 
	 * @return The ibo associated with this vao.
	 */
	public Vbo getIBO()
	{
		return ibo;
	}

	/**
	 * Gets the position vertex buffer object.
	 * 
	 * @return The position vbo associated with this vao.
	 */
	public Vbo getPositionVbo()
	{
		return positionVbo;
	}
}
