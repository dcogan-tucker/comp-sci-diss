package ecs.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecs.component.Component;
import ecs.component.Material;
import ecs.component.Mesh;
import ecs.entity.Entity;
import openGLObjects.Vao;

/**
 * The system that deals with the creation and deletion of meshes.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
final class MeshSystem extends EngineSystem
{	
	/**
	 * Private constructor to ensure  
	 */
	private MeshSystem()
	{
		
	}
	
	/**
	 * Creates the meshes for all entities.
	 */
	protected static void createAll()
	{
		createRenderMap();
		RenderSystem.entities.forEach((mesh, entities) -> 
			{
				entities.forEach((material, list) -> 
					{
						create(mesh, material);
					});
			});
	}
	
	/**
	 * Deletes all mesh data.
	 */
	protected static void destroyAll()
	{
		RenderSystem.entities.forEach((mesh, materialMap) -> 
			{
				destroy(mesh);
			});
	}
	
	/**
	 * Creates a given mesh.
	 * 
	 * @param mesh The mesh to be created.
	 * @param material The material associated with the mesh.
	 */
	protected static void create(Mesh mesh, Material material)
	{
		mesh.vao = new Vao();
		mesh.vao.storeData(mesh.vertices, mesh.textures, mesh.indices);
	}
	
	/**
	 * Deletes the given mesh data.
	 * 
	 * @param mesh The mesh data to delete.
	 */
	protected static void destroy(Mesh mesh)
	{
		mesh.vao.delete();
	}
	
	/**
	 * Creates a map of all entities to render. 
	 */
	private static void createRenderMap()
	{
		Map<Entity, Component> entitiesMap = Component.componentMap.get(Mesh.class);
		entitiesMap.forEach((e, c) ->
			{
				Map<Material, List<Entity>> map;
				List<Entity> eList;
				if (RenderSystem.entities.get((Mesh) c) == null)
				{
					map = new HashMap<>();
					eList = new ArrayList<>();
					eList.add(e);
					if (e.hasComponent(Material.class))
					{
						map.put(((Material) e.getComponent(Material.class)), eList);
					}
					else
					{
						map.put(null, eList);
					}
					RenderSystem.entities.put((Mesh) c, map);
				}
				else
				{
					map = RenderSystem.entities.get((Mesh) c);
					Material mat;
					if (e.hasComponent(Material.class))
					{
						mat = ((Material) e.getComponent(Material.class));
					}
					else
					{
						mat = null;
					}
					
					if (map.get(mat) == null)
					{
						eList = new ArrayList<>();
						eList.add(e);
						map.put((mat), eList);
					}
					else
					{
						eList = map.get(mat);
						eList.add(e);
						map.put((mat), eList);
					}
					RenderSystem.entities.put((Mesh) c, map);
				}
			});
	}
}
