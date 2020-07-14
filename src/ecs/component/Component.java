package ecs.component;

import java.util.HashMap;

import ecs.entity.Entity;

/**
 * The base class for all components. Stores a hash map of every component class
 * type and all entities which have that component.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public abstract class Component
{
	/**
	 * A map with the component class type as the key and the entries a map of the
	 * entities with the corresponding instance of the type of component.
	 */
	public static HashMap<Class<? extends Component>, HashMap<Entity, Component>> componentMap = new HashMap<>();
}
