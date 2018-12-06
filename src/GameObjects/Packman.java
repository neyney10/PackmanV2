package GameObjects;

import java.awt.Image;

import GUI.MyFrame;
import Geom.Point3D;

public class Packman extends GameObject {

    private double radius,speed;
	private static final String packmanImagePath = "GameData\\PACMAN.png";
	private static final Image packmanImage = MyFrame.loadImage(packmanImagePath);

    public Packman(double lat, double lon, double alt,int id, double radius, double speed) {
        setPoint(new Point3D(lat,lon,alt));
        setId(id);
        setRadius(radius);
        setSpeed(speed);
        
        // initial values for this type
        setType(TYPE.P);
        setInitialWidth(41);
        setInitialHeight(41);
    	setSpirit(packmanImage);
    }
    
    /**
     *  creates an empty Object with default values
     */
    public Packman() {
    	this(0, 0, 0, (int)(Math.random()*432133), 1, 1);// TODO: do not make it random, count ID's instead
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
