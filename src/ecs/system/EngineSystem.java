package ecs.system;

import java.util.HashMap;
import java.util.Map;

import ecs.component.Component;
import ecs.entity.Entity;

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
	
	public static void updateComponentMap(Class<? extends Component> componentType, Map<Entity, Component> ec)
	{
		componentMap.put(componentType, ec);
	}
	
	protected void initialise()
	{
		
	}
	
	protected void update()
	{
		
	}
	
	protected void close()
	{
		
	}
}
