package ecs.system.physics.dynamics;

import org.joml.Vector3f;

import ecs.component.Movable;
import ecs.component.State;
import ecs.component.Mass;
import ecs.entity.Entity;
import utils.Vector3fUtils;

/**
 * Class to perform implicit Euler integration to calculate entity movement each frame.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class EulerIntegrator
{
	/**
	 * Integrates the force applied to each entity to calculate new position and rotation each frame.
	 * 
	 * @param dt The time taken for a frame.
	 * @param entities The entities to integrate force for.
	 */
	public static void integrate(float dt, Entity... entities)
	{
		for (Entity e : entities)
		{
			if (e.hasComponent(Movable.class))
			{
				Movable mov = e.getComponent(Movable.class);
				State state = e.getComponent(State.class);
				setPrevious(mov, state);
				Mass mass = e.getComponent(Mass.class);
				mov.momentum.add(new Vector3f(mov.force).mul(dt));
				mov.velocity = new Vector3f(mov.momentum).mul(mass.inverseMass);
				state.position.add(new Vector3f(mov.velocity).mul(dt));
				mov.angMomentum.set(new Vector3f(mov.torque).mul(dt));
				mov.angVelocity.set(mov.angMomentum).mul(mass.inverseInertia);
				state.rotation.add(Vector3fUtils.toDegrees(new Vector3f(mov.angVelocity).mul(dt)));
			}
		}
	}
	
	/**
	 * Step backs the movable and state values of the given entities to the previous frame.
	 * 
	 * @param entities The entities to step back.
	 */
	public static void stepBack(Entity... entities)
	{
		for (Entity e : entities)
		{
			if (e.hasComponent(Movable.class))
			{
				Movable mov = e.getComponent(Movable.class);
				State state = e.getComponent(State.class);
				mov.force.set(mov.previous.force);
				mov.momentum.set(mov.previous.momentum);
				mov.velocity.set(mov.previous.velocity);
				mov.torque.set(mov.previous.torque.negate());
				mov.angMomentum.set(mov.previous.angMomentum);
				mov.angVelocity.set(mov.previous.angVelocity);
				state.position.set(state.previous.position);
				state.rotation.set(state.previous.rotation);
			}
		}
	}
	
	/**
	 * Stores the current frame values at the end of the frame to be
	 * accessed if a step back is needed.
	 * 
	 * @param mov The current movable component.
	 * @param state The current state component.
	 */
	private static void setPrevious(Movable mov, State state)
	{
		mov.previous = new Movable();
		mov.previous.force.set(mov.force);
		mov.previous.momentum.set(mov.momentum);
		mov.previous.velocity.set(mov.velocity);
		mov.previous.torque.set(mov.torque);
		mov.previous.angMomentum.set(mov.angMomentum);
		mov.previous.angVelocity.set(mov.angVelocity);
		state.previous = new State();
		state.previous.position.set(state.position);
		state.previous.rotation.set(state.rotation);
	}
}
