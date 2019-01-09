package Algorithms;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import GameObjects.Box;
import Maps.Map;

/**
 * A logic calculations class of intersection
 * between two lines or line and a box (rectangle)
 * @author Neyney
 *
 */
public class Intersect {

    private Map map;

    /**
     * [Constructor] <br>
     * create a new Intersect calculation object.
     * @param map - for converting geodetic coordinates to pixels for various calculations.
     */
    public Intersect(Map map) {
        this.map = map;
    }

    /**
     * Returns true if two lines intersect.
     * @param Line2D line1
     * @param Line2D line2
     * @return true if the two lines intersect, false otherwise. 
     */
    public boolean areLinesIntersect(Line2D line1, Line2D line2) {
        return line1.intersectsLine(line2);
    }

    /**
     * Checks if a line collides with a given box.
     * @param line
     * @param box
     * @return true if the line collides with the box, false otherwise.
     */
    public boolean isLineIntersectsWithBox(Line2D line, Box box) {
        // convert Polar coordinates of box into two X,Y pixel coordinates on screen.
        Point topPoint = map.getLocationOnScreen(box.getMin());
        Point botPoint = map.getLocationOnScreen(box.getMax());

        // create a awt.geom Rectangle for the "intersects" function.
        double deltaX = Math.abs(topPoint.getX() - botPoint.getX());
        double deltaY = Math.abs(topPoint.getY() - botPoint.getY());
        
        Rectangle2D r = new Rectangle2D.Double(topPoint.getX(), topPoint.getY(), deltaX, deltaY);

        return r.intersectsLine(line);
    }

    /**
     * Checks if the a specific point (2d) is inside a box (rectangle 2d).
     * @param point - Point to check
     * @param box - the rectangle to check
     * @return true if the point is inside the box, false otherwise.
     */
    public boolean isPointInsideBox(Point point, Box box) {
        // convert Polar coordinates of box into two X,Y pixel coordinates on screen.
        Point topPoint = map.getLocationOnScreen(box.getMin());
        Point botPoint = map.getLocationOnScreen(box.getMax());

        // create a awt.geom Rectangle for the "intersects" function.
        double deltaX = Math.abs(topPoint.getX() - botPoint.getX());
        double deltaY = Math.abs(topPoint.getY() - botPoint.getY());

        Rectangle2D r = new Rectangle2D.Double(topPoint.getX(), topPoint.getY(), deltaX, deltaY);

        return r.contains(point);
    }


    /**
     * tests if a line intersects with one of the boxes in the given list of boxes.
     * @param line - the line to check intersection.
     * @param boxes - the array of boxes to check if the line intersects with.
     * @return true if the line intersects with one of them, false otherwise
     */
    public boolean isLineIntersectsWithBoxes(Line2D line, Box[] boxes) {
        for(Box box : boxes)
            if(isLineIntersectsWithBox(line, box))
                return true;
        return false;
    }

}