package ecs.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import ecs.component.Component;
import ecs.system.EngineSystem;

/**
 * An entity is an object with a unique id containing a map of all it's components.
 * @author Dominic Cogan-Tucker
 *
 */
public class Entity
{
	private UUID id;
	private HashMap<Class<? extends Component>, Component> components = new HashMap<>();

	/**
	 * Constructs and entity assigning a unique id to it.
	 */
	public Entity()
	{
		this.id = UUID.randomUUID();
	}

	/**
	 * Adds the given component to this entity's component map if a component of that class
	 * type did not already exist.
	 * 
	 * @param <T> The type of class, which extends component.
	 * @param component The component to add.
	 */
	public <T extends Component> void addComponent(T component)
	{
		if (components.get(component.getClass()) == null)
		{
			components.put(component.getClass(), component);
		}
		
		if (EngineSystem.getEntities(component.getClass()) == null)
		{
			HashMap<Entity, Component> ec = new HashMap<>();
			ec.put(this, component);
			EngineSystem.updateComponentMap(component.getClass(), ec);
		}
		else if (EngineSystem.getEntities(component.getClass()).get(this) == null)
		{
			EngineSystem.getEntities(component.getClass()).put(this, component);
		}
	}

	/**
	 * Removes the component of the corresponding class from this entity's component map.
	 * 
	 * @param component The class of the component to remove.
	 */
	public void removeComponent(Class<? extends Component> component)
	{
		components.remove(component);
		EngineSystem.getEntities(component).remove(this);
	}

	/**
	 * Returns true if the entity has a component of the given class type.
	 * 
	 * @param component The Class type of the component.
	 * @return True if the entity contains a component of the given Class type.
	 */
	public boolean hasComponent(Class<? extends Component> component)
	{
		return components.get(component) != null ? true : false;
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
	public void removeAllComponents()
	{
		if (components != null)
		{
			for (Iterator<Entry<Class<? extends Component>, Component>> iter = components.entrySet().iterator(); iter.hasNext();)
			{
				iter.remove();
			}
		}	
	}

	/**
	 * Gets the UUID of this entity;
	 * 
	 * @return
	 */
	public UUID getID()
	{
		return id;
	}
}