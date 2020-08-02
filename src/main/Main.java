package main;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import ecs.component.*;
import ecs.entity.Arrow;
import ecs.entity.Camera;
import ecs.entity.CollidableCube;
import ecs.entity.Plane;
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
	}
	
	@Override
	protected void scene(Camera camera)
	{
		Plane plane = Plane.create(new Vector3f(0, -2.5f, -7.5f), new Vector3f(0, 0, 0), new Vector2f(5, 5));
		Plane backWall = Plane.create(new Vector3f(0, 0, -10.1f), new Vector3f(90, 0, 0), new Vector2f(5, 5));
		Plane sideWall = Plane.create(new Vector3f(-2.6f, 0, -7.5f), new Vector3f(-90, 0, 90), new Vector2f(5, 5));
		CollidableCube cube = CollidableCube.create(
				new Vector3f(1, 1, -6), 
				new Vector3f(45, 45, 45), 
				1, 1);
		((Moveable) cube.getComponent(Moveable.class)).momentum.set(0, 0, -15);
		Sphere sphere = Sphere.create(
				new Vector3f(0, 2, -7),
				new Vector3f(0, 0, 0), 
				0.03f, 1);
		((Moveable) sphere.getComponent(Moveable.class)).momentum.set(-5, -5, -5);
		CollidableCube cube2 = CollidableCube.create(
				new Vector3f(-1, 0.5f, -7), 
				new Vector3f(0, 0, 0), 
				1, 1);
		((Moveable) cube2.getComponent(Moveable.class)).momentum.set(-10, 0, 0);
	}

	public static void main(String[] args)
	{
		new Main().start();
	}
}
