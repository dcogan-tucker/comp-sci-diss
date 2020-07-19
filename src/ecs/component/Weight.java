package ecs.component;

import org.joml.Vector3f;

/**
 * Component that holds the mass of an entity.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Weight extends Component
{
	/**
	 * The mass of the entity.
	 */
	public float mass;
	
	/**
	 * The inverse mass of the entity.
	 * 
	 */
	public float inverseMass;
	
	/**
	 * THe scale of the entity.
	 */
	public Vector3f scale;
}
