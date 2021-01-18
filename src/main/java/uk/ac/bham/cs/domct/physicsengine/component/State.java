package uk.ac.bham.cs.domct.physicsengine.component;

import org.joml.Vector3f;

/**
 * Component that stores the state of an entity consisting 
 * of the coordinate position of the entit, it's rotation and
 * size in the scene. Also holds the previous state component of 
 * the entity.
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
	 * The scale of the entity.
	 */
	public Vector3f scale = new Vector3f(1f);
	
	/**
	 * The value of the state component from the
	 * previous time step.
	 */
	public State previous;
}
