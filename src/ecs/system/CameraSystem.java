package ecs.system;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import ecs.component.Moveable;
import ecs.component.State;
import ecs.component.View;
import ecs.entity.Camera;
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
	private static float moveSpeed = 0.05f;
	private static float mouseSens = 0.1f;
	private static double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

	/**
	 * Updates the velocity through key actions of a given camera, moving it in the
	 * scene respectively.
	 * 
	 * @param camera The camera to update.
	 */
	public static void move(Camera camera)
	{
		State state = ((State) camera.getComponent(State.class));
		Moveable mov = ((Moveable) camera.getComponent(Moveable.class));

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

		mov.velocity = direction.mul(moveSpeed);
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
			((View) camera.getComponent(View.class)).window.disableMouse(true);
		} else
		{
			((View) camera.getComponent(View.class)).window.disableMouse(false);
		}


	}
}
