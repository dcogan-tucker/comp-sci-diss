package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import component.Mesh;

/**
 * Class that contains file utility methods.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class FileUtils
{
	/**
	 * Private constructor to ensure the class isn't initiated unnecessarily.
	 */
	private FileUtils()
	{
		
	}
	
	/**
	 * Loads the contents of a given file into String format.
	 * 
	 * @param path The file to be loaded.
	 * @return The contents of the file as a String.
	 */
	public static String loadAsString(String path)
	{
		StringBuilder source = new StringBuilder();
		try
		{
			InputStream in = FileUtils.class.getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null)
			{
				source.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e)
		{
			System.err.println("Couldn't find the file at " + path);
			System.exit(-1);
		}
		return source.toString();
	}

	/**
	 * Loads a OBJ file into an array of a mesh and material component.
	 * 
	 * @param path The path of the OBJ file.
	 * @return A component array of length 2, with a mesh at index 0 and material at index 1.
	 */
	public static Mesh loadOBJFile(String name)
	{
		InputStream in = FileUtils.class.getResourceAsStream("/resources/objs/" + name);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		String[] currentLine;
		List<VertexData> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indicies = new ArrayList<>();
		try
		{
			while (true)
			{
				line = reader.readLine();
				if (line.startsWith("v "))
				{
					currentLine = line.split(" ");
					VertexData vertex = new VertexData(new Vector3f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]), (float) Float.valueOf(currentLine[3])));
					vertices.add(vertex);
				} 
				else if (line.startsWith("vt "))
				{
					currentLine = line.split(" ");
					Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]));
					textures.add(texture);
				} 
				else if (line.startsWith("vn "))
				{
					currentLine = line.split(" ");
					Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]), (float) Float.valueOf(currentLine[3]));
					normals.add(normal);
				} 
				else if (line.startsWith("f "))
				{
					break;
				}
			}
			while (line != null && line.startsWith("f "))
			{
				currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				processVertex(vertex1, vertices, textures, normals, indicies);
				processVertex(vertex2, vertices, textures, normals, indicies);
				processVertex(vertex3, vertices, textures, normals, indicies);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e)
		{
			System.err.println("Could not read file.");
		}
		return convertToMesh(vertices, indicies);
	}

	/**
	 * Processes the given vertex, adding texture and normal information if not already present.
	 * 
	 * @param vertex String array of the vertex position, texture and normal index data.
	 * @param vertices List of the object's vertices.
	 * @param textures List of the object's texture coordinates.
	 * @param normals List of the object's normals.
	 * @param indicies List of vertex indices.
	 */
	private static void processVertex(String[] vertex, List<VertexData> vertices, List<Vector2f> textures,
			List<Vector3f> normals, List<Integer> indicies)
	{
		int index = Integer.parseInt(vertex[0]) - 1;
		VertexData current = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		Vector2f texture = textures.get(textureIndex);
		Vector3f normal = normals.get(normalIndex);
		if (!current.isComplete())
		{
			current.setTexture(texture);
			current.setNormal(normal);
			current.setIndex(index);
			indicies.add(index);
		} 
		else
		{
			reProcessVertex(current, texture, normal, vertices, indicies);
		}
	}

	/**
	 * Process a vertex which is already complete with texture and normal data. 
	 * If texture and normal data is different from the already existing vertex
	 * create a new vertex adding to the vertex list. 
	 * 
	 * @param previous The already processed vertex.
	 * @param texture The texture coords of the new vertex.
	 * @param normal The normal of the new vertex.
	 * @param vertices The list of vertices.
	 * @param indicies The list of indicies.
	 */
	private static void reProcessVertex(VertexData previous, Vector2f texture, Vector3f normal, List<VertexData> vertices, List<Integer> indicies)
	{
		if (previous.hasSameTextureAndNormal(texture, normal))
		{
			indicies.add(previous.getIndex());
		} 
		else
		{
			VertexData newVertex = previous.getDuplicateVertex();
			if (newVertex != null)
			{
				reProcessVertex(newVertex, texture, normal, vertices, indicies);
			}
			else 
			{
				VertexData duplicate = new VertexData(previous.getPosition());
				duplicate.setIndex(vertices.size());
				duplicate.setTexture(texture);
				duplicate.setNormal(normal);
				previous.setDuplicateVertex(duplicate);
				vertices.add(duplicate);
				indicies.add(duplicate.getIndex());
			}
		}
	}
	
	/**
	 * Convert the list of vertices and indicies into a mesh.
	 * 
	 * @param verticesList The list of vertices of the object.
	 * @param indiciesList The vertices indexing.
	 * @return A mesh component, with the position, normal and texture data.
	 */
	private static Mesh convertToMesh(List<VertexData> verticesList, List<Integer> indiciesList)
	{
		float[] positions = new float[verticesList.size() * 3];
		float[] textures = new float[verticesList.size() * 2];
		float[] normals = new float[verticesList.size() * 3];
		
		int[] indicies = indiciesList.stream().mapToInt(i -> i).toArray();
		verticesList.forEach(v -> 
			{
				positions[3 * v.getIndex()] = v.getPosition().x;
				positions[3 * v.getIndex() + 1] = v.getPosition().y;
				positions[3 * v.getIndex() + 2] = v.getPosition().z;
				textures[2 * v.getIndex()] = v.getTexture().x;
				textures[2 * v.getIndex() + 1] = v.getTexture().y;
				normals[3 * v.getIndex()] = v.getNormal().x;
				normals[3 * v.getIndex() + 1] = v.getNormal().y;
				normals[3 * v.getIndex() + 2] = v.getNormal().z;
			});
		Mesh mesh = new Mesh();
		mesh.vertices = positions;
		mesh.normals = normals;
		mesh.indices = indicies;
		mesh.textures = textures;
		return mesh;
	}
}
