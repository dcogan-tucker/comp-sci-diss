package uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.detection.broadphase;

import org.joml.Vector3f;
import org.joml.Vector4f;

import uk.ac.bham.cs.domct.physicsengine.utils.MatrixUtils;
import uk.ac.bham.cs.domct.physicsengine.component.Collidable;
import uk.ac.bham.cs.domct.physicsengine.component.Mesh;
import uk.ac.bham.cs.domct.physicsengine.component.State;
import uk.ac.bham.cs.domct.physicsengine.entity.Entity;

/**
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class BroadPhaseDetector
{	
	/**
	 * Determines whether the axis-aligned bounding boxes of two entities are
	 * intersecting.
	 * 
	 * @param a The first entity.
	 * @param b  The second entity.
	 * @return true if the entities bounding box are intersecting.
	 */
	public static boolean areIntersecting(Entity a, Entity b)
	{
		updateBBoxes(a, b);
		BoundingBox boxA = a.getComponent(Collidable.class).bBox;
		BoundingBox boxB = b.getComponent(Collidable.class).bBox;
		return boxA.isIntersecting(boxB);
	}
	
	/**
	 * Updates the bounding boxes of the given list of entities.
	 * 
	 * @param entities The list of entities to have their bounding box updated.
	 */
	private static void updateBBoxes(Entity... entities)
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
	 * @param entity The entity whose BBox should be updated.
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
			v4.mul(MatrixUtils.transformMatrix(state.position, state.rotation, state.scale));
			worldCoords[3 * i] = v4.x;
			worldCoords[3 * i + 1] = v4.y;
			worldCoords[3 * i + 2] = v4.z;
		}
		entity.getComponent(Collidable.class).bBox.update(worldCoords);
	}
}
