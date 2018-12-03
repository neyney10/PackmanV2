package GameObjects;

import Geom.Point3D;

public class Packman extends GameObject {
    private double radius,speed;

    public Packman(double lat, double lon, double alt, TYPE type, double radius, double speed) {
        setPoint(new Point3D(lat,lon,alt));
        setType(type);
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
}
