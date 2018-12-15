package GameObjects;

import java.awt.Image;

import GUI.ImageFactory;
import GUI.MyFrame;
import Geom.Point3D;

public class Fruit extends GameObject {

	private static final String fruitImagePath ="GameData\\fruit.png";


	
    public Fruit(double lat, double lon, double alt,int id) {
        setPoint(new Point3D(lat,lon,alt));
        setId(id);
        
        // set initial values for this type
        setType(TYPE.F);
        setInitialWidth(25);
        setInitialHeight(25);
    	setSpirit(ImageFactory.getImageFromDisk(fruitImagePath));
    }
    
    /**
     * create an empty object with default values.
     */
    public Fruit() {
    	this(0, 0 ,0, (int)(Math.random()*334143)); //TODO: NOT RANDOM
    }

    @Override
    public String toString() {
        return "Fruit: [ "+super.toString()+" ]";
    }
}
