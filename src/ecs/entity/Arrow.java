package ecs.entity;

import org.joml.Vector3f;

import ecs.component.Material;
import ecs.component.Mesh;
import utils.FileUtils;

public class Arrow extends GameObject
{
	private static Mesh mesh = FileUtils.loadOBJFile("/resources/arrow.obj");
	
	private static Material material = new Material();
	
	private static String texturePath = "src/resources/red.png";
	
	private Arrow(Vector3f pos, Vector3f rot, float scale)
	{
		super(mesh, material, pos, rot, new Vector3f(scale), 0);
	}
	
	public static Arrow create(Vector3f pos, Vector3f rot)
	{
		return new Arrow(pos, rot, 0.2f);
	}
	
	public static Material initMaterial()
	{
		material.texturePath = texturePath;
		return material;
	}
}
