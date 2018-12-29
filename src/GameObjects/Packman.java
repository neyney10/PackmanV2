package GameObjects;

import GUI.ImageFactory;
import Geom.Point3D;
import Path.Path;

/**
 * Packman object of type GameObject with image of pacman, starting width and height of 30px each. <br>
 * Its position represented in geodetic coordinates Lat,Lon and alt, and also have speed and eating radius. <br>
 * GameObject is from type of BasicGameSpirit for the ability of being converted into a GameSpirit for use
 * of drawing on screen in X,Y pixel coordinates. <br>
 * please use a BasicGameSpiritFactory for creating GameSpirits from an GameObject. <br>
 * default sprite image location (relative): "GameData\\PACMAN.png"
 */
public class Packman extends GameObject implements Cloneable {
	
	// eating radius, movment speed (in meters) and oreintation in degrees.
	private double radius,speed, orientation;
	
	// the imagePath of the pacman game sprite.
	private static final String packmanImagePath = "GameData\\PACMAN.png";
	// the Path of eating fruits, can be calculated by the ShortestPathAlgo.
	private Path path;
	
	/**
	 * [Constructor] <br>
	 * Creating a new pacman with new values of Position (Lat, lon and alt),
	 * id (Preferbly to be generated by Game object with Game.generateID()),
	 * be careful as the Game object using SET for its data structure, and comparing by ID, if
	 * two game objects have the same id, one of them will be deleted. <br>
	 * Movement speed (in meters), and eating radius (in meters).
	 * @param lat - latitude.
     * @param lon - longitude.
     * @param alt - altitude
     * @param id - ID.
	 * @param speed - speed in meters.
	 * @param radius - eating radius in meters
	 */
    public Packman(double lat, double lon, double alt,int id, double speed, double radius) {
        setPoint(new Point3D(lat,lon,alt));
        setId(id);
        setRadius(radius);
        setSpeed(speed);
        
        // initial values for this type
        setType(TYPE.P);
        setInitialWidth(31);
        setInitialHeight(31);
    	setSpirit(ImageFactory.getImageFromDisk(packmanImagePath));
    }
    
    /**
	 * [Constructor]
     *  creates an empty Object with default values, must supply id from Game.generateID.
	 *  lat = 0, lon = 0, alt = 0, speed = 1, radius = 1.
     */
    public Packman(int id) {
    	this(0, 0, 0, id, 1, 1);
    }

	/**
	 * get the eating radius. (meters)
	 * @return the eating radius in double.
	 */
    public double getRadius() {
        return radius;
    }

	/**
	 * get the speed (meters).
	 * @return this pacman's speed in double (meters)
	 */
    public double getSpeed() {
        return speed;
    }

	/**
	 * set a new eating radius in meters.
	 * @param radius the new radius
	 */
    public void setRadius(double radius) {
        this.radius = radius;
    }

	/**
	 * set a new speed value in meters.
	 * @param speed the new movement speed.
	 */
    public void setSpeed(double speed) {
        this.speed = speed;
	}
	
	/**
	 * get the angle of this pacman.
	 * @param angle in degrees
	 */
	public void getOrientation(double angle) {
		this.orientation = angle;
	}

	/**
	 * @param orientation the orientation to set in angle (degrees)
	 */
	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

    @Override
    public String toString() {
        return "Packman: [ "+super.toString()+" , Speed: "+getSpeed()+" , Radius: "+getRadius()+" ]";
    }

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(Path path) {
		this.path = path;
		this.path.setID(id);
	}
	
	
	/**
	 * Cloning this Packman and returning a new Packman with same values.
	 * @return Packman 
	 */
	public Packman clone() {
		Packman p = new Packman(point.x(),point.y(),point.z(),id,radius,speed);
		p.setSpirit(getSpirit());
		return p;
	}


}
