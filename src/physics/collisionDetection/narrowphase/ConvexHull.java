package physics.collisionDetection.narrowphase;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import ecs.component.Mesh;
import ecs.component.State;
import ecs.component.Weight;
import ecs.entity.Entity;
import utils.MatrixUtils;

/**
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class ConvexHull 
{
	private Entity entity;
	private List<Vertex> vertices = new ArrayList<>();
	private List<Vertex> tempList = new ArrayList<>();
	private int[] indicies;
	
	/**
	 * 
	 * @param entity
	 */
	public ConvexHull(Entity entity)
	{
		this.entity = entity;
		processVertices();
		updateIndexData();
		setAdjacencyData();
	}
	
	/**
	 * 
	 * @param direction
	 * @return
	 */
	protected Vector3f generateSupportPoint(Vector3f direction)
	{
		State state = ((State) entity.getComponent(State.class));
		
		direction.rotateX((float) Math.toRadians(-state.rotation.x));
		direction.rotateY((float) Math.toRadians(-state.rotation.y));
		direction.rotateZ((float) Math.toRadians(-state.rotation.z));
		direction.normalize();
		
		Vertex start = vertices.get(0);
		while (true)
		{
			Vertex current = start;
			float distance = current.toVector3f().dot(direction);
			List<Integer> adjacent = current.getAdjacent();
			for (int i = 0; i < adjacent.size(); i++)
			{
				float newDistance = vertices.get(adjacent.get(i)).toVector3f().dot(direction);
				if (newDistance > distance)
				{
					current = vertices.get(adjacent.get(i));
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
						state.rotation, ((Weight) entity.getComponent(Weight.class)).scale));
		return new Vector3f(v4.x, v4.y, v4.z);
	}
	
	/**
	 * 
	 */
	private void processVertices()
	{
		float[] positions = ((Mesh) entity.getComponent(Mesh.class)).vertices;
		
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
	 * 
	 */
	private void updateIndexData()
	{
		int[] indicies = ((Mesh) entity.getComponent(Mesh.class)).indices;
		
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
	 * 
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
