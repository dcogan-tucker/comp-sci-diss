package system.physics.collision;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import component.Collidable;
import component.State;
import entity.Entity;
import system.EngineSystem;
import system.physics.collision.contactGeneration.ContactPointGenerator;
import system.physics.collision.detection.broadphase.BroadPhaseDetector;
import system.physics.collision.detection.narrowphase.NarrowPhaseDetector;
import system.physics.collision.response.ImpulseCalculator;
import system.physics.dynamics.EulerIntegrator;

/**
 * Class that holds collision detection method.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class CollisionSystem extends EngineSystem
{
	private Set<Collision> collisions = new TreeSet<>();
	private Set<Collision> previous = new TreeSet<>();
	
	private float dt;
	
	public CollisionSystem(double dt)
	{
		this.dt = (float) dt;
	}
	
	/**
	 * Checks every entity in these scene and determines if any are colliding.
	 * Performs two stages of detection: a broad phase, where the bounding boxes
	 * of entities within a certain distance of eachother are checked for 
	 * intersections; and a narrow phase, where any intersections in the first 
	 * phase are check using GJK distance algorithm for a more accurate mesh
	 * collision detection.
	 */
	@Override
	public void update()
	{
		broadPhase();
		narrowPhase();
		
		collisionResolution(dt);
	}
	
	/**
	 * Resolves any collisions remaining on the collisions list. Generating contact
	 * data and the impulse to apply to entities involved in collisions.
	 * 
	 * @param dt The time taken for a frame.
	 */
	private void collisionResolution(float dt)
	{
		for (Iterator<Collision> i = collisions.iterator(); i.hasNext();)
		{
			Collision col = i.next();
			ContactPointGenerator cpg = new ContactPointGenerator(col.sim);
			if (cpg.generateCollisionData())
			{
				EulerIntegrator.stepBack(col.a, col.b);
				try
				{
					col.contact = cpg.getContactPoint();
				} catch (Exception e)
				{	
					e.printStackTrace();
				}
				ImpulseCalculator.calculate(col, dt);
			}
			else
			{
				i.remove();
			}
		}
		
		previous.addAll(collisions);
		collisions.clear();
	}
	
	/**
	 * Broad phase of collision detection. All entity pairs within a certain distance
	 * to eachother are checked to see if their bounding boxes intersect. If the
	 * bounding boxes of two entities collide a Collision object is created with these
	 * entities added to a list which is passed onto the next phase.
	 */
	private void broadPhase()
	{
		// Get all the entities created with the Collidable component.
		Object[] entitiesArray = getEntities(Collidable.class).keySet().toArray();
		// Check every entity against every other.
		for (int i = 0; i < entitiesArray.length; i++)
		{
			Entity a = (Entity) entitiesArray[i];
			for (int j = i + 1; j < entitiesArray.length; j++)
			{
				Entity b = (Entity) entitiesArray[j];
				// Only proceed if the pair of entities are within a given distance of eachother.
				if (a.getComponent(State.class).position.distance(b.getComponent(State.class).position) < 10)
				{
					// Update the bounding boxes for these entities this frame and check if they are intersecting.
					if (BroadPhaseDetector.areIntersecting(a, b))
					{
						Collision collision = new Collision(a, b);
						// Check if these entities were colliding in the previous frame.
						for (Collision c :  previous)
						{
							if (c.equals(collision))
							{
								collision.sim = c.sim;
								break;
							}
						}
						collisions.add(collision);
					}
				}
			}
		}
		// Clear all previous frame collisions.
		previous.clear();
	}
	
	/**
	 * Narrow phase of collision detection. Checks every collision detected in the broad 
	 * phase and applies the GJK distance algorithm to the entities involved to determine
	 * if their meshes are colliding. If there is no mesh collision detection detected then
	 * the Collision object is removed from the list.
	 */
	private void narrowPhase()
	{
		// Cycle through every broad phase collision.
		for (Iterator<Collision> i = collisions.iterator(); i.hasNext();)
		{
			Collision col = i.next();
			if (!NarrowPhaseDetector.areIntersecting(col))
			{
				i.remove();
			}
		}
	}
	
	/**
	 * Returns the set of entities that are colliding.
	 * 
	 * @return The set of entities that are colliding.
	 */
	public Set<Collision> getCollidingPairs()
	{
		return previous;
	}
}
