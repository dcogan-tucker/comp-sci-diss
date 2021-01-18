package uk.ac.bham.cs.domct.physicsengine.systems.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.bham.cs.domct.physicsengine.component.Component;
import uk.ac.bham.cs.domct.physicsengine.component.Material;
import uk.ac.bham.cs.domct.physicsengine.component.Mesh;
import uk.ac.bham.cs.domct.physicsengine.entity.Entity;
import uk.ac.bham.cs.domct.physicsengine.systems.EngineSystem;
import uk.ac.bham.cs.domct.physicsengine.systems.rendering.openGLObjects.Vao;

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
				entities.forEach((material, list) ->
						create(mesh, material)));
	}
	
	/**
	 * Deletes all mesh data.
	 */
	private static void destroyAll()
	{
		RenderSystem.entities.forEach((mesh, materialMap) ->
				destroy(mesh));
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
				Mesh m = (Mesh) c;
				if (RenderSystem.entities.get(m) != null)
				{
					map = RenderSystem.entities.get(m);
					Material mat;
					if (e.hasComponent(Material.class))
					{
						mat = (e.getComponent(Material.class));
					}
					else
					{
						mat = null;
					}

					if (map.get(mat) != null)
					{
						eList = map.get(mat);
					} else
					{
						eList = new ArrayList<>();
					}
					eList.add(e);
					map.put((mat), eList);
				} else
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
				}
				RenderSystem.entities.put((Mesh) c, map);
			});
	}
}
