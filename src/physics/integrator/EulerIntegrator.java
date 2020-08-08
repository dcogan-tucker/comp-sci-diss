package physics.integrator;

import org.joml.Vector3f;

import ecs.component.Moveable;
import ecs.component.State;
import ecs.component.Weight;
import ecs.entity.Entity;

public final class EulerIntegrator
{
	public static void integrate(float dt, Entity... entities)
	{
		for (Entity e : entities)
		{
			if (e.hasComponent(Moveable.class))
			{
				Moveable mov = ((Moveable) e.getComponent(Moveable.class));
				State state = ((State) e.getComponent(State.class));
				setPrevious(mov, state);
				Weight weight = ((Weight) e.getComponent(Weight.class));
				mov.momentum.add(new Vector3f(mov.force).mul(dt));
				mov.velocity = new Vector3f(mov.momentum).mul(weight.inverseMass);
				state.position.add(new Vector3f(mov.velocity).mul(dt));
				mov.angMomentum.set(new Vector3f(mov.torque).mul(dt));
				mov.angVelocity.set(mov.angMomentum).mul(weight.inverseInertia);
				state.rotation.add(new Vector3f(
						(float) Math.toDegrees(mov.angVelocity.x),
						(float) Math.toDegrees(mov.angVelocity.y),
						(float) Math.toDegrees(mov.angVelocity.z)).mul(dt));
			}
		}
	}
	
	public static void stepBack(Entity[] entities)
	{
		for (Entity e : entities)
		{
			if (e.hasComponent(Moveable.class))
			{
				Moveable mov = ((Moveable) e.getComponent(Moveable.class));
				State state = ((State) e.getComponent(State.class));
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
	
	private static void setPrevious(Moveable mov, State state)
	{
		mov.previous = new Moveable();
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
