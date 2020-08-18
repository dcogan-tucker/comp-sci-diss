package main;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import ecs.component.*;
import ecs.entity.Arrow;
import ecs.entity.Camera;
import ecs.entity.CollidableCube;
import ecs.entity.Plane;
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
				new Vector3f(1, 2, -6), 
				new Vector3f(-25, 0, 0),
				0.8f, 1);
		cube.getComponent(Moveable.class).momentum.set(0, 0, -5);
		Sphere sphere = Sphere.create(
				new Vector3f(0, 2, -7.5f),
				new Vector3f(0, 0, 0), 
				0.02f, 1);
		sphere.getComponent(Moveable.class).momentum.set(-5, 0, 0);
		CollidableCube cube2 = CollidableCube.create(
				new Vector3f(-0.5f, 0.5f, -6f), 
				new Vector3f(0, 0, 0), 
				0.8f, 1);
		Sphere sphere2 = Sphere.create(
				new Vector3f(0.5f, 0.5f, -6f),
				new Vector3f(0, 0, 0), 
				0.02f, 1);
		plane.getComponent(Weight.class).restitution = 0.2f;
		backWall.getComponent(Weight.class).restitution = 0.2f;
		sideWall.getComponent(Weight.class).restitution = 0.2f;
		cube.getComponent(Weight.class).restitution = 0.2f;
		cube2.getComponent(Weight.class).restitution = 0.1f;
		sphere.getComponent(Weight.class).restitution = 0.5f;
		sphere2.getComponent(Weight.class).restitution = 0.8f;
	}

	public static void main(String[] args)
	{
		new Main().start();
	}
}
