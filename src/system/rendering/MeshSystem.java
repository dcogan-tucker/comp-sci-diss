package system.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import component.Component;
import component.Material;
import component.Mesh;
import entity.Entity;
import system.EngineSystem;
import system.rendering.openGLObjects.Vao;

/**
 * The system that deals with the creation and deletion of meshes.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
final class MeshSystem extends EngineSystem
{	
	/**
	 * Creates the renderMap and then all entity meshes.
	 */
	@Override
	public void initialise()
	{	
		createRenderMap();
		createAll();
	}
	
	/**
	 * Destroy all entity meshes.
	 */
	@Override
	public void close()
	{
		destroyAll();
	}
	
	/**
	 * Creates the meshes for all entities.
	 */
	private static void createAll()
	{
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
	private static void destroyAll()
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
	private static void create(Mesh mesh, Material material)
	{
		mesh.vao = new Vao();
		mesh.vao.storeData(mesh.vertices, mesh.textures, mesh.indices);
	}
	
	/**
	 * Deletes the given mesh data.
	 * 
	 * @param mesh The mesh data to delete.
	 */
	private static void destroy(Mesh mesh)
	{
		mesh.vao.delete();
	}
	
	/**
	 * Creates a map of all entities to render. 
	 */
	private static void createRenderMap()
	{
		Map<Entity, Component> entitiesMap = getEntities(Mesh.class);
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
						map.put(e.getComponent(Material.class), eList);
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
						mat = (e.getComponent(Material.class));
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
