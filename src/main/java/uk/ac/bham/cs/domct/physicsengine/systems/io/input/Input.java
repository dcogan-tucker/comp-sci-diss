package uk.ac.bham.cs.domct.physicsengine.systems.io.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Manages creation, storage and deletion of keyboard and mouse callbacks for a
 * GLFW window.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Input
{
	private Keyboard keyboard;
	private Mouse mouse;

	/**
	 * Initialises the keyboard and mouse callbacks.
	 */
	public Input()
	{
		keyboard = new Keyboard();
		mouse = new Mouse();
	}

	/**
	 * Frees the resources held by the keyboard and mouse callbacks.
	 */
	public void free()
	{
		keyboard.free();
		mouse.free();
	}

	/**
	 * Gets the keyboard callback.
	 * 
	 * @return The keyboard callback.
	 */
	public GLFWKeyCallback getKeyboardCallback()
	{
		return keyboard.getKeyboardCallback();
	}

	/**
	 * Gets the mouse position callback.
	 * 
	 * @return The mouse position callback.
	 */
	public GLFWCursorPosCallback getMousePosCallback()
	{
		return mouse.getMousePosCallback();
	}

	/**
	 * Gets the mouse button callback.
	 * 
	 * @return The mouse button callback.
	 */
	public GLFWMouseButtonCallback getMouseButtonCallback()
	{
		return mouse.getMouseButtonCallback();
	}

	/**
	 * Gets the mouse scroll callback.
	 * 
	 * @return The mouse scroll callback.
	 */
	public GLFWScrollCallback getMouseScrollCallback()
	{
		return mouse.getMouseScrollCallback();
	}
}
