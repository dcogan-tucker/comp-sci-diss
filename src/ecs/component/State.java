package ecs.component;

import org.joml.Vector3f;

/**
 * Component that stores the state of an entity consisting 
 * of the coordinate position of the entity and it's rotation 
 * in the scene.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class State extends Component
{
	/**
	 * The coordinate position of the entity.
	 */
	public Vector3f position = new Vector3f();

	/**
	 * The rotation of the entity.
	 */
	public Vector3f rotation = new Vector3f();
	
	/**
	 * The value of the state component from the
	 * previous time step.
	 */
	public State previous;
}
