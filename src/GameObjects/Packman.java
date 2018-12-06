package GameObjects;

import Geom.Point3D;

public class Packman extends GameObject {

    final public static int width = 41;
    final public static int height = 41;

    private double radius,speed;

    public Packman(double lat, double lon, double alt,int id, double radius, double speed) {
        setPoint(new Point3D(lat,lon,alt));
        setId(id);
        setType(TYPE.P);
        setRadius(radius);
        setSpeed(speed);
    }

    public double getRadius() {
        return radius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Packman: [ "+super.toString()+" , Speed: "+getSpeed()+" , Radius: "+getRadius()+" ]";
    }
}
