package test.entityManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import component.Collidable;
import component.Component;
import component.Movable;
import component.State;
import entity.Entity;
import systems.EngineSystem;

/**
 * Tests for the Entity-Component management.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class EntityTests
{
	private static Entity a, b, c;
	
	@BeforeEach
	public void init()
	{
		a = new Entity();
		b = new Entity();
		c = new Entity();
	}
	
	@Test
	public void leaveEntityEmpty()
	{
		assertFalse(a.hasComponent(State.class));
		assertFalse(a.hasComponent(Collidable.class));
	}
	
	@Test
	public void addStaetComponentTest()
	{
		State s = new State();
		a.addComponent(s);
		assertTrue(a.hasComponent(State.class));
	}
	
	@Test
	public void addCollidableComponent()
	{
		Collidable c = new Collidable();
		a.addComponent(c);
		assertTrue(a.hasComponent(Collidable.class));
	}
	
	@Test
	public void addThenRemoveStateComponent()
	{
		State s = new State();
		a.addComponent(s);
		a.removeComponent(State.class);
		assertFalse(a.hasComponent(State.class));
	}
	
	@Test
	public void addMultipleComponentsThenRemoveAll()
	{
		State s = new State();
		Collidable c = new Collidable();
		Movable m = new Movable();
		a.addComponent(s).addComponent(c).addComponent(m);
		assertTrue(a.hasComponent(State.class));
		assertTrue(a.hasComponent(Collidable.class));
		assertTrue(a.hasComponent(Movable.class));
		a.removeAllComponents();
		assertFalse(a.hasComponent(State.class));
		assertFalse(a.hasComponent(Collidable.class));
		assertFalse(a.hasComponent(Movable.class));
	}
	
	@Test
	public void addComponentAndModify()
	{
		State s = new State();
		a.addComponent(s);
		a.getComponent(State.class).position.set(1, 2, 3);
		Vector3f actual = a.getComponent(State.class).position;
		Vector3f expected = new Vector3f(1, 2, 3);
		assertEquals(expected, actual);
	}
	
	@Test
	public void entitiesShareInstanceOfComponent()
	{
		State s = new State();
		a.addComponent(s);
		b.addComponent(s);
		a.getComponent(State.class).rotation.set(100);
		Vector3f rotA = a.getComponent(State.class).rotation;
		Vector3f rotB = b.getComponent(State.class).rotation;
		Vector3f expected = new Vector3f(100);
		assertEquals(expected, rotA);
		assertEquals(expected, rotB);
	}
	
	@Test
	public void checkEntitiesWithStateComponent()
	{
		State s = new State();
		Collidable col = new Collidable();
		a.addComponent(s);
		b.addComponent(s);
		c.addComponent(col);
		Map<Entity, Component> actual = EngineSystem.getEntities(State.class);
		Map<Entity, Component> expected = new HashMap<>();
		expected.put(a, s);
		expected.put(b, s);
		assertTrue(expected.entrySet().stream()
			      .allMatch(e -> e.getValue().equals(actual.get(e.getKey()))));
	}
	
	@Test
	public void uniqueIDs()
	{
		assertFalse(a.getID().equals(b.getID()));
		assertFalse(b.getID().equals(c.getID()));
	}
}
