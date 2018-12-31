package GameObjects;

import Geom.Point3D;

public class Box extends GameObject implements Cloneable {
    Point3D max;
    int weight;


    /**
     * [Constructor] <br>
     * Creats a new Box gameobject.
     * @param p1 the first point
     * @param p2 the second point
     */
    public Box(Point3D p1, Point3D p2, int id) {
        setPoints(p1, p2);
        setId(id);
        weight = 1;
        setType(TYPE.B);
    }

    public Box(int id) {
        this( new Point3D(0,0,0), new Point3D(0,0,0), id);
    }


    public Point3D getMin() {
        return super.getPoint();
    }

    public void setMin(Point3D min) {
        super.setPoint(min);
    }

    public Point3D getMax() {
        return this.max;
    }

    public void setMax(Point3D max) {
        this.max = max;
    }

    public void setPoints(Point3D p1, Point3D p2) {
        setPoint(new Point3D(Math.min(p1.x(), p2.x()), Math.max(p1.y(), p2.y()), Math.min(p1.z(), p2.z())));
        max = new Point3D(Math.max(p1.x(), p2.x()), Math.min(p1.y(), p2.y()), Math.max(p1.z(), p2.z()));
    }

    public Box clone() {
        return new Box(getPoint(), getMax(), getId());
    }




}
