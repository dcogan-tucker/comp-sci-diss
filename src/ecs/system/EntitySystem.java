package ecs.system;

import java.util.Map;

import org.joml.Vector3f;

import ecs.component.Component;
import ecs.component.Moveable;
import ecs.component.State;
import ecs.entity.Camera;
import ecs.entity.Entity;

/**
 * The system that moves entities in the scene.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class EntitySystem extends EngineSystem
{
	private static float dt = 1.0f / 144;
	private static float scale = 5;
	
	/**
	 * Private constructor to ensure class isn't unnecessarily
	 * initialised.
	 */
	private EntitySystem()
	{
		
	}
	
	
	/**
	 * Updates all entities.
	 */
	public static void updateEntities()
	{
		moveEntities();
	}
	
	/**
	 * Moves all entities using their acceleration and velocity.
	 */
	private static void moveEntities()
	{
		Map<Entity, Component> entitiesMap = Component.componentMap.get(Moveable.class);
		entitiesMap.forEach((e, c) ->
		{
			if (e.hasComponent(State.class) && !(e instanceof Camera))
			{
				Moveable m = ((Moveable) e.getComponent(Moveable.class));
				State s = ((State) e.getComponent(State.class));
				Vector3f a = new Vector3f(m.acceleration);
				Vector3f v = new Vector3f(m.velocity)
						.add(a.mul(dt));
				Vector3f p = new Vector3f(s.position)
						.add(v.mul(dt).mul(scale));
				m.velocity.set(v);
				s.position.set(p);
			}
		});
	}
}
