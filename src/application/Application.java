package application;

import static org.lwjgl.glfw.GLFW.*;

import ecs.entity.Camera;
import ecs.system.CameraSystem;
import ecs.system.CollisionSystem;
import ecs.system.EntitySystem;
import ecs.system.RenderSystem;
import io.input.Keyboard;
import io.output.Window;
import shaders.EntityShader;

/**
 * Application class to be extended by the main class. Provides abstract methods to
 * initialise the window settings and the content of the scene.
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
	
	private double t = 0;
	private final double dt = 1.0 / 144;
	private double currentTime;
	private double accumulator = 0;
	private double newTime;
	private double frameTime;

	/**
	 * 
	 */
	@Override
	public void run()
	{
		init();
		scene(camera);
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
				update();
				accumulator -= dt;
				t += dt;
			}
			render();
		}
		close();
	}

	/**
	 * Starts the physics engine application. To be called in the main method of the
	 * application's main class.
	 */
	public void start()
	{
		game = new Thread(this, "Application");
		game.start();
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
	}
	
	/**
	 * Define the window settings for the application.
	 */
	protected abstract void windowSettings(Window window);
	
	/**
	 * Define the content of the scene.
	 */
	protected abstract void scene(Camera camera);
	
	/**
	 * Initialises the scene.
	 */
	private void createScene()
	{
		RenderSystem.initialise();
	}
	
	/**
	 * Update the application's systems. To be called every frame.
	 */
	private void update()
	{
		
		CameraSystem.move(camera);
		CollisionSystem.collisionDetection();
		EntitySystem.updateEntities();
	}
	
	/**
	 * Render the scene to the window. To be called every frame.
	 */
	private void render()
	{
		window.update();
		RenderSystem.renderMeshes(camera, shader);
		window.render();
	}
	
	/**
	 * Close the application.
	 */
	private void close()
	{
		window.destroy();
		RenderSystem.close();
		shader.close();
	}
}
