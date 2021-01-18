package uk.ac.bham.cs.domct.physicsengine.demo;

import org.joml.Vector2f;
import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.application.Application;
import uk.ac.bham.cs.domct.physicsengine.component.Collidable;
import uk.ac.bham.cs.domct.physicsengine.entity.Camera;
import uk.ac.bham.cs.domct.physicsengine.entity.CollidablePlane;
import uk.ac.bham.cs.domct.physicsengine.entity.MovableCollidableBox;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.DisplayManager;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.Window;

public class BoxStackDemo extends Application
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
		
		plane.getComponent(Collidable.class).restitution = 0.6f;
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
		new BoxStackDemo().start();
	}
}
