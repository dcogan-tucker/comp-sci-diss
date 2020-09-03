package test.collisionTests;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import component.Collidable;
import component.State;
import entity.Camera;
import entity.CollidablePlane;
import entity.MovableCollidableBox;
import system.io.output.DisplayManager;
import system.io.output.Window;

public class CollisionLoadTest extends Application
{
	
	@Override
	protected void windowSettings(Window window)
	{
		window.setDimensions(3 * DisplayManager.getScreenWidth() / 4, 3 * DisplayManager.getScreenHeight() / 4);
		window.setTitle("Test Scene");
	}

	@Override
	protected void initScene(Camera camera)
	{
		camera.getComponent(State.class).position.set(0, 10, 50);
		camera.getComponent(State.class).rotation.set(20, 0, 0);
		
		CollidablePlane.create(new Vector3f(0, -25, -25), new Vector3f(), new Vector2f(20, 20));
		
		for (int i = -4; i < 5; i++)
		{
			MovableCollidableBox.create(new Vector3f(2 * i, -10, -20), new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()), 0.5f, 1)
									.getComponent(Collidable.class).restitution = 0.3f;
		}
		
		for (int i = -4; i < 5; i++)
		{
			MovableCollidableBox.create(new Vector3f(2 * i, -10, -30), new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()), 0.5f, 1)
									.getComponent(Collidable.class).restitution = 0.6f;
		}
	}
	
	@Override
	protected void updateScene(double dt)
	{
		
	}
		
	public static void main(String[] args)
	{
		new CollisionLoadTest().start();
	}
}