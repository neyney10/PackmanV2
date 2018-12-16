package Maps;

import java.awt.Rectangle;

import Coords.MyCoords;
import GUI.ImageFactory;
import GUI.MyFrame;
import Geom.Point3D;

public final class ArielMap extends Map{
	
	public ArielMap() {
		// default values
		/*BOAZ SWITCH BETWEEN LATITUDE AND LONGITUDE
		 */
		double x1 = 35.2024f; // upper left corner
		double y1 = 32.1056f;
		double x2 = 35.2121f; // lower right corner
		double y2 = 32.1019f;
		 
		
//		double x1 = 32.1052f; // upper left corner
//		double y1 = 35.2024f;
//		double x2 = 32.1022f; // lower right corner
//		double y2 = 35.2121f;

		
		mapRange = new MapRange(x1,y1,x2,y2);
		screenRange = new Rectangle(0, 0, 1433, 642);
		originalScreenRange = new Rectangle(0, 0, 1433, 642);
		scaleFactorX = 1;
		scaleFactorY = 1;
		
		background = ImageFactory.getImageFromDisk("GameData\\Ariel1.png");
		
		if(MyFrame.DEBUG) {
			angleFactor = new MyCoords().azimuth_elevation_dist(new Point3D(x1,y1,0), new Point3D(32.105827, 35.212061))[0];
			angleFactor = Math.toRadians(angleFactor);
			System.out.print("[DEBUG][MAP] ");
			System.out.println(angleFactor);
		}
	}

}
