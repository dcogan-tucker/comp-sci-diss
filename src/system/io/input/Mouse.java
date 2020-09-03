package system.io.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Manages mouse position, button press and scroll callback creation, storage
 * and deletion.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Mouse
{
	private GLFWCursorPosCallback mousePos;
	private GLFWMouseButtonCallback mouseButton;
	private GLFWScrollCallback mouseScroll;

	private static boolean[] buttonsPressed = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private static double mouseX, mouseY;
	private static double scrollX, scrollY;

	/**
	 * Initialises mouse position, button press, and scroll callbacks. Protected
	 * method as to be only accessed by the input class.
	 */
	protected Mouse()
	{
		mousePos = new GLFWCursorPosCallback()
		{
			@Override
			public void invoke(long window, double xpos, double ypos)
			{
				mouseX = xpos;
				mouseY = ypos;
			}
		};

		mouseButton = new GLFWMouseButtonCallback()
		{
			@Override
			public void invoke(long window, int button, int action, int mods)
			{
				buttonsPressed[button] = (action != GLFW_RELEASE);
			}
		};

		mouseScroll = new GLFWScrollCallback()
		{
			@Override
			public void invoke(long window, double xoffset, double yoffset)
			{
				scrollX += xoffset;
				scrollY += yoffset;
			}
		};
	}

	/**
	 * Returns whether a given mouse button is being pressed or not.
	 * 
	 * @param button The button in question.
	 * @return true if the button is currently being pressed.
	 */
	public static boolean isPressed(int button)
	{
		return buttonsPressed[button];
	}

	/**
	 * Frees the resources held by the mouse callbacks. Protected method as to be
	 * only accessed by the input class.
	 */
	protected void free()
	{
		mousePos.free();
		mouseButton.free();
		mouseScroll.free();
	}

	/**
	 * Gets the mouse position callback. Protected method as to be only accessed by
	 * the input class.
	 * 
	 * @return The mouse position callback.
	 */
	protected GLFWCursorPosCallback getMousePosCallback()
	{
		return mousePos;
	}

	/**
	 * Gets the mouse button callback. Protected method as to be only accessed by
	 * the input class.
	 * 
	 * @return The mouse button callback.
	 */
	protected GLFWMouseButtonCallback getMouseButtonCallback()
	{
		return mouseButton;
	}

	/**
	 * Gets the mouse scroll callback. Protected method as to be only accessed by
	 * the input class.
	 * 
	 * @return The mouse scroll callback.
	 */
	protected GLFWScrollCallback getMouseScrollCallback()
	{
		return mouseScroll;
	}

	/**
	 * Gets the x position of the mouse.
	 * 
	 * @return The current x position of the mouse.
	 */
	public static double getX()
	{
		return mouseX;
	}

	/**
	 * Gets the y position of the mouse.
	 * 
	 * @return The current y position of the mouse.
	 */
	public static double getY()
	{
		return mouseY;
	}

	/**
	 * Gets the total x scroll distance of the mouse.
	 * 
	 * @return The total x scroll distance.
	 */
	public static double getScrollX()
	{
		return scrollX;
	}

	/**
	 * Gets the total y scroll distance of the mouse.
	 * 
	 * @return The total y scroll distance.
	 */
	public static double getScrollY()
	{
		return scrollY;
	}
}
