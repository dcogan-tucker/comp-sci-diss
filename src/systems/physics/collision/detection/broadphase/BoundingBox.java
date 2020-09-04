package systems.physics.collision.detection.broadphase;;

/**
 * Axis-aligned bounding box. The bounding box is used for collision detection
 * between two collidable objects. If two boxes are overlapping they are
 * considered to have collided.
 * 
 * @author Dominic Cogan-Tucker
 *
 */
public class BoundingBox
{
	/**
	 * The max x, y and z values of the bounding box.
	 */
	public float maxX, maxY, maxZ;
	
	/*
	 * The min x, y and z values of the bounding box.
	 */
	public float minX, minY, minZ;

	/**
	 * Generates a bounding box from an array of vertex values.
	 * 
	 * @param vertices The vertices of the object to generate a bounding box for.
	 */
	public BoundingBox(float[] vertices)
	{
		setBoxMinAndMaxValues(vertices);
	}

	/**
	 * Updates the bounding box values.
	 * 
	 * @param vertices The vertices of the object.
	 */
	protected void update(float[] vertices)
	{
		setBoxMinAndMaxValues(vertices);
	}
	
	/**
	 * Returns true if this bounding box is intersecting with the one give.
	 * 
	 * @param other The box to check against
	 * @return true, if this and other are intersecting, false otherwise.
	 */
	public boolean isIntersecting(BoundingBox other)
	{
		return (minX <= other.maxX && maxX >= other.minX) &&
				(minY <= other.maxY && maxY >= other.minY) &&
				(minZ <= other.maxZ && maxZ >= other.minZ);
	}

	/**
	 * Sets the bounding box minimum and maximum x, y and z values.
	 * 
	 * @param vertices The vertices of the object.
	 */
	private void setBoxMinAndMaxValues(float[] vertices)
	{
		maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
		minX = minY = minZ = Float.POSITIVE_INFINITY;

		for (int i = 0; i < vertices.length / 3; i++)
		{
			float x = vertices[3 * i];
			if (x > maxX)
			{
				maxX = x;
			} else if (x < minX)
			{
				minX = x;
			}

			float y = vertices[3 * i + 1];
			if (y > maxY)
			{
				maxY = y;
			} else if (y < minY)
			{
				minY = y;
			}

			float z = vertices[3 * i + 2];
			if (z > maxZ)
			{
				maxZ = z;
			} else if (z < minZ)
			{
				minZ = z;
			}
		}
	}
}
