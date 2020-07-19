package physics.integrator;

import org.joml.Vector3f;

import ecs.component.Moveable;
import ecs.component.State;
import ecs.component.Weight;
import ecs.entity.Entity;

public final class EulerIntegrator
{
	
	private static float SCALE = 10;
	
	public static void integrate(Entity e, float dt)
	{
		Moveable mov = ((Moveable) e.getComponent(Moveable.class));
		State state = ((State) e.getComponent(State.class));
		mov.momentum = new Vector3f(mov.force).mul(dt);
		mov.velocity = new Vector3f(mov.momentum)
				.mul(((Weight) e.getComponent(Weight.class)).inverseMass);
		state.position.add(new Vector3f(mov.velocity).mul(dt).mul(SCALE));
	}
}
