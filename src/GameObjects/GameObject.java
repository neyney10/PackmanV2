package GameObjects;

import Geom.Point3D;

enum TYPE {P,F;}
public abstract class GameObject {
    private TYPE type;
    private Point3D point;

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
    }
}
