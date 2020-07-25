package ecs.system;

import java.util.Map;

import org.joml.Vector3f;

import ecs.component.Component;
import ecs.component.Moveable;
import ecs.component.State;
import ecs.component.Weight;
import ecs.entity.Camera;
import ecs.entity.Entity;
import physics.integrator.EulerIntegrator;
import physics.integrator.ImpulseCalculator;

/**
 * The system that moves entities in the scene.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class EntitySystem extends EngineSystem
{
	private static double delta;
	
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
	public static void updateEntities(double dt)
	{
		delta = dt;
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
				EulerIntegrator.integrate(e, (float) delta);
				reapplyExternal(e, (Moveable) c);
			}
		});
	}
	
	private static void reapplyExternal(Entity e, Moveable m)
	{
		m.force = new Vector3f(0, - ImpulseCalculator.GRAVITAIONAL_ACCELERATION * ((Weight) e.getComponent(Weight.class)).mass, 0);
	}
}
