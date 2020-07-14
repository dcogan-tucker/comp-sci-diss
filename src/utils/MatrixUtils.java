package utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A class of static matrix utility functions.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public final class MatrixUtils
{
	
	/**
	 * Private constructor to ensure the class isn't initiated unnecessarily.
	 */
	private MatrixUtils()
	{
		
	}
	
	/**
	 * Creates a transformation matrix that can transform the coordinates of an object. 
	 * @param translation The translation transformation.
	 * @param rotation The rotational transformation.
	 * @param scale The scale transformation.
	 * @return The transformation matrix.
	 */
	public static Matrix4f transformMatrix(Vector3f translation, Vector3f rotation, Vector3f scale)
	{		
		Matrix4f matrix = new Matrix4f().identity()
				.translate(translation)
				.rotateX((float) Math.toRadians(rotation.x))
				.rotateY((float) Math.toRadians(rotation.y))
				.rotateZ((float) Math.toRadians(rotation.z))
				.scale(scale);
		return matrix;
	}
	
	/**
	 * Creates a projection matrix.
	 * @param aspect The aspect ratio of the window.
	 * @param fov The desired fov.
	 * @param far The desired far plane (max view distance).
	 * @param near The desired near plane (min view distance).
	 * @return The projection matrix.
	 */
	public static Matrix4f projectionMatrix(float aspect, float fov, float near, float far) 
	{
		float yScale = (float) (1f / Math.tan(Math.toRadians(fov / 2f)));
		float xScale = yScale / aspect;
		float frustumLength = far - near;
		
		Matrix4f matrix = new Matrix4f()
				.m00(xScale)
				.m11(yScale)
				.m22(-(far + near) / frustumLength)
				.m23(-1)
				.m32(-(2 * far * near) / frustumLength)
				.m33(0);
		return matrix;
	}
	
	/**
	 * Creates a view matrix.
	 * @param translation The translation of the view.
	 * @param rotation The rotation of the view.
	 * @return The view matrix.
	 */
	public static Matrix4f viewMatrix(Vector3f translation, Vector3f rotation)
	{
		Matrix4f matrix = new Matrix4f().identity()
				.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
				.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
				.translate(-translation.x, -translation.y, -translation.z);
		return matrix;
	}
}
