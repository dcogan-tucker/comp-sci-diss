package main;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import ecs.component.Collidable;
import ecs.entity.Camera;
import ecs.entity.MovableCollidableBox;
import io.output.DisplayManager;
import io.output.Window;
import ecs.entity.CollidablePlane;

public class CubeStackDemo extends Application
{

	@Override
	protected void windowSettings(Window window)
	{
		window.setDimensions(3 * DisplayManager.getScreenWidth() / 4, 3 * DisplayManager.getScreenHeight() / 4);
		window.setTitle("Cubestack Demo");
	}

	@Override
	protected void initScene(Camera camera)
	{
		CollidablePlane plane = CollidablePlane.create(new Vector3f(0, -2.5f, -7.5f), new Vector3f(0, 0, 0), new Vector2f(5, 5));
		
		MovableCollidableBox cube = MovableCollidableBox.create(
				new Vector3f(0f, 0f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		MovableCollidableBox cube1 = MovableCollidableBox.create(
				new Vector3f(0, 2f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		MovableCollidableBox cube2 = MovableCollidableBox.create(
				new Vector3f(0, 4f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		MovableCollidableBox cube3 = MovableCollidableBox.create(
				new Vector3f(0, 6f, -7.5f), 
				new Vector3f(0, 0, 0), 
				1, 1);
		
		plane.getComponent(Collidable.class).restitution = 0.2f;
		cube.getComponent(Collidable.class).restitution = 0.2f;
		cube1.getComponent(Collidable.class).restitution = 0.2f;
		cube2.getComponent(Collidable.class).restitution = 0.2f;
		cube3.getComponent(Collidable.class).restitution = 0.2f;
	}
	
	@Override
	protected void updateScene(double dt)
	{
		
	}

	public static void main(String[] args)
	{
		new CubeStackDemo().start();
	}
}
