package ecs.entity;

import org.joml.Vector3f;
import ecs.component.Material;
import ecs.component.Mesh;
import ecs.component.Movable;
import ecs.system.physics.collision.response.ImpulseCalculator;

/**
 * A CollidableGameObject with a moveable component that gives the entity a
 * velocity and acceleration.
 * @author Dominic Cogan-Tucker
 *
 */
public class MovableCollidableGameObject extends CollidableGameObject
{

	/**
	 * Constructs a MoveableCollidableGameObject with a given mesh, material,
	 * position, rotation and scale.
	 * 
	 * @param mesh The mesh for the entity.
	 * @param material The material for the entity.
	 * @param pos The position of the entity.
	 * @param rot The rotation of the entity.
	 * @param scale The scale of the entity.
	 */
	public MovableCollidableGameObject(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector3f scale, float mass)
	{
		super(mesh, material, pos, rot, scale, mass);
		Movable moveableComponent = new Movable();
		float gravity = - ImpulseCalculator.GRAVITAIONAL_ACCELERATION * mass;
		moveableComponent.force = new Vector3f(0, gravity, 0);
		addComponent(moveableComponent);
	}

}
