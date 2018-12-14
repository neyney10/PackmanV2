package Maps;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import Coords.MyCoords;
import GUI.GameSpirit;
import GUI.MyFrame;
import GameObjects.GameObject;
import Geom.Point3D;

public class Map {
	private MapRange mapRange;
	private Rectangle screenRange;
	private Rectangle originalScreenRange;
	private Image background;
	double scaleFactorX, scaleFactorY, angleFactor;

	public Map() {
		// default values
		/*BOAZ SWITCH BETWEEN LATITUDE AND LONGITUDE
		 */
		double x1 = 35.2024f; // upper left corner
		double y1 = 32.1056f;
		double x2 = 35.2121f; // lower right corner
		double y2 = 32.1019f;
		 
		
//		double x1 = 32.1052f; // upper left corner
//		double y1 = 35.2024f;
//		double x2 = 32.1022f; // lower right corner
//		double y2 = 35.2121f;

		mapRange = new MapRange(x1,y1,x2,y2);
		//mapRange = new MapRange(new javafx.geometry.Point2D(32.102263, 35.202302), new javafx.geometry.Point2D(32.105827, 35.212061));
		screenRange = new Rectangle(0, 0, 1433, 642);
		originalScreenRange = new Rectangle(0, 0, 1433, 642);
		scaleFactorX = 1;
		scaleFactorY = 1;
		
		background = MyFrame.loadImage("GameData\\Ariel1.png");
		
		// DEVELOPER DEBUG
		angleFactor = new MyCoords().azimuth_elevation_dist(new Point3D(x1,y1,0), new Point3D(32.105827, 35.212061))[0];
		angleFactor = Math.toRadians(angleFactor);
		if(MyFrame.DEBUG) {
			System.out.print("[DEBUG][MAP] ");
			System.out.println(angleFactor);

//			// translate the axes
//			double originX, originY;
//			originX = 32.1055;
//			originY = 35.2023;
//			
//			double x ,y;
//			x = 32.1055 - originX;
//			y = 35.2023 - originY;
//			
//			// rotate whole system
//			x1 = -(x*Math.cos(angleFactor) - y*Math.sin(angleFactor)); // upper left corner
//			y1 = +(y*Math.cos(angleFactor) + x*Math.sin(angleFactor));
//		
//			x = 32.102336 - originX;
//			y = 35.212281 - originY;
//			
//			x2 = -(x*Math.cos(angleFactor) - y*Math.sin(angleFactor)); // lower right corner
//			y2 = +(y*Math.cos(angleFactor) + x*Math.sin(angleFactor));
//			
//			// retranslate the axes back
//			x1 += originX;
//			y1 += originY;
//			x2 += originX;
//			y2 += originY;
			
			//mapRange = new MapRange(x1,y1,x2,y2);

		}
	}
	
	/**
	 * NOT TESTED
	 * @param point2d
	 * @param point2d2
	 * @param angle
	 * @return
	 */
	public Point2D rotateAxis(javafx.geometry.Point2D point2d, javafx.geometry.Point2D point2d2, double angle) {
		// translate the axes
		
		angle = Math.toRadians(angle);
		Point trP = new Point();
		
		double x ,y;
		// rotate whole system
		x = point2d2.getX() - point2d.getX();
		y = point2d2.getY() - point2d.getY();
		
		trP.x = (int) (x*Math.cos(angle) - y*Math.sin(angle)); 
		trP.y = (int) +(y*Math.cos(angle) + x*Math.sin(angle));
		
		// retranslate the axes back
		trP.x += point2d.getX();
		trP.y += point2d.getY();
		
		return trP;
		
	}

	public double getX1() {
		return mapRange.getX1();
	}

	public double getWidth() {
		return mapRange.getWidth();
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
	public Point getLocationOnScreen(GameObject obj) {
			return getLocationOnScreen(obj.getPoint());
	}

	/**
	 * Get the geodetic/polar coordinate representing the screen position. <br>
	 * [Developer note] Note: not tested yet
	 * @param obj GameObject
	 * @return java.awt.Point, with (x,y) in Pixels represents the location on screen.
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
	 * [Developer Note] not tested, might not even work correctly.
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
	 * [Developer Note] not tested, might not even work correctly.
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
	 * [Developer Note] not tested, might not even work correctly.
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
	 * Note: Calculating Rescaled / non-original / non-Raw points. angle might be different with different screen sizes. <br>
	 * Directly Upwards = 180 degrees. <br>
	 * Directly on the Right = 90 degrees. <br>
	 * Directly Downwards = 0 degrees. <br>
	 * Directly on the Left = 270 degrees 
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return angle in degrees
	 */
	public double getAngle(Point p1, Point p2) {
		double deltaX, deltaY, angle;
		deltaX = p2.x-p1.x;
		deltaY = p2.y-p1.y;

		angle = Math.toDegrees(Math.atan2(deltaX, deltaY));
		return (angle < 0)? angle+360 : angle;
	}

	/**
	 * Get the angle between two points on screen (RAW pixels), in degrees. <br>
	 * Note: calculating RAW points.
	 * @param p1 - point in pixels
	 * @param p2 - point in pixels
	 * @return angle in degrees
	 */
	public double getAngleRaw(Point p1, Point p2) {
		double deltaX, deltaY, angle;
		deltaX = (p2.x-p1.x)/scaleFactorX;
		deltaY = (p2.y-p1.y)/scaleFactorY;

		angle = Math.toDegrees(Math.atan2(deltaX, deltaY));
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
	
	public Point transformByScale(int x, int y) {
		return new Point((int)(x/scaleFactorX), (int)(y/scaleFactorY));
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
