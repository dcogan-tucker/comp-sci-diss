package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;
import ecs.component.Scale;
import ecs.component.State;

/**
 * An an entity with a model component giving it a mesh and texture. Also has a
 * position, rotation and scale in the scene.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class GameObject extends Entity
{
	/**
	 * Constructs a GameObject from a textured mesh and it's position, rotation and
	 * scale in the scene.
	 * 
	 * @param mesh The textured mesh of the entity. 
	 * @param pos The position of the entity in the scene.
	 * @param rot The rotation of the entity in the scene.
	 * @param scale The scale of the entity in the scene.
	 */
	public GameObject(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector3f scale)
	{
		super();		
		addComponent(mesh);
		addComponent(material);

		State state = new State();
		state.position = pos;
		state.rotation = rot;
		addComponent(state);

		Scale scaleComp = new Scale();
		scaleComp.scale = scale;
		addComponent(scaleComp);
	}
}
