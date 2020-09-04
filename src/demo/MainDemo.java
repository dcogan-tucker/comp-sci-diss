package demo;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import component.*;
import entity.Camera;
import entity.CollidablePlane;
import entity.MovableCollidableBall;
import entity.MovableCollidableBox;
import systems.io.output.DisplayManager;
import systems.io.output.Window;

public class MainDemo extends Application
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
		MovableCollidableBox cube = MovableCollidableBox.create(
				new Vector3f(1, 2, -6), 
				new Vector3f(-20, 0, 0),
				0.8f, 1);
		cube.getComponent(Movable.class).momentum.set(0, 0, -10);
		MovableCollidableBall sphere = MovableCollidableBall.create(
				new Vector3f(0, 2, -7.5f),
				new Vector3f(0, 0, 0), 
				0.02f, 1);
		sphere.getComponent(Movable.class).momentum.set(-5, 0, 0);
		MovableCollidableBox cube2 = MovableCollidableBox.create(
				new Vector3f(-0.5f, 0.5f, -6f), 
				new Vector3f(0, 0, 0), 
				0.8f, 1);
		MovableCollidableBall sphere2 = MovableCollidableBall.create(
				new Vector3f(0.5f, 0.5f, -6f),
				new Vector3f(0, 0, 0), 
				0.02f, 1);
		plane.getComponent(Collidable.class).restitution = 0.6f;
		backWall.getComponent(Collidable.class).restitution = 0.6f;
		sideWall.getComponent(Collidable.class).restitution = 0.6f;
		cube.getComponent(Collidable.class).restitution = 0.1f;
		cube2.getComponent(Collidable.class).restitution = 0.3f;
		sphere.getComponent(Collidable.class).restitution = 1f;
		sphere2.getComponent(Collidable.class).restitution = 0.9f;
	}
	
	@Override
	protected void updateScene(double dt)
	{
		
	}

	public static void main(String[] args)
	{
		new MainDemo().start();
	}
}
