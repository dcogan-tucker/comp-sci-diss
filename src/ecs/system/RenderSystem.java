package ecs.system;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecs.component.Material;
import ecs.component.Mesh;
import ecs.component.State;
import ecs.component.View;
import ecs.component.Weight;
import ecs.entity.Arrow;
import ecs.entity.Camera;
import ecs.entity.Entity;
import openGLObjects.Vao;
import shaders.EntityShader;
import utils.MatrixUtils;

/**
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class RenderSystem extends EngineSystem
{
	private static Camera camera;
	private static State cameraState;
	private static EntityShader shader;
	
	protected static Map<Mesh, Map<Material, List<Entity>>> entities = new HashMap<>();
	public static List<Entity> arrows = new ArrayList<>();
	private static boolean arrowInitialised = false;
	
	/**
	 * Private constructor to ensure that the class isn't unnecessarily
	 * initialised.
	 */
	private RenderSystem()
	{
		
	}
	
	/**
	 * Initialises the rendering process by creating the materials
	 * and meshes.
	 */
	public static void initialise(Camera cam, EntityShader s)
	{
		MaterialSystem.createAll();
		MaterialSystem.create(Arrow.initMaterial());
		MeshSystem.createAll();
		camera = cam;
		cameraState = camera.getComponent(State.class);
		shader = s;
		shader.bind();
	}
	
	/**
	 * Closes the rendering system, deleting all material and mesh
	 * data.
	 */
	public static void close()
	{
		MaterialSystem.destroyAll();
		MeshSystem.destroyAll();
	}
	
	/**
	 * Renders all meshes.
	 * 
	 * @param cam The camera view of the scene.
	 * @param s The shader program to use.
	 */
	public static void renderMeshes()
	{
		generateArrows();
		entities.forEach((mesh, entities) -> 
			{
				Vao vao = mesh.vao;
				vao.bind();
				glEnableVertexAttribArray(0);
				entities.forEach((material, list) -> 
					{
						if (material != null)
						{
							bindMaterial(material);
						}
						list.forEach(entity -> 
							{
								drawEntity(entity, mesh);
							});
						if (material != null)
						{
							glDisableVertexAttribArray(1);
						}
					});
				glDisableVertexAttribArray(0);
			});
		//shader.unbind();
	}
	
	/**
	 * Binds the given material to the active texture slot.
	 * 
	 * @param material The material to bind.
	 */
	private static void bindMaterial(Material material)
	{
		glEnableVertexAttribArray(1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, material.textureID);
	}
	
	/**
	 * Draws the mesh at the entity's position.
	 * 
	 * @param entity The entity.
	 * @param mesh The mesh to draw.
	 */
	private static void drawEntity(Entity entity, Mesh mesh)
	{
		State entityState = entity.getComponent(State.class);
		shader.setModelMatrix(MatrixUtils.transformMatrix(entityState.position, 
				entityState.rotation, ((Weight) entity.getComponent(Weight.class)).scale));
		shader.setViewMatrix(MatrixUtils.viewMatrix(cameraState.position, cameraState.rotation));
		shader.setProjectionMatrix(((View) camera.getComponent(View.class)).window.getProjectionMatrix());
		glDrawElements(GL_TRIANGLES, mesh.indices.length, GL_UNSIGNED_INT, 0);
	}
	
	private static void generateArrows()
	{
		if (arrows.size() != 0)
		{
			Arrow a = (Arrow) arrows.get(0);
			Mesh mesh = a.getComponent(Mesh.class);
			Material material = a.getComponent(Material.class);
			if (!arrowInitialised)
			{
				MeshSystem.create(mesh, material);
			}
			Map<Material, List<Entity>> map = new HashMap<>();
			map.put(material, arrows);
			entities.put(mesh, map);
		}
	}
}