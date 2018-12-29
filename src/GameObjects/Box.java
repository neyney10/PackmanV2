package GameObjects;

import Geom.Point3D;

public class Box extends GameObject implements Cloneable{
    Point3D min,max;
    int id;
    int weight;
    public Box(Point3D p1,Point3D p2){
    min = new Point3D(Math.min(p1.x(),p2.x()),Math.min(p1.y(),p2.y()),Math.min(p1.z(),p2.z()));
    max = new Point3D(Math.max(p1.x(),p2.x()),Math.max(p1.y(),p2.y()),Math.max(p1.z(),p2.z()));
    weight = 1;
    }
}
