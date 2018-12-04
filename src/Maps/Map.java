package Maps;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import Coords.MyCoords;
import GameObjects.GameObject;
import Geom.Point3D;

public class Map {
    private MapRange mapRange;
    private Rectangle screenRange;
    private Image background;
    
    public Map() {
    	// default values
    	double x1 = 35.2024f; // upper left corner
    	double y1 = 32.1056f;
    	double x2 = 35.2121f; // lower right corner
    	double y2 = 32.1022f;

    	mapRange = new MapRange(x1,y1,x2,y2);
    	screenRange = new Rectangle(0, 0, 1000, 600);
    }
    
    public double getX1() {
    	return mapRange.getX1();
    }
    
    public double getWidth() {
    	return mapRange.getWidth();
    }
    
    /**
     * Get the place on screen in Pixels from the Geodetic/polar Point found on GameObject.
     * @param obj GameObject
     * @return java.awt.Point, with (x,y) in Pixels represents the location on screen.
     */
    public Point getLocationOnScreen(GameObject obj) {
    	int x,y;
    	Point3D objPoint = obj.getPoint();
    	x = (int) ((objPoint.x() - mapRange.x1) * (screenRange.getWidth() / mapRange.getWidth()));
    	y = (int) ((objPoint.y() - mapRange.y1) * (screenRange.getHeight() / mapRange.getHeight()));
    	
    	return new Point(x, y);
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
    	y = mapRange.y1 + p.getY() * (mapRange.getHeight()/screenRange.getHeight());
    	
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
     * Get the angle between two points on screen (pixels), in degrees. <br>
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
     * updating screenRange from current MyFrame size on screen.
     * @deprecated might be integrated inside MyFrame
     * @param width
     * @param height
     */
    public void updateScreenRange(int width, int height) {
    	screenRange.setSize(width, height);
    }

    
}
