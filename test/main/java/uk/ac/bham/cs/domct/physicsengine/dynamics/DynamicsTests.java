package uk.ac.bham.cs.domct.physicsengine.dynamics;

import org.joml.Vector3f;

import org.junit.Test;
import uk.ac.bham.cs.domct.physicsengine.component.Movable;
import uk.ac.bham.cs.domct.physicsengine.component.State;
import uk.ac.bham.cs.domct.physicsengine.entity.MovableCollidableBall;
import uk.ac.bham.cs.domct.physicsengine.entity.MovableCollidableBox;
import uk.ac.bham.cs.domct.physicsengine.systems.physics.dynamics.EulerIntegrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing the capabilities of the systems ability to calculate motion.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class DynamicsTests
{
	private static final float TOLLERANCE = 0.0001f;
	
	@Test
	public void stationaryBox()
	{
		MovableCollidableBox box = MovableCollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		box.getComponent(Movable.class).force.set(0);
		EulerIntegrator.integrate(100, box);
		
		Vector3f actual = box.getComponent(State.class).position;
		Vector3f expected = new Vector3f();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void stationaryBall()
	{
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(), new Vector3f(), 1, 1);
		ball.getComponent(Movable.class).force.set(0);
		EulerIntegrator.integrate(5, ball);
		
		Vector3f actual = ball.getComponent(State.class).position;
		Vector3f expected = new Vector3f();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void movingBox1()
	{
		MovableCollidableBox box = MovableCollidableBox.create(new Vector3f(0, 10, 0), new Vector3f(), 1, 1);
		box.getComponent(Movable.class).force.set(0, 10, 0);
		EulerIntegrator.integrate(10, box);
		
		Vector3f actual = box.getComponent(State.class).position;
		Vector3f expected = new Vector3f(0, 1010, 0);
		
		assertTrue(expected.equals(actual, expected.length() * TOLLERANCE));
	}
	
	@Test
	public void movingBox2()
	{
		MovableCollidableBox box = MovableCollidableBox.create(new Vector3f(-5, 0, 1), new Vector3f(), 1, 10);
		box.getComponent(Movable.class).force.set(-10, 0, 0);
		EulerIntegrator.integrate(1, box);
		
		Vector3f actual = box.getComponent(State.class).position;
		Vector3f expected = new Vector3f(-6, 0, 1);
		
		assertTrue(expected.equals(actual, expected.length() * TOLLERANCE));
	}
	
	@Test
	public void movingBox3()
	{
		MovableCollidableBox box = MovableCollidableBox.create(new Vector3f(1, 2, 3), new Vector3f(), 1, 321);
		box.getComponent(Movable.class).force.set(-3, -2, 1);
		EulerIntegrator.integrate(123, box);
		
		Vector3f actual = box.getComponent(State.class).position;
		Vector3f expected = new Vector3f(-140.39f, -92.26f, 50.13f);

		assertTrue(expected.equals(actual, expected.length() * TOLLERANCE));
	}
	
	@Test
	public void movingBall()
	{
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(1, 2, 3), new Vector3f(), 1, 321);
		ball.getComponent(Movable.class).force.set(-3, -2, 1);
		EulerIntegrator.integrate(123, ball);
		
		Vector3f actual = ball.getComponent(State.class).position;
		Vector3f expected = new Vector3f(-140.39f, -92.26f, 50.13f);

		assertTrue(expected.equals(actual, expected.length() * TOLLERANCE));
	}
	
	@Test
	public void rotatingBox()
	{
		MovableCollidableBox box = MovableCollidableBox.create(new Vector3f(0, 0, 0), new Vector3f(), 1, 1);
		box.getComponent(Movable.class).torque.set(0, 1, 0);
		EulerIntegrator.integrate(100, box);
		
		Vector3f actual = box.getComponent(State.class).rotation;
		Vector3f expected = new Vector3f(0, 3.438E+6f, 0);
		
		assertTrue(expected.equals(actual, expected.length() * TOLLERANCE));
	}
	
	@Test
	public void rotatingBall()
	{
		MovableCollidableBox ball = MovableCollidableBox.create(new Vector3f(Float.POSITIVE_INFINITY), new Vector3f(), 1, 1);
		ball.getComponent(Movable.class).torque.set(-1, 2, -3);
		EulerIntegrator.integrate(5, ball);
		
		Vector3f actual = ball.getComponent(State.class).rotation;
		Vector3f expected = new Vector3f(-8.594E+3f,  1.719E+4f, -2.578E+4f);
		
		assertTrue(expected.equals(actual, expected.length() * TOLLERANCE));
	}
}
