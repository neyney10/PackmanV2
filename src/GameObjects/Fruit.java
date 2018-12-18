package GameObjects;

import GUI.ImageFactory;
import Geom.Point3D;
/**
 * Fruit object of type GameObject with image of Fruit, starting width and height of 250px each. <br>
 * Its position represented in geodetic coordinates Lat,Lon and alt. <br>
 * GameObject is from type of BasicGameSpirit for the ability of being converted into a GameSpirit for use
 * of drawing on screen in X,Y pixel coordinates. <br>
 * please use a BasicGameSpiritFactory for creating GameSpirits from an GameObject. <br>
 * default sprite image location (relative): "GameData\\fruit.png"
 */
public class Fruit extends GameObject {

    // default sprite path image
	private static final String fruitImagePath ="GameData\\fruit.png";

    /**
     * [Constructor]
     * Creates a new Fruit with given values.
     * @param lat - latitude.
     * @param lon - longitude.
     * @param alt - altitude
     * @param id - ID.
     */
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
    public Fruit(int id) {
    	this(0, 0 ,0, id);
    }



    @Override
    public String toString() {
        return "Fruit: [ "+super.toString()+" ]";
    }
}
