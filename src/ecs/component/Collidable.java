package ecs.component;

import ecs.system.physics.collision.broadphase.BoundingBox;
import ecs.system.physics.collision.narrowphase.ConvexHull;

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
