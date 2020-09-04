package systems.rendering;

import static org.lwjgl.opengl.GL11.*;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import component.Component;
import component.Material;
import entity.Entity;
import systems.EngineSystem;

/**
 * The system that deals with the creation of all materials.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
final class MaterialSystem extends EngineSystem
{
	/**
	 * A set of all the used materials.
	 */
	private static Set<Material> materials = new HashSet<Material>();
	
	/**
	 * Creates all entity materials.
	 */
	@Override
	public void initialise()
	{
		createAll();
	}
	
	/**
	 * Destroys all entity materials.
	 */
	@Override
	public void close()
	{
		destroyAll();
	}
	
	/**
	 * Creates the textures associated all entities to be rendered.
	 */
	private static void createAll()
	{
		Map<Entity, Component> entitiesMap = getEntities(Material.class);
		// put materials into a set removing any duplicates.
		entitiesMap.forEach((e, c) -> materials.add((Material) c));
		materials.forEach(m -> create(m));
	}
	
	/**
	 * Deletes all the data stored for all the materials.
	 */
	private static void destroyAll()
	{
		materials.forEach(m -> destroy(m));
	}
	
	/**
	 * Creates the texture for the given material.
	 * 
	 * @param m The material to create the texture for.
	 */
	private static void create(Material m)
	{
		m.textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, m.textureID);
		loadTexture(m);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, m.width, m.height, 
				0, GL_RGBA, GL_UNSIGNED_BYTE, m.texture);
		glBindTexture(GL_TEXTURE_2D,  0);
	}
	
	/**
	 * Deletes the texture data for the given material.
	 * 
	 * @param m The material to delete the texture for.
	 */
	private static void destroy(Material m)
	{
		glDeleteTextures(m.textureID);
	}
	
	/**
	 * Loads the texture from the path location from the given
	 * material.
	 * 
	 * @param m The given material to load the texture from.
	 */
	private static void loadTexture(Material m)
	{
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer comp = stack.mallocInt(1);
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);

			m.texture = STBImage.stbi_load(m.texturePath, w, h, comp, 4);
			if (m.texture == null)
			{
				System.err.println("Couldn't load " + m.texturePath);
			}
			m.width = w.get();
			m.height = h.get();
		}
	}
}
