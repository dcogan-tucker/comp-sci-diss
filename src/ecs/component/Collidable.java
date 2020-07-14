package ecs.component;

import physics.collisionDetection.broadphase.BoundingBox;
import physics.collisionDetection.narrowphase.ConvexHull;

/**
 * Component that holds an entity's bounding box for collision detection.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Collidable extends Component
{
	/**
	 * The entity's bounding box.
	 */
	public BoundingBox bBox;
	
	/**
	 * The entity's convex hull.
	 */
	public ConvexHull hull;
}
