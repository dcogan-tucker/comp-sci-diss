package uk.ac.bham.cs.domct.physicsengine.rendering;

import org.joml.Vector3f;

import uk.ac.bham.cs.domct.physicsengine.application.Application;
import uk.ac.bham.cs.domct.physicsengine.component.State;
import uk.ac.bham.cs.domct.physicsengine.entity.Camera;
import uk.ac.bham.cs.domct.physicsengine.entity.CollidableBox;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.DisplayManager;
import uk.ac.bham.cs.domct.physicsengine.systems.io.output.Window;

public class RenderingLoadVisualTest extends Application
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
		
		for (int i = 0; i < 100; i++)
		{
			CollidableBox.create(new Vector3f((float) Math.random() * 20 - 10, (float) Math.random() * 20 - 10, (float) Math.random() * -50), new Vector3f((float) Math.random() * 360, (float) Math.random() * 360, (float) Math.random() * 360), 1, 1);
		}
		

	}
	
	@Override
	protected void updateScene(double dt)
	{
	}
		
	public static void main(String[] args)
	{
		new RenderingLoadVisualTest().start();
	}
}