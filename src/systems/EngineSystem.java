package systems;

import java.util.HashMap;
import java.util.Map;

import component.Component;
import entity.Entity;

public abstract class EngineSystem
{
	/**
	 * A Map of each component type and all the entities that contain that component.
	 */
	private static Map<Class<? extends Component>, Map<Entity, Component>> componentMap = new HashMap<>();
	
	/**
	 * Static method to return a Map of all entities that contain the given component class.
	 * 
	 * @param component The class of component to base the entity retrevial off.
	 * @return Map of entity and their component matching to the given class.
	 */
	public static Map<Entity, Component> getEntities(Class<? extends Component> component)
	{
		return componentMap.get(component);
	}
	
	/**
	 * Updates the component map adding a new component and corresponding entity-component map.
	 * 
	 * @param componentType The type of component to be added.
	 * @param ec The entity-component map for that component type.
	 */
	public static void updateComponentMap(Class<? extends Component> componentType, Map<Entity, Component> ec)
	{
		componentMap.put(componentType, ec);
	}
	
	/**
	 * Removes the entity from the system.
	 * 
	 * @param e The entity to remove.
	 */
	public static void removeEntity(Entity e)
	{
		componentMap.forEach((clazz, ec) ->
			{
				ec.remove(e);
			});
	}
	
	/**
	 * Clears all entities from the system.
	 */
	public static void clear()
	{
		componentMap.clear();
	}
	
	/**
	 * Method to initialise a system, to be overridden if needed. Protected to 
	 * ensure method isn't called without being implemented.
	 */
	protected void initialise()
	{
		
	}
	
	/**
	 * Method to update a system, to be overridden if needed. Protected to 
	 * ensure method isn't called without being implemented.
	 */
	protected void update()
	{
		
	}
	
	/**
	 * Method to close a system, to be overridden if needed. Protected to 
	 * ensure method isn't called without being implemented.
	 */
	protected void close()
	{
		
	}
}
