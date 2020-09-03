package entity;

import org.joml.Vector3f;

import component.Mass;
import component.Material;
import component.Mesh;
import component.State;

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
	public GameObject(Mesh mesh, Material material, Vector3f pos, Vector3f rot, Vector3f scale, float mass)
	{
		super();		
		addComponent(mesh);
		addComponent(material);

		State state = new State();
		state.position = pos;
		state.rotation = rot;
		state.scale = scale;
		addComponent(state);
		
		Mass m = new Mass();
		m.mass = mass;
		m.inverseMass = 1f / mass;
		addComponent(m);
	}
	
	/**
	 * A to string method that returns the simple class name and state of the game object.
	 */
	@Override
	public String toString()
	{
		State state = this.getComponent(State.class);
		return this.getClass().getSimpleName() + " (position: " + state.position + ", rotation: " + state.rotation + ").";
	}
}
