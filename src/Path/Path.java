package Path;

import java.util.Iterator;
import java.util.LinkedList;

import Coords.MyCoords;
import Geom.Point3D;

public class Path {
	LinkedList<Point3D> points;
	
	
	public Path() {
		points = new LinkedList<>();
	}
	
	public void add(Point3D p3d) {
		points.add(p3d);
	}
	
	public double length() {
		if(points.size() <= 1)
			return 0;
		
		MyCoords m = new MyCoords();
		double len = 0;
		Iterator<Point3D> iter = points.iterator();
		Point3D p3d1 = iter.next();
		while(iter.hasNext()) {
			Point3D p3d2 = iter.next();
			len += m.distance3d(p3d1, p3d2);
			p3d1 = p3d2;
		}
		
		return len;
	}
	
	public Iterator<Point3D> iterator() {
		return points.iterator();
	}

}
