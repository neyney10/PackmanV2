package Path;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Iterator;
import java.util.LinkedList;

import Coords.MyCoords;
import Geom.Point3D;
import Maps.Map;
import Util.ColorGenerator;

/**
 * Path class is a class which contains a Geodetic Point3D points
 * can be drawable on screen with a Map object to concvert geodetic points to pixels.
 */
public class Path {
	
	private LinkedList<Point3D> points;
	private String color;
	private int id;
	
	/**
	 * Path is an array of Geodetic Point3D points
	 * @author Ofek Bader
	 */
	public Path() {
		points = new LinkedList<>();
		setColor(ColorGenerator.getInstance().colorGenerate()); // default
	}


	/**
	 * Path is an array of Geodetic Point3D points
	 * with a starting point
	 * @param Point3D point - starting point.
	 */
	public Path(Point3D point) {
		this();
		points.addFirst(point);
	}

	/**
	 * add new point to this Path.
	 * @param p3d a new point to add.
	 */
	public void add(Point3D p3d) {
		points.add(p3d);
	}

	/**
	 * returns the size of the list of points, the total amount of points in the path.
	 * @return amount of points
	 */
	public int getPointAmount() {
		return points.size();
	}

	
	/**
	 * return the last point in this path
	 * @return Point3D last point.
	 */
	public Point3D getLast() {
		return points.getLast();
	}


	/**
	 * RETURNS THE SIZE IN METERS
	 * @return
	 */
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
	

	/**
	 * SetColor setting color of path
	 * @param color String hex rgb of color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Paint the path with lines
	 * @param g - graphic context (Which frame/panel/JComponent)
	 * @param map (the game's map for coordinates translation on screen)
	 */
	public void paint(Graphics g, Map map) {
		// get the enhanced type of Graphics version - Graphics2D that can select strokeSize and colors.
		Graphics2D g2 = (Graphics2D) g;
		
		// setup drawing configuration
		Stroke lineStroke = new BasicStroke(3);
		Stroke ovalStroke = new BasicStroke(2.0f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                10.0f, new float[]{6f}, 0.0f);

		g2.setColor(Color.decode(color)); // color

		
		Iterator<Point3D> iterPath = iterator();
		Point3D p1 = iterPath.next();
		Point3D p2;
		
		Point px1, px2;
		
		do {
			p2 = iterPath.next();
			px1 = map.getLocationOnScreen(p1);
			px2 = map.getLocationOnScreen(p2);
			
			g2.setStroke(lineStroke); // size
			g2.drawLine(px1.x, px1.y, px2.x, px2.y);
			g2.setStroke(ovalStroke); // size
			g2.drawOval(px1.x-16, px1.y-16, 32, 32);
			
			p1 = p2;
			

		} while(iterPath.hasNext());
		
		g2.drawOval(px2.x-16, px2.y-16, 32, 32);
	}

	/**
	 * getColor getting color of path
	 * @return color String hex rgb of color
	 */
	public String getColor() {
		return color;

	}

	/**
	 * Sets a new ID
	 * @param id
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * gets this Path's id.
	 * @return id (integer)
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * get the iterator over this Path which iterates over Point3D
	 * @return Iterator<Point3D> iterator.
	 */
	public Iterator<Point3D> iterator() {
		return points.iterator();
	}


}
