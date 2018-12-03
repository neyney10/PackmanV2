package GameObjects;

import Geom.Point3D;

public class Fruit extends GameObject {
    public Fruit(double lat, double lon, double alt, TYPE type, double radius, double speed) {
        setPoint(new Point3D(lat,lon,alt));
        setType(type);
    }
}
