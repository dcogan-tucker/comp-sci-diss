package uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.detection.narrowphase;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import uk.ac.bham.cs.domct.physicsengine.component.Mesh;
import uk.ac.bham.cs.domct.physicsengine.component.State;
import uk.ac.bham.cs.domct.physicsengine.entity.CollidablePlane;
import uk.ac.bham.cs.domct.physicsengine.entity.Entity;
import uk.ac.bham.cs.domct.physicsengine.entity.MovableCollidableBall;
import uk.ac.bham.cs.domct.physicsengine.utils.MatrixUtils;

/**
 * Stores the mesh data for the entity in a form to be used in collision detection.
 * The Convex Hull stores a list of vertices and the index ordering, where these
 * vertices store adjacency data for the hull.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class ConvexHull 
{
	/**
	 * The entity this hull belongs to.
	 */
	private Entity entity;
	
	/**
	 * The hulls vertices.
	 */
	private List<Vertex> vertices = new ArrayList<>();
	private List<Vertex> tempList = new ArrayList<>();
	
	/**
	 * The index ordering of the vertices.
	 */
	private int[] indicies;
	
	/**
	 * Constructs a convex hull for the given entity.
	 * 
	 * @param entity The entity to generate a convex hull for.
	 */
	public ConvexHull(Entity entity)
	{
		this.entity = entity;
		processVertices();
		updateIndexData();
		setAdjacencyData();
	}
	
	/**
	 * Generates a support point in the given direction for the hull.
	 * 
	 * @param direction The direction to generate the support point in.
	 * 
	 * @return The support point of the hull in the given direction.
	 */
	protected Vector3f generateSupportPoint(Vector3f direction)
	{
		State state = entity.getComponent(State.class);
		Vector3f rot = new Vector3f();
		rot.set(state.rotation);
		if (!(entity instanceof MovableCollidableBall))
		{
			if (!(entity instanceof CollidablePlane))
			{
				if (rot.x > 0) rot.x = -rot.x;
				if (rot.y > 0) rot.y = -rot.y;
				if (rot.z > 0) rot.z = -rot.z;
				
				if (rot.x % 45 == 0 && (rot.x / 45) % 2 != 0) rot.x = -45;
				if (rot.x % 45 == 0 && (rot.x / 45) % 2 == 0) rot.x = 0;
				else if (rot.x < -45) rot.x = 45 -(rot.x % 90);
				if (rot.y % 45 == 0 && (rot.y / 45) % 2 != 0) rot.y = -45;
				if (rot.y % 45 == 0 && (rot.y / 45) % 2 == 0) rot.x = 0;
				else if (rot.y < -45) rot.y = 45 - (rot.y % 90);
				if (rot.z % 45 == 0 && (rot.z / 45) % 2 != 0) rot.z = -45;
				if (rot.z % 45 == 0 && (rot.z / 45) % 2 == 0) rot.z = 0;
				else if (rot.z < -45) rot.z = 45 -(rot.z % 90);
				
				if (rot.x > 0) rot.x = -rot.x;
				if (rot.y > 0) rot.y = -rot.y;
				if (rot.z > 0) rot.z = -rot.z;
			}
			direction.rotateX((float) Math.toRadians(-rot.x));
			direction.rotateY((float) Math.toRadians(-rot.y));
			direction.rotateZ((float) Math.toRadians(-rot.z));
			direction.normalize();
		}
		
		Vertex start = vertices.get(0);
		while (true)
		{
			Vertex current = start;
			float distance = current.toVector3f().dot(direction);
			List<Integer> adjacent = current.getAdjacent();
			for (Integer integer : adjacent)
			{
				float newDistance = vertices.get(integer).toVector3f().dot(direction);
				if (newDistance > distance)
				{
					current = vertices.get(integer);
					distance = newDistance;
				}
			}
			if (current.equals(start))
			{
				break;
			}
			start = current;
		}
	
		Vector4f v4 = new Vector4f(start.toVector3f(), 1)
				.mul(MatrixUtils.transformMatrix(state.position, 
						rot, state.scale));
		return new Vector3f(v4.x, v4.y, v4.z);
	}
	
	/**
	 * Processes the vertices of the entity to create a list for the convex hull
	 * to use.
	 */
	private void processVertices()
	{
		float[] positions = entity.getComponent(Mesh.class).vertices;
		
		for (int i = 0; i < positions.length / 3; i++)
		{
			Vertex newVert = new Vertex(positions[3 * i], 
					positions[3 * i + 1], positions[3 * i + 2]);
			tempList.add(newVert);
			boolean alreadyContains = false;
			for (Vertex existingVert : vertices)
			{
				if (newVert.equals(existingVert))
				{
					alreadyContains = true;
					break;
				}
			}
			if (!alreadyContains)
			{
				vertices.add(newVert);
			}
		}
	}
	
	/**
	 * Updates the index data for the vertices in the convex hull.
	 */
	private void updateIndexData()
	{
		int[] indicies = entity.getComponent(Mesh.class).indices;
		
		this.indicies = new int[indicies.length];
		for (int i = 0; i < indicies.length; i++)
		{
			int index = indicies[i];
			if (index < vertices.size())
			{
				this.indicies[i] = index;
			}
			else
			{
				Vertex duplicate = tempList.get(index);
				for (int j = 0; j < vertices.size(); j++)
				{
					if (duplicate.equals(vertices.get(j)))
					{
						index = j;
						break;
					}
				}
				this.indicies[i] = index;
			}
		}
		
		tempList.clear();
	}
	
	/**
	 * Sets the adjaceny data for all vetices in the convex hull.
	 */
	private void setAdjacencyData()
	{
		for (int i = 0; i < this.indicies.length / 3; i++)
		{
			int previous = this.indicies[3 * i];
			int current = this.indicies[3 * i + 1];
			int next = this.indicies[3 * i + 2];
			
			vertices.get(previous)
				.addAdjacentIndex(current)
				.addAdjacentIndex(next);
			
			vertices.get(current)
				.addAdjacentIndex(previous)
				.addAdjacentIndex(next);
			
			vertices.get(next)
				.addAdjacentIndex(previous)
				.addAdjacentIndex(current);
		}
	}
}
