package ecs.component;

import org.joml.Vector3f;

/**
 * Component that holds an entity's momentum and velocity
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Moveable extends Component
{
	
	/**
	 * The force being applied to the entity.
	 */
	public Vector3f force = new Vector3f();
	
	/**
	 * The momentum of the entity.
	 */
	public Vector3f momentum = new Vector3f();
	
	/**
	 * The velocity of the entity.
	 */
	public Vector3f velocity = new Vector3f();
	
	/**
	 * The torque being applied to the entity.
	 */
	public Vector3f torque = new Vector3f();
	
	/**
	 * The angular momentum of the entity.
	 */
	public Vector3f angMomentum = new Vector3f();
	
	/**
	 * The angular velocity of the entity.
	 */
	public Vector3f angVelocity = new Vector3f();
	
	/**
	 * The value of the moveable component from the 
	 * previous time step.
	 */
	public Moveable previous;
}