package main;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import ecs.component.*;
import ecs.entity.Camera;
import ecs.entity.MoveableCollidableBox;
import io.output.DisplayManager;
import io.output.Window;
import ecs.entity.CollidablePlane;
import ecs.entity.MoveableCollidableBall;

public class Main extends Application
{
	
	@Override
	protected void windowSettings(Window window)
	{
		window.setDimensions(3 * DisplayManager.getScreenWidth() / 4, 3 * DisplayManager.getScreenHeight() / 4);
		window.setTitle("Sandbox");
	}
	
	@Override
	protected void initScene(Camera camera)
	{
		CollidablePlane plane = CollidablePlane.create(new Vector3f(0, -2.5f, -7.5f), new Vector3f(0, 0, 0), new Vector2f(5, 5));
		CollidablePlane backWall = CollidablePlane.create(new Vector3f(0, 0, -10.1f), new Vector3f(90, 0, 0), new Vector2f(5, 5));
		CollidablePlane sideWall = CollidablePlane.create(new Vector3f(-2.6f, 0, -7.5f), new Vector3f(-90, 0, 90), new Vector2f(5, 5));
		MoveableCollidableBox cube = MoveableCollidableBox.create(
				new Vector3f(1, 2, -6), 
				new Vector3f(-25, 0, 0),
				0.8f, 1);
		cube.getComponent(Moveable.class).momentum.set(0, 0, -5);
		MoveableCollidableBall sphere = MoveableCollidableBall.create(
				new Vector3f(0, 2, -7.5f),
				new Vector3f(0, 0, 0), 
				0.02f, 1);
		sphere.getComponent(Moveable.class).momentum.set(-5, 0, 0);
		MoveableCollidableBox cube2 = MoveableCollidableBox.create(
				new Vector3f(-0.5f, 0.5f, -6f), 
				new Vector3f(0, 0, 0), 
				0.8f, 1);
		MoveableCollidableBall sphere2 = MoveableCollidableBall.create(
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
	
	@Override
	protected void updateScene(double dt)
	{
		
	}

	public static void main(String[] args)
	{
		new Main().start();
	}
}
