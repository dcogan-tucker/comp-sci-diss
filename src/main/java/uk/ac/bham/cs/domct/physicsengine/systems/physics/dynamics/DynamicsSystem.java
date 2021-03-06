package uk.ac.bham.cs.domct.physicsengine.systems.physics.dynamics;

import java.util.Map;

import uk.ac.bham.cs.domct.physicsengine.component.Component;
import uk.ac.bham.cs.domct.physicsengine.component.Mass;
import uk.ac.bham.cs.domct.physicsengine.component.Movable;
import uk.ac.bham.cs.domct.physicsengine.component.State;
import uk.ac.bham.cs.domct.physicsengine.entity.Camera;
import uk.ac.bham.cs.domct.physicsengine.entity.Entity;
import uk.ac.bham.cs.domct.physicsengine.systems.EngineSystem;
import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.response.ImpulseCalculator;

/**
 * The system that moves entities through dynamic calculations.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class DynamicsSystem extends EngineSystem
{
	/**
	 * The time taken for a frame.
	 */
	private float dt;
	
	/**
	 * Constructs a dynamic system, with a given frame time.
	 * 
	 * @param dt The frame time for calculations.
	 */
	public DynamicsSystem(double dt)
	{
		this.dt = (float) dt;
	}
	
	/**
	 * Updates all entities.
	 */
	@Override
	public void update()
	{
		moveEntities(dt);
	}
	
	/**
	 * Moves all entities using their acceleration and velocity.
	 */
	private static void moveEntities(float dt)
	{
		Map<Entity, Component> entitiesMap = getEntities(Movable.class);
		entitiesMap.forEach((e, c) ->
		{
			if (e.hasComponent(State.class) && !(e instanceof Camera))
			{
				EulerIntegrator.integrate(dt, e);
				reapplyExternal(e, (Movable) c);
			}
		});
	}
	
	/**
	 * Reapply the external force of gravity each frame.
	 * 
	 * @param e The entity to apply the force too.
	 * @param m The entity's movable component.
	 */
	private static void reapplyExternal(Entity e, Movable m)
	{
		m.force.set(0, -ImpulseCalculator.GRAVITAIONAL_ACCELERATION * (e.getComponent(Mass.class)).mass, 0);
	}
}
