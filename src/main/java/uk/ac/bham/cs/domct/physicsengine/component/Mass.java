package uk.ac.bham.cs.domct.physicsengine.component;

/**
 * Component that holds the mass, inverse mass, inertia
 * and inverse inertia of an entity. These are the values
 * that determine the resistance of motion of an entity.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Mass extends Component
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
}
