package main;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import ecs.entity.Camera;
import ecs.entity.CollidableCube;
import ecs.entity.Plane;
import io.output.DisplayManager;
import io.output.Window;

public class SlopeDemo extends Application
{

	@Override
	protected void windowSettings(Window window)
	{
		window.setDimensions(3 * DisplayManager.getScreenWidth() / 4, 3 * DisplayManager.getScreenHeight() / 4);
		window.setTitle("Cubestack");
	}

	@Override
	protected void scene(Camera camera)
	{
		Plane slope = Plane.create(new Vector3f(0, -2.5f, -7.5f), new Vector3f(0, 0, -25), new Vector2f(5, 5));
		
		CollidableCube cube = CollidableCube.create(
				new Vector3f(0, -1f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
	}
	
	public static void main(String[] args)
	{
		new SlopeDemo().start();
	}

}
