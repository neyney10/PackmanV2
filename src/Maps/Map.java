package Maps;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import Coords.MyCoords;
import GUI.GameSpirit;
import GameObjects.BasicGameSpirit;
import Geom.Point3D;

/**
 * an abstract Map class which defines the basic properties of map such as image, current screen range, Geodetic Map range,
 * and original screen range.
 * @author Neyney / Ofek Bader.
 */
public abstract class Map {
	protected MapRange mapRange;
	protected Rectangle screenRange;
	protected Rectangle originalScreenRange;
	protected Image background;
	double scaleFactorX, scaleFactorY, angleFactor;

	public Map() {
		scaleFactorX = 1;
		scaleFactorY = 1;
		originalScreenRange = new Rectangle(0, 0, 1433, 642);
	}
	
	/**
	 * Creating a new map object with
	 * @param screenWidth
	 * @param screenHeight
	 */
	public Map(int screenWidth, int screenHeight) {
		originalScreenRange = new Rectangle(0, 0, screenWidth, screenHeight);

	}
	
	/**
	 * Rotate position of Point by given angle relative to an origin point
	 * @param point2d origin point
	 * @param point2d2 the point to rotate
	 * @param angle how much to rotate in Degrees!
	 * @return a rotated point Point
	 */
	public Point rotateAxis(Point origin, Point point2d2, double angle) {
		// translate the axes
		
		angle = Math.toRadians(angle);
		Point trP = new Point();
		
		double x ,y;
		// rotate whole system
		x = point2d2.getX() - origin.getX();
		y = point2d2.getY() - origin.getY();
		
		trP.x = (int) (x*Math.cos(angle) - y*Math.sin(angle)); 
		trP.y = (int) +(y*Math.cos(angle) + x*Math.sin(angle));
		
		// retranslate the axes back
		trP.x += origin.getX();
		trP.y += origin.getY();
		
		return trP;
		
	}

	/**
	 * get the geodetic latitude of the top left corner Point.
	 * @return latitude
	 */
	public double getX1() {
		return mapRange.getX1();
	}

		/**
	 * get the geodetic latitude of the bottom right corner Point.
	 * @return latitude
	 */
	public double getX2() {
		return mapRange.getX2();
	}

	/** 
	* get the geodetic longitude of the top left corner Point.
	* @return latitude
	*/
   public double getY1() {
	   return mapRange.getY1();
   }

	   /**
	* get the geodetic longitude of the bottom right corner Point.
	* @return latitude
	*/
   public double getY2() {
	   return mapRange.getY2();
   }

   /**
	* get the Delta latitude of the map.(width)
	@param double the delta Latitude
    */
	public double getWidth() {
		return mapRange.getWidth();
	}
	
	/**
	 * get the current Screen/window width.
	 * @return width in pixels.
	 */
	public int getScreenWidth() {
		return (int) screenRange.getWidth();
	}
	
	/**
	 * get the current Screen/Window height
	 * @return height - in pixels
	 */
	public int getScreenHeight() {
		return (int) screenRange.getHeight();
	}
	
	/**
	 * get the original / starting width of the window/screen.
	 * @return int the original width in pixels.
	 */
	public int getOriginalScreenWidth() {
		return (int) originalScreenRange.getWidth();
	}

	/**
	 * get the original / starting height of the window/screen.
	 * @return int the original height in pixels.
	 */
	public int getOriginalScreenHeight() {
		return (int) originalScreenRange.getHeight();
	}
	
	
	/**
	 * Get the place on screen in Pixels from the Geodetic/polar Point found on GameObject.
	 * @param Point3D p3d
	 * @return java.awt.Point, with (x,y) in Pixels represents the location on screen.
	 */
	public Point getLocationOnScreen(Point3D p3d) {
		double x,y;

		x =  ((p3d.x() - mapRange.x1)) * (screenRange.getWidth() / mapRange.getWidth());
		y =  ((-p3d.y() + mapRange.y1)) * (screenRange.getHeight() / mapRange.getHeight());
		//y *= -1; // FIX

		return new Point((int)x, (int)y);
	}

	/**
	 * Get the place on screen in Pixels from the Geodetic/polar Point found on GameObject.
	 * @param obj GameObject
	 * @return java.awt.Point, with (x,y) in Pixels represents the location on screen.
	 */
	public Point getLocationOnScreen(BasicGameSpirit obj) {
			return getLocationOnScreen(obj.getPoint());
	}

	/**
	 * Get the geodetic/polar coordinate representing the screen position. <br>
	 * @param obj GameObject
	 * @return Point3D Geodetic point.
	 */
	public Point3D getLocationFromScreen(Point p) {
		double x,y;
		x = mapRange.x1 + p.getX() * (mapRange.getWidth()/screenRange.getWidth());
		y = -mapRange.y1 + p.getY() * (mapRange.getHeight()/screenRange.getHeight());
		y *=-1; // FIX
		return new Point3D(x,y,0);
	}

	/**
	 * Get distance between two points on screen (pixels) in METERES! <br>
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return distance in METERS.
	 */
	public double getDistance(Point p1, Point p2) {
		Point3D p3d1 = getLocationFromScreen(p1);
		Point3D p3d2 = getLocationFromScreen(p2);
		return new MyCoords().distance3d(p3d1, p3d2);

	}

	/**
	 * Get distance between two points on screen (pixels) in PIXELS! <br>
	 * Note: Calculating the distance by current scaling, its not RAW distance and might change by screen size <br>
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return distance in SCREEN PIXELS.
	 */
	public double getDistanceByPixel(Point p1, Point p2) {
		double deltaX, deltaY;
		deltaX = p2.x-p1.x;
		deltaY = p2.y-p1.y;
		return Math.sqrt(Math.abs((deltaX*deltaX)+(deltaY*deltaY)));
	}
	
	/**
	 * Get distance between two points on screen (pixels) in PIXELS! <br>
	 * Note: Calculating the distance by ORIGINAL scaling, this is RAW data!
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return distance in SCREEN PIXELS.
	 */
	public double getDistanceByPixelRaw(Point p1, Point p2) {
		double deltaX, deltaY;
		deltaX = (p2.x-p1.x)/scaleFactorX;
		deltaY = (p2.y-p1.y)/scaleFactorY;
		return Math.sqrt(Math.abs((deltaX*deltaX)+(deltaY*deltaY)));
	}

	/**
	 * Get the angle between two points on screen (pixels), in degrees. <br>
	 * Note: Calculating Rescaled / non-original / non-Raw points. angle might be different with different screen sizes. unless
	 * given a Raw points as arguments.<br>
	 * the angle is in degrees and in clockwise <br>
	 * Directly Upwards = 0 degrees. <br>
	 * Directly on the Right = 90 degrees. <br>
	 * Directly Downwards = 180 degrees. <br>
	 * Directly on the Left = 270 degrees 
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return angle in degrees
	 */
	public double getAngle(Point p1, Point p2) {
		double deltaX, deltaY, angle;
		deltaX = p2.x-p1.x;
		deltaY = p2.y-p1.y;

		angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
		return (angle < 0)? angle+360 : angle;
	}

	/**
	 * Get the angle between two points on screen (RAW pixels), in degrees. <br>
	 * Note: calculating RAW points. <br>
	 * First the function reverting the points into a RAW-pixel points,
	 * if Raw pixel points are passed as argument, the function would still try to revert the point to RAW
	 * as it doesn't recognize which is RAW and which isn't, please provide only Non-RAW pixel coordinates.
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return angle in degrees
	 */
	public double getAngleRaw(Point p1, Point p2) {
		double deltaX, deltaY, angle;
		deltaX = (p2.x-p1.x)/scaleFactorX;
		deltaY = (p2.y-p1.y)/scaleFactorY;
		
		angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
		return (angle < 0)? angle+360 : angle;
	}
	/**
	 * updating screenRange from current MyFrame size on screen.
	 * might be integrated inside MyFrame
	 * @param width
	 * @param height
	 */
	public void updateScreenRange(int width, int height) {
		screenRange.setSize(width, height);

		scaleFactorX = ((double)width)/originalScreenRange.getWidth();
		scaleFactorY = ((double)height)/originalScreenRange.getHeight();
	}

	/**
	 * Set new position to GameSpirit in pixels on screen, by using manipulation of current ScreenSize and MapSize
	 * to get the optimal result, the function also get into consideration scaling factors of the screen.
	 * @param obj the GameSpirit object to update it's position.
	 * @param p a new location point to move to
	 */
	public void updateLocationOnScreen(GameSpirit obj, Point p) {
		updateLocationOnScreen(obj, p.x, p.y);
	}

	/**
	 * updating the GameSpirit with the current Scale Factors in effect By using manipulation of current ScreenSize and MapSize
	 * to get the optimal result, the function also get into consideration scaling factors of the screen.
	 * @param obj the GameSpirit object to update it's position
	 */
	public void updateLocationOnScreen(GameSpirit obj) {
		obj.setBounds(
				(int)(obj.getStartX()*scaleFactorX),
				(int)(obj.getStartY()*scaleFactorY),
				(int)(obj.getStartWidth()*scaleFactorX),
				(int)(obj.getStartHeight()*scaleFactorY)
				);
	}
	
	/**
	 * Set new position to GameSpirit in pixels on screen, by using manipulation of current ScreenSize and MapSize
	 * to get the optimal result, the function also get into consideration scaling factors of the screen.
	 * @param obj the GameSpirit object to update it's position.
	 * @param x a new X location point to move to
	 * @param y a new Y location point to move to
	 */
	public void updateLocationOnScreen(GameSpirit obj, int x, int y) {
		obj.updateLocation(x, y);
		updateLocationOnScreen(obj);
	}
	
	/**
	 * Moving the GameSpirit object by x and y, with consideration of scaling factors of the screen.
	 * @param obj the GameSpirit object to update it's position
	 * @param x amount of pixels to move to the right (positive) or left (negative)
	 * @param y amount of pixels to move to downwards (positive) or upwards (negative)
	 */
	public void moveLocationByPixels(GameSpirit obj, int x, int y) {
		obj.moveByPixel(x, y);
		updateLocationOnScreen(obj);

	}

	/**
	 * @return the scaleFactorX
	 */
	public double getScaleFactorX() {
		return scaleFactorX;
	}

	/**
	 * @param scaleFactorX the scaleFactorX to set
	 */
	public void setScaleFactorX(double scaleFactorX) {
		this.scaleFactorX = scaleFactorX;
	}

	/**
	 * @return the scaleFactorY
	 */
	public double getScaleFactorY() {
		return scaleFactorY;
	}

	/**
	 * @param scaleFactorY the scaleFactorY to set
	 */
	public void setScaleFactorY(double scaleFactorY) {
		this.scaleFactorY = scaleFactorY;
	}
	
	/**
	 * Returns RAW position from currently modified by scale (reverting scale factors)
	 * @return a RAW point X,Y
	 */
	public Point transformByScale(int x, int y) {
		return new Point((int)(x/scaleFactorX), (int)(y/scaleFactorY));
	}

	/**
	 * Returns RAW position from currently modified by scale (reverting scale factors)
	 * @return a RAW point X,Y
	 */
	public Point transformByScale(Point point) {
		return transformByScale(point.x,point.y);
	}

	/**
	 * @return the background
	 */
	public Image getBackground() {
		return background;
	}

	/**
	 * @param background the background to set
	 */
	public void setBackground(Image background) {
		this.background = background;
	}
	

	


}
