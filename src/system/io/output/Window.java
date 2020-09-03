package system.io.output;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import system.io.input.Input;
import utils.MatrixUtils;

import java.awt.Dimension;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * Window stores information pertaining to the creation of a GLFW window. The
 * create() method must be called once to initialise the window, while the
 * update() method should be called every frame. When the window is close the
 * destroy() method should be called.
 * 
 * @author Dominic Cogan-Tucker
 *
 */

public class Window
{
	private long window = -1;
	private int width;
	private int height;
	private Matrix4f projection;
	private String title;
	private Vector3f background = new Vector3f();
	private Input input;
	private GLFWWindowSizeCallback sizeCallback;
	private boolean isResized = false;
	
	private float fov = 70;
	private float nearPlane = 0.1f;
	private float farPlane = 1000;

	/**
	 * Constructs a Window object with the given width, height and title as an int,
	 * int and String respectively which can then be used to create a window with
	 * the create method.
	 * 
	 * @param width  The desired width of the window in pixels as an int.
	 * @param height The desired height of the window in pixels as an int.
	 * @param title  The desired title of the window as a String.
	 */
	public Window(int width, int height)
	{
		this.title = "";
		this.width = width;
		this.height = height;
		this.projection = MatrixUtils.projectionMatrix((float) width / (float) height, fov, nearPlane, farPlane);
	}

	/**
	 * Initialises GLFW and then creates and displays a GLFW window.
	 * 
	 * @throws IllegalStateException If GLFW doesn't initialise.
	 * @throws RuntimeException      If the window is failed to be created.
	 */
	public void create()
	{
		if (!glfwInit())
		{
			throw new IllegalStateException("GLFW wasn't initialised");
		}
		input = new Input();
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL)
		{
			throw new RuntimeException("Failed to create the GLFW window");
		}
		try (MemoryStack stack = stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(window, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		setCallbacks();
		glfwShowWindow(window);
		glfwSwapInterval(1);
	}

	/**
	 * Sets the keyboard, mouse and resize callbacks for this window.
	 */
	private void setCallbacks()
	{
		sizeCallback = new GLFWWindowSizeCallback()
		{
			@Override
			public void invoke(long window, int w, int h)
			{
				width = w;
				height = h;
				isResized = true;
			}
		};
		glfwSetKeyCallback(window, input.getKeyboardCallback());
		glfwSetCursorPosCallback(window, input.getMousePosCallback());
		glfwSetMouseButtonCallback(window, input.getMouseButtonCallback());
		glfwSetScrollCallback(window, input.getMouseScrollCallback());
		glfwSetWindowSizeCallback(window, sizeCallback);
	}

	/**
	 * Updates the window. Adjusts the viewport if the window has been resized.
	 * Clears the colour and depth buffers. And the processes all events. This
	 * method should be called every frame.
	 */
	public void update()
	{
		if (isResized)
		{
			glViewport(0, 0, width, height);
			isResized = false;
		}
		glClearColor(background.x, background.y, background.z, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwPollEvents();
	}

	/**
	 * Swaps the front and back buffers for the window displaying the draw call for
	 * the frame. Make sure to call after the whole scene has been rendered.
	 */
	public void render()
	{
		glfwSwapBuffers(window);
	}

	/**
	 * Destroys the GLFW window and terminates GLFW.
	 */
	public void destroy()
	{
		input.free();
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	/**
	 * Sets the title of the window.
	 * 
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
		if (window != -1)
		{
			glfwSetWindowTitle(window, title);
		}
	}
	
	/**
	 * Sets the window dimensions in pixels.
	 * 
	 * @param dimension The dimension of the window as a java aw
	 * dimension object.
	 */
	public void setDimensions(Dimension dimension)
	{
		setDimensions(dimension.width, dimension.height);
	}
	
	/**
	 * Sets the window dimensions in pixels.
	 * 
	 * @param width The width of the window in pixels.
	 * @param height The height of the window in pixels.
	 */
	public void setDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
		if (window != -1)
		{
			glfwSetWindowSize(window, width, height);	
		}
	}

	/**
	 * Returns whether or not a window should be closed.
	 * 
	 * @return true if the window should close, false if it shouldn't.
	 */
	public boolean shouldClose()
	{
		return glfwWindowShouldClose(window);
	}

	/**
	 * Sets whether or not a window should be closed.
	 * 
	 * @param shouldClose Whether or not the window should be closed.
	 */
	public void setShouldClose(boolean shouldClose)
	{
		glfwSetWindowShouldClose(window, shouldClose);
	}

	/**
	 * Set the background RGB colour of the window. With each channel being set a
	 * float value between 0 and 1.
	 * 
	 * @param r The value to set the R channel.
	 * @param g The value to set the G channel.
	 * @param b The value to set the B channel.
	 */
	public void setBackgroundColour(float r, float g, float b)
	{
		background.set(r, g, b);
	}
	
	/**
	 * Set the state of the mouse to either disabled or visible.
	 * 
	 * @param disable Whether to disable the mouse or not.
	 */
	public void disableMouse(boolean disable)
	{
		glfwSetInputMode(window, GLFW_CURSOR, disable ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
	}

	/**
	 * Gets the projection matrix for this window.
	 * 
	 * @return The projection matrix for this window.
	 */
	public Matrix4f getProjectionMatrix()
	{
		return projection;
	}

	/**
	 * Gets the GLFW window ID.
	 * 
	 * @return GLFW window ID as a long.
	 */
	public long getWindow()
	{
		return window;
	}

	/**
	 * Gets the width of the window in pixels.
	 * 
	 * @return Width of the window in pixels.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets the height of the window in pixels.
	 * 
	 * @return Height of the window in pixels.
	 */
	public int getHeight()
	{
		return height;
	}

}
