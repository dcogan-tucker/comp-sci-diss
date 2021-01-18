package uk.ac.bham.cs.domct.physicsengine.component;

import uk.ac.bham.cs.domct.physicsengine.systems.io.output.Window;

/**
 * Component that stores the view of the entity. Used for the camera entity.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class View extends Component
{
	/**
	 * The window which the view is in.
	 */
	public Window window;
}
