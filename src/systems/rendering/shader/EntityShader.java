package systems.rendering.shader;

import org.joml.Matrix4f;

/**
 * Shader program for entity's with methods to load a model, view and projection
 * matrix to the vertex shader.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class EntityShader extends ShaderProgram
{
	private static final String VERTEX_PATH = "/resources/shaders/vertexShader.glsl";
	private static final String FRAGMENT_PATH = "/resources/shaders/fragmentShader.glsl";

	private int locationModelMatrix;
	private int locationViewMatrix;
	private int locationProjectionMatrix;

	public EntityShader()
	{
		super(VERTEX_PATH, FRAGMENT_PATH);
	}

	@Override
	/**
	 * Gets the location of all uniform variables.
	 */
	protected void getAllUniformLocations()
	{
		locationModelMatrix = getUniformLocation("model");
		locationProjectionMatrix = getUniformLocation("projection");
		locationViewMatrix = getUniformLocation("view");
	}

	/**
	 * Sets the model matrix uniform value.
	 * 
	 * @param matrix The value to set.
	 */
	public void setModelMatrix(Matrix4f matrix)
	{
		setUniform(locationModelMatrix, matrix);
	}

	/**
	 * Sets the view matrix uniform value.
	 * 
	 * @param matrix The value to set.
	 */
	public void setViewMatrix(Matrix4f matrix)
	{
		setUniform(locationViewMatrix, matrix);
	}

	/**
	 * Sets the projection matrix uniform value.
	 * 
	 * @param matrix The value to set.
	 */
	public void setProjectionMatrix(Matrix4f matrix)
	{
		setUniform(locationProjectionMatrix, matrix);
	}
}
