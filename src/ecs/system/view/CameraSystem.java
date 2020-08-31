package ecs.system.view;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import ecs.component.Movable;
import ecs.component.Controllable;
import ecs.component.State;
import ecs.component.View;
import ecs.entity.Camera;
import ecs.system.EngineSystem;
import io.input.*;

/**
 * System that controls the camera, containing a static 
 * method to move a camera object.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class CameraSystem extends EngineSystem
{
	/**
	 * The mouse sensitivity.
	 */
	private float mouseSens = 0.1f;
	
	/**
	 * The Mouse mouse position for this frame and the previous.
	 */
	private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;
	
	/**
	 * The camera used in the system.
	 */
	private Camera camera;
	
	/**
	 * Constructs a camera system, taking the camera to control as the input.
	 * 
	 * @param cam The camera for the system to control.
	 */
	public CameraSystem(Camera cam)
	{
		camera = cam;
	}
	
	/**
	 * Calls the cameras move method, to check if any movement is required each frame.
	 */
	@Override
	public void update()
	{
		move();
	}
	
	/**
	 * Updates the velocity through key actions of a given camera, moving it in the
	 * scene respectively.
	 * 
	 * @param camera The camera to update.
	 */
	private void move()
	{
		State state = camera.getComponent(State.class);
		Movable mov = camera.getComponent(Movable.class);
		Controllable control = camera.getComponent(Controllable.class);

		float x = (float) Math.sin(Math.toRadians(-state.rotation.y));
		float z = (float) Math.cos(Math.toRadians(state.rotation.y));

		Vector3f direction = new Vector3f();

		if (Keyboard.isPressed(GLFW_KEY_A))
		{
			direction.add(new Vector3f(-z, 0, x));
		} else if (Keyboard.isPressed(GLFW_KEY_D))
		{
			direction.add(new Vector3f(z, 0, -x));
		}

		if (Keyboard.isPressed(GLFW_KEY_W))
		{
			direction.add(new Vector3f(-x, 0, -z));
		} else if (Keyboard.isPressed(GLFW_KEY_S))
		{
			direction.add(new Vector3f(x, 0, z));
		}

		if (Keyboard.isPressed(GLFW_KEY_SPACE))
		{
			direction.add(new Vector3f(0, 1, 0));
		} else if (Keyboard.isPressed(GLFW_KEY_LEFT_CONTROL))
		{
			direction.add(new Vector3f(0, -1, 0));
		}

		if (!direction.equals(new Vector3f()))
		{
			direction.normalize();
		}

		mov.velocity = direction.mul(control.speed);
		state.position.add(mov.velocity);

		newMouseX = Mouse.getX();
		newMouseY = Mouse.getY();

		float dx = (float) (newMouseX - oldMouseX);
		float dy = (float) (newMouseY - oldMouseY);
		
		oldMouseX = newMouseX;
		oldMouseY = newMouseY;

		if (Mouse.isPressed(GLFW_MOUSE_BUTTON_RIGHT))
		{
			state.rotation.add(new Vector3f(dy * mouseSens, dx * mouseSens, 0));
			camera.getComponent(View.class).window.disableMouse(true);
		} else
		{
			camera.getComponent(View.class).window.disableMouse(false);
		}
	}
}
