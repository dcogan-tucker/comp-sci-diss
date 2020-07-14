package ecs.component;

import org.joml.Vector3f;

/**
 * Component that holds an entity's velocity and acceleration.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Moveable extends Component
{
	/**
	 * The velocity of the entity.
	 */
	public Vector3f velocity;
	
	/**
	 * The acceleration of the entity.
	 */
	public Vector3f acceleration;
}