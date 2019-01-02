package Algorithms;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import GameObjects.Box;
import Maps.Map;

public class Intersect {

    Map map;

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
     * @param Line2D line1
     * @param Box box
     * @param Map map - for coordinates conversion of Box
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
     * @param 
     */
    public boolean isLineIntersectsWithBoxes(Line2D line, Box[] boxes) {
        for(Box box : boxes)
            if(isLineIntersectsWithBox(line, box))
                return true;
        return false;
    }

}