package ecs.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecs.component.Collidable;
import ecs.component.Component;
import ecs.component.Moveable;
import ecs.component.State;
import ecs.entity.Entity;
import ecs.entity.Plane;
import physics.collisionDetection.broadphase.BroadPhaseDetector;
import physics.collisionDetection.collisionData.Collision;
import physics.collisionDetection.collisionData.CollisionResolver;
import physics.collisionDetection.collisionData.ContactData;
import physics.collisionDetection.collisionData.ContactPoint;
import physics.collisionDetection.narrowphase.NarrowPhaseDetector;
import physics.collisionDetection.narrowphase.Simplex;
import physics.integrator.EulerIntegrator;
import physics.integrator.ImpulseCalculator;

/**
 * Class that holds collision detection method.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class CollisionSystem extends EngineSystem
{
	private static List<Entity[]> broadPhaseCollisions = new ArrayList<>();
	private static Map<Entity[], Simplex> narrowPhaseCollisions = new HashMap<>();
	private static List<Collision> collisions = new ArrayList<>();
	
	/**
	 * Private constructor to ensure that the class isn't unnecessarily
	 * initialised.
	 */
	private CollisionSystem()
	{
		
	}
	
	/**
	 * Detects whether any Collidable entities are colliding in the scene.
	 */
	public static void collisionDetection(float dt)
	{
		Map<Entity, Component> entitiesMap = Component.componentMap.get(Collidable.class);
		List<Entity> entities = new ArrayList<Entity>();
		// put all collidable entities into a list to loop through.
		entitiesMap.forEach((entity, component) -> entities.add(entity));
		
		BroadPhaseDetector.updateBBoxes(entities);
		for (int i = 0; i < entities.size(); i++)
		{
			for (int j = i + 1; j < entities.size(); j++)
			{
				if (BroadPhaseDetector.areIntersecting(entities.get(i), entities.get(j)))
				{
					Entity[] collidingPair = new Entity[2];
					if (entities.get(i).hasComponent(Moveable.class))
					{
						collidingPair[0] = entities.get(i);
						collidingPair[1] = entities.get(j);
					}
					else if (entities.get(j).hasComponent(Moveable.class))
					{
						collidingPair[0] = entities.get(j);
						collidingPair[1] = entities.get(i);
					}
					
					if (collidingPair[0] != null)
					{
						broadPhaseCollisions.add(collidingPair);
					}
				}
			}
		}
		
		broadPhaseCollisions.forEach(collidingPair ->
			{
				if(NarrowPhaseDetector.areIntersecting(collidingPair[0], collidingPair[1]))
				{
					narrowPhaseCollisions.put(collidingPair, NarrowPhaseDetector.getSimplex());
				}
			});
		
		narrowPhaseCollisions.forEach((collidingPair, simplex)-> 
			{
				CollisionResolver cr = new CollisionResolver(simplex);
				if (cr.generateCollisionData())
				{
					EulerIntegrator.stepBack(collidingPair);
					
					Collision collision = new Collision();
					collision.a = collidingPair[0];
					collision.b = collidingPair[1];
					ContactPoint point = cr.getContactPoint();
					boolean contains = false;
					for (int i = 0; i < collisions.size(); i++)
					{
						if (collisions.get(i).equals(collision))
						{
							collisions.get(i).data.addContact(point);
							contains = true;
							collision = collisions.get(i);
							break;
						}
					}
					if (!contains)
					{
						collision.data = new ContactData();
						collision.data.addContact(point);
						collisions.add(collision);
					}
					//System.out.println(collidingPair[0] + " is colliding with " + collidingPair[1]);
					ImpulseCalculator.calculate(collision, dt);
				}
			});
		
		broadPhaseCollisions.clear();
		narrowPhaseCollisions.clear();
		collisions.clear();
	}
}
