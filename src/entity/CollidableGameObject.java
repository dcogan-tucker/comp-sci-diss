package entity;

import org.joml.Vector3f;
import org.joml.Vector4f;

import component.Collidable;
import component.Material;
import component.Mesh;
import systems.physics.collision.detection.broadphase.BoundingBox;
import systems.physics.collision.detection.narrowphase.ConvexHull;
import utils.MatrixUtils;

/**
 * A GameObject with the addition of a collidable component which stores data
 * regarding the objects bounding box.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class CollidableGameObject extends GameObject
{
	/**
	 * Constructs a CollidableGameObject from a textured mesh and it's position, rotation and
	 * scale in the scene, generating a bounding box from it's mesh.
	 * 
	 * @param mesh The textured mesh of the entity. 
	 * @param pos The position of the entity in the scene.
	 * @param rot The rotation of the entity in the scene.
	 * @param scale The scale of the entity in the scene.
	 */
	public CollidableGameObject(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector3f scale, float weight)
	{
		super(mesh, material, pos, rot, scale, weight);
		Collidable collidable = new Collidable();
		addComponent(collidable);
		float[] values = ((Mesh) getComponent(Mesh.class)).vertices;
		float[] worldCoords = new float[values.length];
		Vector3f[] vertices = new Vector3f[values.length / 3];
		for (int i = 0; i < values.length / 3; i++)
		{
			vertices[i] = new Vector3f(values[3 * i], values[3 * i + 1], values[3 * i + 2]);
			Vector4f v4 = new Vector4f(vertices[i], 1);
			v4.mul(MatrixUtils.transformMatrix(pos, rot, scale));
			worldCoords[3 * i] = v4.x;
			worldCoords[3 * i + 1] = v4.y;
			worldCoords[3 * i + 2] = v4.z;
		}
		collidable.bBox = new BoundingBox(worldCoords);
		collidable.hull = new ConvexHull(this);
	}
}
