package GameObjects;

import java.awt.Image;

import Geom.Point3D;

/**
 * Interface for giving basic information/data for the implementing object to be instantiated as a
 * GameSpirit object. <br>
 * Information such as starting values of width/height and image.
 * @author Ofek Bader
 *
 */
public interface BasicGameSpirit {
	
	/**
	 * get a copy of current Spirit image.
	 * @return Image of the current Spirit
	 */
	public Image getSpirit();
	
	/**
	 * Set new Spirit image.
	 * @param spirit image to set.
	 */
	public void setSpirit(Image spirit);
	
	/**
	 * get the starting width value (size of the object / image)
	 * @return the starting width value (in pixels).
	 */
	public int getInitialWidth();
	
	/**
	 * get the starting height value (size of the object / image)
	 * @return the starting height value (in pixels).
	 */
	public int getInitialHeight();
	
	/**
	 * Set starting width value (size of object / image)
	 * @param width
	 */
	public void setInitialWidth(int width);
	
	/**
	 * Set starting height value (size of object / image)
	 * @param height
	 */
	public void setInitialHeight(int height);
	
	/**
	 * Set starting width & height values (size of object / image)
	 * @param width
	 * @param height
	 */
	public void setInitialSize(int width, int height);
	
	/**
	 * returns the 3D position.
	 * @return returns a 3D point "Point3D"
	 */
	public Point3D getPoint();
}
