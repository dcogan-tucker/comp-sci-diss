package test.collision;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import component.Collidable;
import entity.CollidableBox;
import entity.CollidablePlane;
import entity.MovableCollidableBall;
import systems.EngineSystem;
import systems.physics.collision.Collision;
import systems.physics.collision.detection.narrowphase.NarrowPhaseDetector;

/**
 * Tests to verify the capability of the broad phase (axis-aligned bounding boxes), narrow phase (gjk) collision detection algorithms.#
 * 
 * @author Dominic Coagn-Tucker
 */
public class CollisionTests
{
	@AfterEach
	public void clear()
	{
		EngineSystem.clear();
	}

	
	// Checking the broad phase axis-aligned bounding box intersection, and then narrow phase GJK intersection detection of box-box mesh collisions. //
	@Test
	public void boxBoxCollisionTest1() 
	{
		// new Vector3f() sets x, y, z to 0.
		CollidableBox a = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		
		// Axis-aligned bounding box check.
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		// GJK algorithm check
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	
	@Test
	public void boxBoxCollisionTest2() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(), new Vector3f(0, 45, 0), 2, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest3() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(-0.5f, 0, 0), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(0.5f, 0, 0), new Vector3f(), 1, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest4() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(-0.5f), new Vector3f(90, 0, 0), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(0.5f), new Vector3f(0, 90, 0), 1, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest5() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(1, 0, 1), new Vector3f(0, 45, 0), 1, 1);
		
		// Rotated box results in a false positive in the initial phase.
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		// GJK reveals entities aren't actually intersecting.
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertFalse(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest6() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(), 0, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest7() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(-0.50001f, 0, 0), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(0.50001f, 0, 0), new Vector3f(), 1, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertFalse(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertFalse(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest8() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(0, -0.50001f, 0), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(0, 0.50001f, 0), new Vector3f(), 1, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertFalse(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertFalse(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest9() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(0, 0, 0.5001f), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(0, 0, -0.5001f), new Vector3f(), 1, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertFalse(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertFalse(gjkIntersection);
	}
	
	@Test
	public void boxBoxCollisionTest10() 
	{
		CollidableBox a = CollidableBox.create(new Vector3f(Float.POSITIVE_INFINITY), new Vector3f(), 1, 1);
		CollidableBox b = CollidableBox.create(new Vector3f(Float.NEGATIVE_INFINITY), new Vector3f(), 1, 1);
		
		boolean bBAreIntersecting = a.getComponent(Collidable.class).bBox.isIntersecting(b.getComponent(Collidable.class).bBox);
		assertFalse(bBAreIntersecting);
		
		Collision col = new Collision(a, b);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	// Checking the broad phase axis-aligned bounding box intersection, and then narrow phase GJK intersection detection of box-sphere mesh collisions. //
	@Test
	public void boxBallCollisionTest1() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(), new Vector3f(), 1, 1);
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(ball.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(box, ball);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxBallCollisionTest2() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(1, 0, 0), new Vector3f(), 1, 1);
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(), new Vector3f(), 0.03f, 1);
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(ball.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(box, ball);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxBallCollisionTest3() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(0, 0, 0), new Vector3f(0, 45, 0), 1, 1);
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(1, 0, 1), new Vector3f(), 0.02f, 1);
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(ball.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(box, ball);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertFalse(gjkIntersection);
	}
	
	@Test
	public void boxBallCollisionTest4() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(0, 0, 0), new Vector3f(), 1, 1);
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(1, 0, 0), new Vector3f(), 0, 1);
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(ball.getComponent(Collidable.class).bBox);
		assertFalse(bBAreIntersecting);
		
		Collision col = new Collision(box, ball);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertFalse(gjkIntersection);
	}
	
	@Test
	public void boxBallCollisionTest5() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(Float.POSITIVE_INFINITY), new Vector3f(), 100, 1);
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(ball.getComponent(Collidable.class).bBox);
		assertFalse(bBAreIntersecting);
		
		Collision col = new Collision(box, ball);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	// Checking the broad phase axis-aligned bounding box intersection, and then narrow phase GJK intersection detection of box-plane mesh collisions. //
	@Test
	public void boxPlaneBoudningBoxTest1() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidablePlane plane = CollidablePlane.create(new Vector3f(), new Vector3f(), new Vector2f(1, 1));
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(plane.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(box, plane);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxPlaneBoudningBoxTest2() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidablePlane plane = CollidablePlane.create(new Vector3f(), new Vector3f(90, 0, 0), new Vector2f(1, 1));
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(plane.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(box, plane);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxPlaneBoudningBoxTest3() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidablePlane plane = CollidablePlane.create(new Vector3f(0, 0.7f, 0), new Vector3f(0, 0, 30), new Vector2f(1, 1));
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(plane.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		
		Collision col = new Collision(box, plane);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void boxPlaneBoudningBoxTest4() 
	{
		CollidableBox box = CollidableBox.create(new Vector3f(0, -1, 0), new Vector3f(), 1, 1);
		CollidablePlane plane = CollidablePlane.create(new Vector3f(0, -0.4999f, 0), new Vector3f(), new Vector2f(10, 10));
		
		boolean bBAreIntersecting = box.getComponent(Collidable.class).bBox.isIntersecting(plane.getComponent(Collidable.class).bBox);
		assertFalse(bBAreIntersecting);
		Collision col = new Collision(box, plane);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertFalse(gjkIntersection);
	}
	
	// Checking the broad phase axis-aligned bounding box intersection, and then narrow phase GJK intersection detection of ball-plane mesh collisions. //
	@Test
	public void ballPlaneBoudningBoxTest1() 
	{
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidablePlane plane = CollidablePlane.create(new Vector3f(), new Vector3f(), new Vector2f(1, 1));
		
		boolean bBAreIntersecting = ball.getComponent(Collidable.class).bBox.isIntersecting(plane.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		Collision col = new Collision(ball, plane);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void ballPlaneBoudningBoxTest2() 
	{
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidablePlane plane = CollidablePlane.create(new Vector3f(0, 0.5f, 0), new Vector3f(0, 45, 45), new Vector2f(10, 10));
		
		boolean bBAreIntersecting = ball.getComponent(Collidable.class).bBox.isIntersecting(plane.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		Collision col = new Collision(ball, plane);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
	
	@Test
	public void ballPlaneBoudningBoxTest3() 
	{
		MovableCollidableBall ball = MovableCollidableBall.create(new Vector3f(), new Vector3f(), 1, 1);
		CollidablePlane plane = CollidablePlane.create(new Vector3f(0, -0.5f, 0), new Vector3f(0, 180, 0), new Vector2f(10, 10));
		
		boolean bBAreIntersecting = ball.getComponent(Collidable.class).bBox.isIntersecting(plane.getComponent(Collidable.class).bBox);
		assertTrue(bBAreIntersecting);
		Collision col = new Collision(ball, plane);
		
		boolean gjkIntersection = NarrowPhaseDetector.areIntersecting(col);
		assertTrue(gjkIntersection);
	}
}
