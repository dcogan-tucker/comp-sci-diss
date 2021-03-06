package uk.ac.bham.cs.domct.physicsengine.collision;
import org.joml.Vector2f;
import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.application.Application;
import uk.ac.bham.cs.domct.physicsengine.component.Movable;
import uk.ac.bham.cs.domct.physicsengine.component.State;
import uk.ac.bham.cs.domct.physicsengine.entity.Camera;
import uk.ac.bham.cs.domct.physicsengine.entity.CollidableBox;
import uk.ac.bham.cs.domct.physicsengine.entity.CollidablePlane;
import uk.ac.bham.cs.domct.physicsengine.entity.MovableCollidableBall;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.DisplayManager;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.Window;
import uk.ac.bham.cs.domct.physicsengine.systems.physics.collision.CollisionSystem;

public class CollisionVisualTest extends Application
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
		camera.getComponent(State.class).position.set(new Vector3f(0, 0, 5));
		
		CollidableBox.create(new Vector3f(-0.3f, -1.3f, -1.7f), new Vector3f(), 1, 1);
		CollidableBox.create(new Vector3f(0.3f, -0.7f, -2.3f), new Vector3f(), 1, 1);
		
		CollidablePlane.create(new Vector3f(-3, -1, -2), new Vector3f(), new Vector2f(2));
		MovableCollidableBall.create(new Vector3f(-3, -1, -2), new Vector3f(), 0.02f, 1).removeComponent(Movable.class);
		
		CollidablePlane.create(new Vector3f(3, -1, -2), new Vector3f(), new Vector2f(2));
		CollidableBox.create(new Vector3f(3, -0.5f, -2), new Vector3f(), 0.8f, 1);
	}
	
	@Override
	protected void updateScene(double dt)
	{
		CollisionSystem.enableCollisionDebugging();
	}
		
	public static void main(String[] args)
	{
		new CollisionVisualTest().start();
	}
}
