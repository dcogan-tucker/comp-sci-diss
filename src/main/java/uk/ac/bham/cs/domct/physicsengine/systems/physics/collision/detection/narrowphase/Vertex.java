package uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.detection.narrowphase;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

/**
 * Vertex class, containing the x, y, and z coordiantes of the vertex
 * and a list of any adjacent vertices.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
class Vertex
{
	/**
	 * The x, y and z coordinates of the vertex as floats.
	 */
	protected float x, y, z;
	private List<Integer> adjacent = new ArrayList<>();
	
	/**
	 * Constructs a a Vertex with the given x, y and z
	 * coordinates.
	 * 
	 * @param x The x coordinate of the vertex.
	 * @param y The y coordinate of the vertex.
	 * @param z The z coordinate of the vertex.
	 */
	protected Vertex(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Add the index value of an adjacent vertex to this
	 * vertex's adjacency list.
	 * 
	 * @param index The index of the adjacent vertex.
	 * @return this vertex.
	 */
	protected Vertex addAdjacentIndex(int index)
	{
		if (!adjacent.contains(index))
		{
			adjacent.add(index);
		}
		return this;
	}
	
	/**
	 * Returns a list of the index values of the adjacent
	 * vertices.
	 * 
	 * @return A list of indices of adjacent vertices.
	 */
	protected List<Integer> getAdjacent()
	{
		return adjacent;
	}
	
	/**
	 * Returns this vertex in the form of a JOML Vector3f.
	 * 
	 * @return this vertex as a Vector3f.
	 */
	protected Vector3f toVector3f()
	{
		return new Vector3f(x, y, z);
	}
	
	/**
	 * Returns whether or not this vertex is equal to another given
	 * vertex. Two Vertex objects are equal if the x, y and z coordinates
	 * of both are equal.
	 * 
	 * @param other The vertex to compare to.
	 * @return true if this and the other vertex are equal.
	 */
	protected boolean equals(Vertex other)
	{
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x) &&
				Float.floatToIntBits(y) == Float.floatToIntBits(other.y) &&
				Float.floatToIntBits(z) == Float.floatToIntBits(other.z);
	}
	
	/**
	 * Returns the vertex in string form. i.e "(x, y, z)".
	 */
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
