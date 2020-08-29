package ecs.system.physics.dynamics;

import java.util.Map;

import ecs.component.Component;
import ecs.component.Moveable;
import ecs.component.State;
import ecs.component.Weight;
import ecs.entity.Camera;
import ecs.entity.Entity;
import ecs.system.EngineSystem;
import ecs.system.physics.collision.response.ImpulseCalculator;

/**
 * The system that moves entities in the scene.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class EntitySystem extends EngineSystem
{
	private float dt;
	
	public EntitySystem(double dt)
	{
		this.dt = (float) dt;
	}
	
	@Override
	public void initialise()
	{
		
	}
	
	/**
	 * Updates all entities.
	 */
	@Override
	public void update()
	{
		moveEntities(dt);
	}
	
	@Override
	public void close()
	{
		
	}
	
	/**
	 * Moves all entities using their acceleration and velocity.
	 */
	private static void moveEntities(float dt)
	{
		Map<Entity, Component> entitiesMap = getEntities(Moveable.class);
		entitiesMap.forEach((e, c) ->
		{
			if (e.hasComponent(State.class) && !(e instanceof Camera))
			{
				EulerIntegrator.integrate(dt, e);
				reapplyExternal(e, (Moveable) c);
			}
		});
	}
	
	/**
	 * 
	 * @param e
	 * @param m
	 */
	private static void reapplyExternal(Entity e, Moveable m)
	{
		m.force.set(0, -ImpulseCalculator.GRAVITAIONAL_ACCELERATION * ((Weight) e.getComponent(Weight.class)).mass, 0);
	}
}
