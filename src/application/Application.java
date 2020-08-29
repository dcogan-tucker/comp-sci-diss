package application;

import static org.lwjgl.glfw.GLFW.*;

import ecs.entity.Camera;
import ecs.system.physics.collision.CollisionSystem;
import ecs.system.physics.dynamics.EntitySystem;
import ecs.system.rendering.RenderSystem;
import ecs.system.rendering.shaders.EntityShader;
import ecs.system.view.CameraSystem;
import io.input.Keyboard;
import io.output.Window;

/**
 * Application class to be extended by the main class. Provides abstract methods to
 * initialise the window settings, the content of the scene and to update the scene
 * each frame.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public abstract class Application implements Runnable
{
	private Thread game;
	private Window window;
	private EntityShader shader;
	private Camera camera;
	private boolean paused = false;
	
	private final double dt = 1.0 / 144;
	private double currentTime;
	private double accumulator = 0;
	private double newTime;
	private double frameTime;
	
	private RenderSystem renderer;
	private CameraSystem camSystem;
	private EntitySystem entitySystem = new EntitySystem(dt);
	private CollisionSystem collisionSystem = new CollisionSystem(dt);

	@Override
	public void run()
	{
		init();
		initScene(camera);
		createScene();
		currentTime = (double) System.currentTimeMillis() / 1000;
		
		while (!window.shouldClose() && !Keyboard.isPressed(GLFW_KEY_ESCAPE))
		{
			newTime = (double) System.currentTimeMillis() / 1000;
			frameTime = newTime - currentTime;
			currentTime = newTime;
			accumulator += frameTime;
			while (accumulator >= dt)
			{
				update(dt);
				accumulator -= dt;
			}
			render();
		}
		close();
	}

	/**
	 * Starts the physics engine application. To be called in the main method of the
	 * application's main class.
	 */
	protected Application start()
	{
		game = new Thread(this, "Application");
		game.start();
		return this;
	}
	
	/**
	 * Initialises the application.
	 */
	private void init()
	{
		window = new Window(1280, 720);
		windowSettings(window);
		camera = new Camera(window);
		shader = new EntityShader();
		window.create();
		shader.create();
		renderer = new RenderSystem(camera, shader);
		camSystem = new CameraSystem(camera);
	}
	
	/**
	 * Method for the user to define the window settings for 
	 * the application.
	 * 
	 * @param window The window for the application.
	 */
	protected abstract void windowSettings(Window window);
	
	/**
	 * Method for the user to define the initial contents of the
	 * scene.
	 * 
	 * @param camera The camera to view the scene.
	 */
	protected abstract void initScene(Camera camera);
	
	/**
	 * Initialises the scene.
	 */
	private void createScene()
	{
		renderer.initialise();
	}

	/**
	 * Update the application's systems. To be called every frame.
	 */
	private void update(double dt)
	{
		if (Keyboard.isPressed(GLFW_KEY_P))
		{
			if (!paused)
			{
				paused = true;
			}
			else
			{
				paused = false;
			}
		}
		camSystem.update();
		if (!paused)
		{
			entitySystem.update();
			collisionSystem.update();
		}
		updateScene(dt);
	}
	
	/**
	 * Method for the user to define how the scene updates each
	 * frame.
	 * 
	 * @param dt The time taken for a frame.
	 */
	protected abstract void updateScene(double dt);

	
	/**
	 * Render the scene to the window. To be called every frame.
	 */
	private void render()
	{
		window.update();
		renderer.update();
		window.render();
	}
	
	/**
	 * Close the application.
	 */
	private void close()
	{
		window.destroy();
		renderer.close();
		shader.close();
	}
}
