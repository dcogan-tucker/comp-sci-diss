package uk.ac.bham.cs.domct.physicsengine.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A vertex of an object containing information about it's position, texture coordinates
 * and normal. Along with a duplicate vertex if the object has another vertex at the same
 * position with a different texture or normal value.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
class VertexData
{
	private Vector3f position;
	private Vector2f texture;
	private Vector3f normal;
	private VertexData duplicate;
	private int index;
	
	/**
	 * Constructs a vertex with the given position data.
	 * 
	 * @param position The position of the vertex as a Vector3f.
	 */
	public VertexData(Vector3f position)
	{
		this.position = position;
	}

	/**
	 * Returns whether the vertex data is complete. i.e. both the texture and normal data are
	 * not equal to null.
	 * 
	 * @return true if the texture and normal data are filled.
	 */
	public boolean isComplete()
	{
		return texture != null & normal != null;
	}
	
	/**
	 * Returns whether the vertex has the same texture and normal data as the given values.
	 * 
	 * @param texture The given texture.
	 * @param normal The given normal.
	 * @return true if this vertex has the same texture and normal as the values given.
	 */
	public boolean hasSameTextureAndNormal(Vector2f texture, Vector3f normal)
	{
		return this.texture.equals(texture.x, texture.y) && 
				this.normal.equals(normal.x, normal.y, normal.z);
	}
	
	/**
	 * Returns the duplicate vertex. i.e. The vertex with the same position data. Returns null
	 * if there is no duplicate. In the case where a vertex has more than one duplicate vertex
	 * then each duplicate vertex will store the data for the next duplicate vertex.
	 * 
	 * @return The duplicate vertex or null if there isn't one.
	 */
	public VertexData getDuplicateVertex()
	{
		return duplicate;
	}

	/**
	 * Sets the value of the duplicate vertex.
	 * 
	 * @param duplicate The vertex to set as the duplicate vertex
	 */
	public void setDuplicateVertex(VertexData duplicate)
	{
		this.duplicate = duplicate;
	}

	/**
	 * Returns the position data of this vertex.
	 * 
	 * @return The position of the vertex as a Vector3f.
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * Sets the psotion for this vertex.
	 *
	 * @param position The position to set for this vertex.
	 */
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	/**
	 * Returns the texture of this vertex.
	 * 
	 * @return The texture of this vertex.
	 */
	public Vector2f getTexture()
	{
		return texture;
	}

	/**
	 * Sets the texture of this vertex.
	 *
	 * @param texture The texture to set for this vertex.
	 */
	public void setTexture(Vector2f texture)
	{
		this.texture = texture;
	}

	/**
	 * Returns the normal to this vertex.
	 *
	 * @return The normal of the vertex.
	 */
	public Vector3f getNormal()
	{
		return normal;
	}

	/**
	 * Sets the normal of this vertex.
	 * 
	 * @param normal The normal to set for this vertex.
	 */
	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}

	/**
	 * Returns the index of this vertex.
	 * 
	 * @return The index of this vertex.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * Sets the index of this vertex.
	 * 
	 * @param index The index to set for this vertex.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	
}
