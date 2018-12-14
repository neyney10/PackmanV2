package GameObjects;

import Geom.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject implements Comparable<GameObject>, BasicGameSpirit, Cloneable{
    private TYPE type;
    private Point3D point;
    private int id;
    private Image spirit;
    private int width = 16, height = 16;

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
    	BufferedImage i = (BufferedImage) spirit;
    	BufferedImage copyOfImage = 
    			   new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
    	Graphics g = copyOfImage.createGraphics();
    	g.drawImage(i, 0, 0, null);
        return copyOfImage;
    }

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
        } else if(o.type == TYPE.P) {
            return -1;
        } else return 1;
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
