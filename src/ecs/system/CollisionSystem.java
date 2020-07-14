package ecs.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecs.component.Collidable;
import ecs.component.Component;
import ecs.entity.Entity;
import physics.collisionDetection.broadphase.BroadPhaseDetector;
import physics.collisionDetection.collisionData.CollisionResolver;
import physics.collisionDetection.narrowphase.NarrowPhaseDetector;
import physics.collisionDetection.narrowphase.Simplex;

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
	public static void collisionDetection()
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
					collidingPair[0] = entities.get(i);
					collidingPair[1] = entities.get(j);
					broadPhaseCollisions.add(collidingPair);
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
				cr.generateCollisionData();
				System.out.println(collidingPair[0].getClass().getSimpleName() 
						+ " colliding with " + collidingPair[1].getClass().getSimpleName()
						+ " WorldPoint: " + cr.getContactData().worldPoint 
						+ ", WorldNormal: " + cr.getContactData().worldNormal
						+ ", PenDepth: " + cr.getContactData().penDepth);
			});
		
		broadPhaseCollisions.clear();
		narrowPhaseCollisions.clear();
	}
}
