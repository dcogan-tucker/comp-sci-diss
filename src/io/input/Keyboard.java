package io.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Manage keyboard callback creation, storage and deletion.
 * @author Dominic Cogan-Tucker
 *
 */
public class Keyboard
{
	private GLFWKeyCallback keyboard;

	private static boolean[] keysPressed = new boolean[GLFW_KEY_LAST];

	/**
	 * Initialise the keyboard callback. Protected constructor as to be only accessed
	 * by the input class.
	 */
	protected Keyboard()
	{
		keyboard = new GLFWKeyCallback()
		{
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods)
			{
				keysPressed[key] = (action != GLFW_RELEASE);
			}
		};
	}

	/**
	 * Returns whether a given key is being pressed or not.
	 * 
	 * @param key The key in question.
	 * @return true if the key is currently being pressed.
	 */
	public static boolean isPressed(int key)
	{
		return keysPressed[key];
	}

	/**
	 * Frees the resources held by the keyboard callback. Protected method as to be
	 * only accessed by the input class.
	 * 
	 */
	protected void free()
	{
		keyboard.free();
	}

	/**
	 * Gets the keyboard callback. Protected method as to be only accessed by the input
	 * class.
	 * @return The kyeboard callback.
	 */
	protected GLFWKeyCallback getKeyboardCallback()
	{
		return keyboard;
	}
}
