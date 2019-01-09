package GameObjects;

import Geom.Point3D;

/**
 * A box GameObject, given an upperLeft and LowerRight points to form it.
 * @author neyne
 *
 */
public class Box extends GameObject implements Cloneable {
    Point3D max;
    int weight;


    /**
     * [Constructor] <br>
     * Creates a new Box gameobject.
     * @param p1 the first point
     * @param p2 the second point
     * @param id - object's id
     */
    public Box(Point3D p1, Point3D p2, int id) {
        setPoints(p1, p2);
        setId(id);
        weight = 1;
        setType(TYPE.B);
    }

    /**
     * [Constructor] <br>
     * Creates a new Box gameobject, sets default values of 0,0,0
     * @param id - object's id
     */
    public Box(int id) {
        this( new Point3D(0,0,0), new Point3D(0,0,0), id);
    }


    /**
     * Get the minimun point (lowerRight) of the box
     * @return Point3D lower right point.
     */
    public Point3D getMin() {
        return super.getPoint();
    }

    /**
     * Sets the lower right point
     * @param min - point3d to set.
     */
    public void setMin(Point3D min) {
        super.setPoint(min);
    }

    /**
     * Get the Upper left point
     * @return Point3D upper left (max) point.
     */
    public Point3D getMax() {
        return this.max;
    }

    /**
     * set the upper left point
     * @param max - point3d to set.
     */
    public void setMax(Point3D max) {
        this.max = max;
    }

    /**
     * Set both points of Minimum (lower right) and Maximum (Upper left) to this box.
     * @param p1 - upper left
     * @param p2 - lower right
     */
    public void setPoints(Point3D p1, Point3D p2) {
        setPoint(new Point3D(Math.min(p1.x(), p2.x()), Math.max(p1.y(), p2.y()), Math.min(p1.z(), p2.z())));
        max = new Point3D(Math.max(p1.x(), p2.x()), Math.min(p1.y(), p2.y()), Math.max(p1.z(), p2.z()));
    }

    /**
     * Clone this object, retrieve a new box instances with same values.
     */
    public Box clone() {
        return new Box(getPoint(), getMax(), getId());
    }




}
