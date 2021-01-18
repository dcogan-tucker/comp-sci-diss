package uk.ac.bham.cs.domct.physicsengine.entity;

import java.util.HashMap;
import java.util.Map;

import uk.ac.bham.cs.domct.physicsengine.component.Component;
import uk.ac.bham.cs.domct.physicsengine.systems.EngineSystem;

import java.util.UUID;

/**
 * An entity is an object with a unique id containing a map of all it's components.
 * @author Dominic Cogan-Tucker
 *
 */
public class Entity
{
	private UUID id;
	private Map<Class<? extends Component>, Component> components = new HashMap<>();

	/**
	 * Constructs and entity assigning a unique id to it.
	 */
	public Entity()
	{
		this.id = UUID.randomUUID();
	}
	
	/**
	 * Gets the UUID of this entity;
	 * 
	 * @return The UUID of the Entity.
	 */
	public UUID getID()
	{
		return id;
	}
	
	/**
	 * Adds the given component to this entity's component map if a component of that class
	 * type did not already exist.
	 * 
	 * @param <T> The type of class, which extends component.
	 * @param component The component to add.
	 */
	public <T extends Component> Entity addComponent(T component)
	{
		components.putIfAbsent(component.getClass(), component);

		if (EngineSystem.getEntities(component.getClass()) == null)
		{
			HashMap<Entity, Component> ec = new HashMap<>();
			ec.put(this, component);
			EngineSystem.updateComponentMap(component.getClass(), ec);
		}

		EngineSystem.getEntities(component.getClass()).putIfAbsent(this, component);
		return this;
	}

	/**
	 * Removes the component of the corresponding class from this entity's component map.
	 * 
	 * @param component The class of the component to remove.
	 */
	public Entity removeComponent(Class<? extends Component> component)
	{
		components.remove(component);
		EngineSystem.getEntities(component).remove(this);
		return this;
	}

	/**
	 * Returns true if the entity has a component of the given class type.
	 * 
	 * @param component The Class type of the component.
	 * @return True if the entity contains a component of the given Class type.
	 */
	public boolean hasComponent(Class<? extends Component> component)
	{
		return components.get(component) != null;
	}

	/**
	 * Gets the component of the corresponding class from this entity's component map.
	 * 
	 * @param component The class which extends component of the component to look for.
	 * @throws IllegalArgumentException when the entity does not have contain the
	 *                                  component of the given class.
	 * @return The component corresponding to the given class.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> component)
	{
		Component c = components.get(component);
		if (c == null)
		{
			throw new IllegalArgumentException(
					"Entity does not contain the " + component.getSimpleName() + " component.");
		}
		return (T) c;
	}
	
	/**
	 * Removes all components from this entity.
	 * 
	 */
	public Entity removeAllComponents()
	{
		if (components != null)
		{
			components.clear();
		}	
		return this;
	}
	
	/**
	 * Deletes the entity from the system.
	 */
	public void destroy()
	{
		EngineSystem.removeEntity(this);
	}
}