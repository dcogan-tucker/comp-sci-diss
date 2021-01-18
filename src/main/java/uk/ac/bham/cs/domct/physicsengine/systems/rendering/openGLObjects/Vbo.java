package uk.ac.bham.cs.domct.physicsengine.systems.rendering.openGLObjects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL15.*;

/**
 * A vertex buffer object, which is a memory buffer located in the memory of a
 * video card storing information about vertices of an object. e.g. The
 * coordinates of the vertices or the colour associated with each vertex.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Vbo
{
	protected int id;
	protected int type;

	/**
	 * Constructs a Vbo and generates it an id.
	 * 
	 * @param type The type of VBO. Can either be a GL_ARRAY_BUFFER or
	 *             GL_ELEMENT_ARRAY_BUFFER.
	 */
	protected Vbo(int type)
	{
		id = glGenBuffers();
		this.type = type;
	}

	/**
	 * Binds the Vbo to the current active Vao.
	 */
	protected void bind()
	{
		glBindBuffer(type, id);
	}

	/**
	 * Unbinds the Vbo from the current active Vao.
	 */
	protected void unbind()
	{
		glBindBuffer(type, 0);
	}

	/**
	 * Stores the data into the Vbo.
	 * 
	 * @param data The data to be stored as an array of floats.
	 */
	protected void storeData(float[] data)
	{
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data).flip();
		storeData(buffer);
	}

	/**
	 * Stores the data into the Vbo.
	 * 
	 * @param data The data to be stored as a FloatBuffer.
	 */
	protected void storeData(FloatBuffer data)
	{
		glBufferData(type, data, GL_STATIC_DRAW);
	}

	/**
	 * Stores the data into the Vbo.
	 * 
	 * @param data The data to be stored as an array of ints.
	 */
	protected void storeData(int[] data)
	{
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data).flip();
		storeData(buffer);
	}

	/**
	 * Stores the data into the Vbo.
	 * 
	 * @param data The data to be stored as IntBuffer.
	 */
	protected void storeData(IntBuffer data)
	{
		glBufferData(type, data, GL_STATIC_DRAW);
	}

	/**
	 * Deletes the Vbo from memory.
	 */
	protected void delete()
	{
		glDeleteBuffers(id);
	}

	/**
	 * Gets the id of this Vbo.
	 * 
	 * @return The id of this Vbo.
	 */
	protected int getID()
	{
		return id;
	}
}
