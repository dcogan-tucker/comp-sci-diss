package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Controllable;
import ecs.component.Moveable;
import ecs.component.State;
import ecs.component.View;
import io.output.Window;

/**
 * An entity with the position, movable and view components attached. This
 * allows the camera to be placed in the scene and be moved around and contains
 * act as the view into the scene.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Camera extends Entity
{
	/**
	 * Construct a Camera taking in the window to use as it's view.
	 * 
	 * @param window The window to use.
	 */
	public Camera(Window window)
	{
		super();
		// Create and attach a position component.
		setPositionComponent(new Vector3f(), new Vector3f());
		// Create and attach a movable component.
		setMoveableComponent();
		// Create and attach a controllable component.
		setControllableComponent();
		// Create and attach the view component.
		setViewComponent(window);
	}

	/**
	 * Construct a Camera at the given position and rotation, taking in the window
	 * to use as it's view.
	 * 
	 * @param window The window to use.
	 * @param pos The position of the camera.
	 * @param rot The rotation of the camera.
	 */
	public Camera(Window window, Vector3f pos, Vector3f rot)
	{
		super();
		// Create and attach a position component
		setPositionComponent(pos, rot);
		// Create and attach a movable component
		setMoveableComponent();
		// Create and attach the view component.
		setViewComponent(window);
	}

	/**
	 * Sets and adds the position component to this camera.
	 * 
	 * @param pos The position of the camera.
	 * @param rot The rotation of the camera.
	 */
	private void setPositionComponent(Vector3f pos, Vector3f rot)
	{
		State positionComp = new State();
		positionComp.position = pos;
		positionComp.rotation = rot;
		addComponent(positionComp);
	}
	
	/**
	 * Sets the controllable component of the camera.
	 */
	private void setControllableComponent()
	{
		Controllable controlComp = new Controllable();
		controlComp.speed = 0.05f;
		addComponent(controlComp);
	}

	/**
	 * Sets and adds the movable component to this camera. Initially velocity and
	 * acceleration set to zero.
	 */
	private void setMoveableComponent()
	{
		Moveable moveableComp = new Moveable();
		moveableComp.force = new Vector3f();
		addComponent(moveableComp);
	}

	/**
	 * Sets and adds the view component to this camera.
	 * 
	 * @param window The window of the view.
	 */
	private void setViewComponent(Window window)
	{
		View viewComp = new View();
		viewComp.window = window;
		addComponent(viewComp);
	}
}
