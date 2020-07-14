package ecs.system;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecs.component.Material;
import ecs.component.Mesh;
import ecs.component.Scale;
import ecs.component.State;
import ecs.component.View;
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
	public static void initialise()
	{
		MaterialSystem.createAll();
		MeshSystem.createAll();
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
	public static void renderMeshes(Camera cam, EntityShader s)
	{
		camera = cam;
		shader = s;
		cameraState = ((State) camera.getComponent(State.class));
		shader.bind();
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
		shader.unbind();
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
		State entityState = ((State) entity.getComponent(State.class));
		shader.setModelMatrix(MatrixUtils.transformMatrix(entityState.position, 
				entityState.rotation, ((Scale) entity.getComponent(Scale.class)).scale));
		shader.setViewMatrix(MatrixUtils.viewMatrix(cameraState.position, cameraState.rotation));
		shader.setProjectionMatrix(((View) camera.getComponent(View.class)).window.getProjectionMatrix());
		glDrawElements(GL_TRIANGLES, mesh.indices.length, GL_UNSIGNED_INT, 0);
	}
}