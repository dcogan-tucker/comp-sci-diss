package systems.io.output;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Class containing static methods that provide easy access to display values i.e screen size.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class DisplayManager
{
	/**
	 * Returns the dimensions of the screen.
	 * 
	 * @return The dimensions of the screen.
	 */
	public static Dimension getScreenSize()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	/**
	 * Returns the screen width.
	 * 
	 * @return The screen width in pixels.
	 */
	public static int getScreenWidth()
	{
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	/**
	 * Returns the screen height.
	 * 
	 * @return The screen height in pixels.
	 */
	public static int getScreenHeight()
	{
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
}
