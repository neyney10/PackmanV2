package GameObjects;

import Geom.Point3D;

import java.awt.*;

public abstract class GameObject implements Comparable<GameObject>{
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
    public int compareTo(GameObject o) {
        if(o.type == this.type) {
            return o.id - this.id;
        } else if(o.type == TYPE.P) {
            return -1;
        } else return 1;
    }

    @Override
    public String toString() {
        return "Point: [ Lat: "+point.x()+" , Lon: "+point.y()+" , Alt: "+point.z()+" ] , Id: "+getId()+" , Type: "+getType();
    }
}
