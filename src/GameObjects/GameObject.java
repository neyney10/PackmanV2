package GameObjects;

import Geom.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * abstract GameObject object of type BaiscGameSpirit.. <br>
 * Its position represented in geodetic coordinates Lat,Lon and alt, and also have an image sprite. <br>
 * GameObject is from type of BasicGameSpirit for the ability of being converted into a GameSpirit for use
 * of drawing on screen in X,Y pixel coordinates. <br>
 * please use a BasicGameSpiritFactory for creating GameSpirits from an GameObject. <br>
 * Having default values of width = 16 and height = 16.
 */
public abstract class GameObject implements Comparable<GameObject>, BasicGameSpirit, Cloneable{
    //Type enum, P for pacman, F for fruit ans so ON...
    protected TYPE type;
    // Geodetic GPS point (lat, lon ,alt)
    protected Point3D point;
    // this object's id.
    protected int id;
    // image
    protected Image spirit;
    // default values.
    protected int width = 16, height = 16;

    /**
     * get this GameObject's Type. (ENUM of TYPE)
     * @return TYPE.type
     */
    public TYPE getType() {
        return type;
    }

    /**
     * set a new type for this GameObject. <Br>
     * NOTE: warning, please do not set it directly unless you have to. !
     * @param type
     */
    public void setType(TYPE type) {
        this.type = type;
    }

    /**
     * get this Point3D geodetic point position.
     * @return Point3D geodetic position.
     */
    public Point3D getPoint() {
        return point;
    }

    /**
     * Set new Point3D geodetic position.
     * @param point Point3D.
     */
    public void setPoint(Point3D point) {
        this.point = point;
    }

    /**
     * Get this GameObject's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * set new ID to this GameObject.
     * NOTE : PLEASE USE THE "generateID()" function in Game object for setting a new id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get this GameObject's game sprite image (getting a copy).
     * @return Image that can be casted into BufferedImage.
     */
    public Image getSpirit() {
    	BufferedImage i = (BufferedImage) spirit;
    	BufferedImage copyOfImage = 
    			   new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
    	Graphics g = copyOfImage.createGraphics();
    	g.drawImage(i, 0, 0, null);
        return copyOfImage;
    }

    
    /**
     * set a new sprite image. (passing by reference)
     * @param Image sprite.
     */
    public void setSpirit(Image spirit) {
        this.spirit = spirit;
    }
    
	@Override
	public int getInitialWidth() {
		return width;
	}

	@Override
	public int getInitialHeight() {
		return height;
	}

	@Override
	public void setInitialWidth(int width) {
		this.width = width;
	}

	@Override
	public void setInitialHeight(int height) {
		this.height = height;
	}

	@Override
	public void setInitialSize(int width, int height) {
		setInitialWidth(width);
		setInitialHeight(height);
	}

    @Override
    public int compareTo(GameObject o) {
        if(o.type == this.type) {
            return this.id - o.id;
        } else return (this.type.getValue() - o.type.getValue());

    }
    
    @Override
    public GameObject clone() {
    	GameObject o;
		try {
			o = (GameObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    	o.setId(getId());
    	o.setInitialSize(getInitialWidth(), getInitialHeight());
    	o.setSpirit(getSpirit());
    	o.setPoint(new Point3D(point.x(), point.y(), point.z())); 
    	o.setType(getType());
    	return o;
    }
   

    @Override
    public String toString() {
        return "Point: [ Lat: "+point.x()+" , Lon: "+point.y()+" , Alt: "+point.z()+" ] , Id: "+getId()+" , Type: "+getType();
    }
}
