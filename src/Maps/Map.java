package Maps;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

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
    
    public void updateScreenRange(int width, int height) {
    	screenRange.setSize(width, height);
    }

    
}
