package ecs.component;

import org.joml.Vector3f;

/**
 * Component that stores the scale factors of the entity.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Scale extends Component
{
	/**
	 * The scale of the entity as a Vector. With each component scaling the
	 * respective axis.
	 */
	public Vector3f scale;
}
