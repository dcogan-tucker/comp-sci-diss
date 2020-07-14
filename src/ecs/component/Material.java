package ecs.component;

import java.nio.ByteBuffer;

/**
 * The material component of the entity, holds data about the entity's texture.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class Material extends Component
{	
	/**
	 * The OpenGL texture ID.
	 */
	public int textureID;
	
	/**
	 * The texture png as a ByteBuffer. 
	 */
	public ByteBuffer texture;
	
	/**
	 * The height and width of the texture.
	 */
	public int width, height;
	
	/**
	 * The path location of the texture as a String.
	 */
	public String texturePath;
}
