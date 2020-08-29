package collisionTests;

import org.joml.Vector2f;
import org.joml.Vector3f;

import application.Application;
import ecs.component.Moveable;
import ecs.component.State;
import ecs.entity.Camera;
import ecs.entity.CollidableBox;
import ecs.entity.CollidablePlane;
import ecs.entity.MoveableCollidableBall;
import io.output.DisplayManager;
import io.output.Window;

public class CollisionTest extends Application
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
		MoveableCollidableBall.create(new Vector3f(-3, -1, -2), new Vector3f(), 0.02f, 1).removeComponent(Moveable.class);
		
		CollidablePlane.create(new Vector3f(3, -1, -2), new Vector3f(), new Vector2f(2));
		CollidableBox.create(new Vector3f(3, -0.5f, -2), new Vector3f(), 0.8f, 1);
	}
	
	@Override
	protected void updateScene(double dt)
	{
		
	}
		
	public static void main(String[] args)
	{
		new CollisionTest().start();
	}
}
