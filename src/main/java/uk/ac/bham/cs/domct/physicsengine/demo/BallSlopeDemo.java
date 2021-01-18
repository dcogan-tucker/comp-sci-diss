package uk.ac.bham.cs.domct.physicsengine.demo;

import org.joml.Vector2f;
import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.application.Application;
import uk.ac.bham.cs.domct.physicsengine.component.Collidable;
import uk.ac.bham.cs.domct.physicsengine.entity.Camera;
import uk.ac.bham.cs.domct.physicsengine.entity.CollidablePlane;
import uk.ac.bham.cs.domct.physicsengine.entity.MovableCollidableBall;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.DisplayManager;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.Window;

public class BallSlopeDemo extends Application
{

	@Override
	protected void windowSettings(Window window)
	{
		window.setDimensions(3 * DisplayManager.getScreenWidth() / 4, 3 * DisplayManager.getScreenHeight() / 4);
		window.setTitle("Slope Demo");
	}

	@Override
	protected void initScene(Camera camera)
	{
		CollidablePlane slope = CollidablePlane.create(new Vector3f(0, -2.5f, -7.5f), new Vector3f(0, 0, -15), new Vector2f(5, 5));		
		MovableCollidableBall sphere = MovableCollidableBall.create(
				new Vector3f(-1.5f, 1f, -7.5f), 
				new Vector3f(0, 0, 0), 
				0.025f, 1);
		slope.getComponent(Collidable.class).restitution = 0.65f;
		sphere.getComponent(Collidable.class).restitution = 1.1f;
		slope.getComponent(Collidable.class).friction = 0.3f;
		sphere.getComponent(Collidable.class).friction = 0.3f;
	}
	
	@Override
	protected void updateScene(double dt)
	{
		
	}
	
	public static void main(String[] args)
	{
		new BallSlopeDemo().start();
	}
}
