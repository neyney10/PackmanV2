package GameObjects;

import java.awt.Image;

import Geom.Point3D;


public abstract class GameObject {
    private TYPE type;
    private Point3D point;
    private Image spirit;

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

	public Image getSpirit() {
		return spirit;
	}

	public void setSpirit(Image spirit) {
		this.spirit = spirit;
	}
	
	
}
