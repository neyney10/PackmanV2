package GameObjects;

import Geom.Point3D;

import java.awt.Image;

public abstract class GameObject {
    private TYPE type;
    private Point3D point;
    private int id;
    private Image spirit;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getSpirit() {
        return spirit;
    }

    public void setSpirit(Image spirit) {
        this.spirit = spirit;
    }

    @Override
    public String toString() {
        return "Point: [ Lat: "+point.x()+" , Lon: "+point.y()+" , Alt: "+point.z()+" ] , Id: "+getId()+" , Type: "+getType();
    }
}
