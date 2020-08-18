package physics.collisionDetection.broadphase;

import ecs.entity.Entity;
import utils.MatrixUtils;

import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import ecs.component.Collidable;
import ecs.component.Mesh;
import ecs.component.State;
import ecs.component.Weight;

/**
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class BroadPhaseDetector
{
	/**
	 * Private constructor to ensure the class isn't initialised unnecessarily..
	 */
	private BroadPhaseDetector()
	{
		
	}
	
	/**
	 * Determines whether the axis-aligned bounding boxes of two entities are
	 * intersecting.
	 * 
	 * @param entity The first entity.
	 * @param other  The second entity.
	 * @return true if the entities bounding box are intersecting.
	 */
	public static boolean areIntersecting(Entity a, Entity b)
	{
		BoundingBox boxA = a.getComponent(Collidable.class).bBox;
		BoundingBox boxB = b.getComponent(Collidable.class).bBox;
		return boxA.isIntersecting(boxB);
	}
	
	/**
	 * Updates the bounding boxes of the given list of entities.
	 * 
	 * @param entities The list of entities to have their bounding box updated.
	 */
	public static void updateBBoxes(Entity... entities)
	{
		for (Entity e : entities)
		{
			updateBBox(e);
		}
	}
	
	/**
	 * Updates the bounding box of the given entity. Using the entity's current
	 * position the max and min x, y and z values are calculated to determine the
	 * bound of the box.
	 * 
	 * @param entity
	 */
	private static void updateBBox(Entity entity)
	{
		State state = entity.getComponent(State.class);
		float[] values = entity.getComponent(Mesh.class).vertices;
		float[] worldCoords = new float[values.length];
		Vector3f[] vertices = new Vector3f[values.length / 3];
		for (int i = 0; i < values.length / 3; i++)
		{
			vertices[i] = new Vector3f(values[3 * i], values[3 * i + 1], values[3 * i + 2]);
			Vector4f v4 = new Vector4f(vertices[i], 1);
			v4.mul(MatrixUtils.transformMatrix(state.position, state.rotation, entity.getComponent(Weight.class).scale));
			worldCoords[3 * i] = v4.x;
			worldCoords[3 * i + 1] = v4.y;
			worldCoords[3 * i + 2] = v4.z;
		}
		entity.getComponent(Collidable.class).bBox.update(worldCoords);
	}
}
