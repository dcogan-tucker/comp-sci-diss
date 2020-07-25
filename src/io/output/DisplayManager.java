package io.output;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DisplayManager
{
	public static Dimension getScreenSize()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	public static int getScreenWidth()
	{
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	public static int getScreenHeight()
	{
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
}
