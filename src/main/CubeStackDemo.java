package main;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import ecs.component.Moveable;
import ecs.entity.Camera;
import ecs.entity.CollidableCube;
import ecs.entity.Plane;
import io.output.DisplayManager;
import io.output.Window;

public class CubeStackDemo extends Application
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
		Plane plane = Plane.create(new Vector3f(0, -2.5f, -7.5f), new Vector3f(0, 0, 0), new Vector2f(5, 5));
		Plane backWall = Plane.create(new Vector3f(0, 0, -10.1f), new Vector3f(90, 0, 0), new Vector2f(5, 5));
		Plane sideWall = Plane.create(new Vector3f(-2.6f, 0, -7.5f), new Vector3f(-90, 0, 90), new Vector2f(5, 5));
		
		CollidableCube cube = CollidableCube.create(
				new Vector3f(0, -1f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		CollidableCube cube1 = CollidableCube.create(
				new Vector3f(0, 1f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		CollidableCube cube2 = CollidableCube.create(
				new Vector3f(0, 3f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		CollidableCube cube3 = CollidableCube.create(
				new Vector3f(0, 5f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		CollidableCube cube4 = CollidableCube.create(
				new Vector3f(0, 7f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
	}

	public static void main(String[] args)
	{
		new CubeStackDemo().start();
	}
}
