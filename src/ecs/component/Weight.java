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
	 */
	public float inverseMass;
	
	/**
	 * The inertia of the entity.
	 */
	public float inertia;
	
	/**
	 * The inverse inertia of the entity.
	 */
	public float inverseInertia;
	
	/**
	 * The coefficient of restitution for the entity. 
	 */
	public float restitution = 1f;
	
	/**
	 * The coefficient of friction for the entity.
	 */
	public float friction = 0f;
	
	/**
	 * THe scale of the entity.
	 */
	public Vector3f scale;
}
