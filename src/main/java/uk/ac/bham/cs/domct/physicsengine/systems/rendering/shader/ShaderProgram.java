package uk.ac.bham.cs.domct.physicsengine.systems.rendering.shader;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import uk.ac.bham.cs.domct.physicsengine.utils.FileUtils;

/**
 * Abstract class used as baseline to create a shader program, with methods to
 * bind, unbind, delete and load uniform variables of different types.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public abstract class ShaderProgram
{

	private String vertexFile, fragmentFile;
	private int vertexID, fragmentID, programID;
	private boolean isBound = false;

	/**
	 * Constructs a ShaderProgram object with the file location of it's vertex and
	 * fragment shader files.
	 * 
	 * @param vertexFile The file path location of the vertex shader.
	 * @param fragmentFile The file path location of the fragment shader.
	 */
	protected ShaderProgram(String vertexFile, String fragmentFile)
	{
		this.vertexFile = FileUtils.loadAsString(vertexFile);
		this.fragmentFile = FileUtils.loadAsString(fragmentFile);
	}

	/**
	 * Assigns a id to the program, vertex shader and fragment shader. Compiling and
	 * attaching the shaders to the program.
	 */
	public void create()
	{
		programID = glCreateProgram();

		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexFile);
		glCompileShader(vertexID);
		if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Vertex Shader: " + glGetShaderInfoLog(vertexID));
			return;
		}

		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentFile);
		glCompileShader(fragmentID);
		if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println("Fragment Shader: " + glGetShaderInfoLog(fragmentID));
			return;
		}

		glAttachShader(programID, vertexID);
		glAttachShader(programID, fragmentID);

		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
		{
			System.err.println("Program Linking: " + glGetProgramInfoLog(programID));
			return;
		}
		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE)
		{
			System.err.println("Program Validation: " + glGetProgramInfoLog(programID));
			return;
		}

		getAllUniformLocations();
		deleteShaders();
	}

	/**
	 * Binds this program to the current rendering process.
	 */
	public void bind()
	{
		glUseProgram(programID);
		isBound = true;
	}

	/**
	 * Unbinds this program from the current rendering process.
	 */
	public void unbind()
	{
		glUseProgram(0);
		isBound = false;
	}
	
	/**
	 * Returns true if this shader is currently bound.
	 * 
	 * @return true if this shader is currently bound.
	 */
	public boolean isBound()
	{
		return isBound;
	}

	/**
	 * Detaches the shaders from the program and deletes them to free memory.
	 */
	private void deleteShaders()
	{
		glDetachShader(programID, vertexID);
		glDetachShader(programID, fragmentID);
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
	}

	/**
	 * Deletes the shader program from memory.
	 */
	public void close()
	{
		glDeleteProgram(programID);
	}

	/**
	 * Gets the location of all uniform variables.
	 */
	protected abstract void getAllUniformLocations();

	/**
	 * Gets the location of a uniform variable.
	 * 
	 * @param name The name of the uniform to locate.
	 * @return The location of the uniform variable.
	 */
	protected int getUniformLocation(String name)
	{
		return glGetUniformLocation(programID, name);
	}

	/**
	 * Sets the value of a given uniform variable.
	 * 
	 * @param location  The name of the given uniform.
	 * @param value The value to set as a float.
	 */
	protected void setUniform(int location, float value)
	{
		glUniform1f(location, value);
	}

	/**
	 * Sets the value of a given uniform variable.
	 * 
	 * @param location  The name of the given uniform.
	 * @param value The value to set as an int.
	 */
	protected void setUniform(int location, int value)
	{
		glUniform1i(location, value);
	}

	/**
	 * Sets the value of a given uniform variable.
	 * 
	 * @param location  The name of the given uniform.
	 * @param value The value to set as a boolean.
	 */
	protected void setUniform(int location, boolean value)
	{
		glUniform1i(location, value ? 1 : 0);
	}

	/**
	 * Sets the value of a given uniform variable.
	 * 
	 * @param location  The name of the given uniform.
	 * @param value The value to set as a Vector2f.
	 */
	protected void setUniform(int location, Vector2f value)
	{
		glUniform2f(location, value.x, value.y);
	}

	/**
	 * Sets the value of a given uniform variable.
	 * 
	 * @param location  The name of the given uniform.
	 * @param value The value to set as a Vector3f.
	 */
	protected void setUniform(int location, Vector3f value)
	{
		glUniform3f(location, value.x, value.y, value.z);
	}

	/**
	 * Sets the value of a given uniform variable.
	 * 
	 * @param location  The name of the given uniform.
	 * @param value The value to set as a Matrix4f.
	 */
	protected void setUniform(int location, Matrix4f value)
	{
		FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);
		value.get(matrixBuffer);
		glUniformMatrix4fv(location, false, matrixBuffer);
	}
}
