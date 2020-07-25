package main;

import org.joml.Vector3f;

import application.Application;
import ecs.component.*;
import ecs.entity.Camera;
import ecs.entity.CollidableCube;
import ecs.entity.Slope;
import ecs.entity.Sphere;
import io.output.DisplayManager;
import io.output.Window;

public class Main extends Application
{
	
	@Override
	protected void windowSettings(Window window)
	{
		window.setDimensions(3 * DisplayManager.getScreenWidth() / 4, 3 * DisplayManager.getScreenHeight() / 4);
		window.setTitle("Sandbox");
		window.setBackgroundColour(0.5f, 0.5f, 0.5f);
	}
	
	@Override
	protected void scene(Camera camera)
	{
		((State) camera.getComponent(State.class)).position = new Vector3f (0, 2, 4);
		((State) camera.getComponent(State.class)).rotation = new Vector3f(30, 0, 0);
		Slope slope = Slope.create(
				new Vector3f(1, -3, -2), new Vector3f(), new Vector3f(6, 3, 3), Float.POSITIVE_INFINITY);
		CollidableCube cube = CollidableCube.create(
				new Vector3f(0, 0, -2), 
				new Vector3f(0, 0, 0), 
				new Vector3f(1, 1, 1), 1);
//		CollidableCube cube2 = CollidableCube.create(
//				new Vector3f(0, 2, -2f), 
//				new Vector3f(0, 0, 0), 
//				new Vector3f(1, 1, 1), 5);
//		cube2.removeComponent(Material.class);
	}

	public static void main(String[] args)
	{
		new Main().start();
	}
}
