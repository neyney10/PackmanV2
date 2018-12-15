import java.awt.Image;
import java.awt.Point;
import java.util.TreeSet;

import GUI.MyFrame;
import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.TYPE;
import Geom.Point3D;
import Maps.ArielMap;
import Maps.Map;
import Maps.MapFactory;
import Maps.MapFactory.MapType;
import Path.Path;

public class mainFile {


	
	public static void main(String[] args) {
		System.out.println("Main starting...");
		
		//// starting user simulation for testing
		// get data

		
		// create new GameObjects
		Packman p1 = new Packman(35.211222,32.104496,30,0, 1, 1);
		
		Packman p2 = new Packman(35.207462,32.102482,30,1, 1, 1);
	
		Fruit f1 = new Fruit(35.207462,32.102482,30,3);

		
		Map m = MapFactory.getMap(MapType.ArielUniversity);
		System.out.println("Check conversion: "+p1.getPoint()+" | "+m.getLocationOnScreen(p1)+ " | "+m.getLocationFromScreen(m.getLocationOnScreen(p1)));
		System.out.println("Check conversion: "+p2.getPoint()+" | "+m.getLocationOnScreen(p2)+ " | "+m.getLocationFromScreen(m.getLocationOnScreen(p2)));
		System.out.println(m.getDistance(new Point(551,331), new Point(555,555)));
		System.out.println(m.getDistanceByPixel(new Point(551,331), new Point(555,555)));
		System.out.println(m.getAngle(new Point(551,331), new Point(551,0)));
		System.out.println(m.getAngle(new Point(551,331), new Point(251,331)));
		System.out.println(m.getAngle(new Point(551,331), new Point(251,111)));
		
		// create game object
		TreeSet<GameObject> objs = new TreeSet<GameObject>();
		objs.add(p1);
		objs.add(p2);
		objs.add(f1);
		
		Game game = new Game(objs);
		
		MyFrame f = MyFrame.getInstance();
		f.setVisible(true);
		System.out.println("Making MyFrame visible...");
		f.setGame(game);
		
		Path p = new Path();
		p.add(new Point3D(35.211222,32.104496,30));
		p.add(new Point3D(35.207462,32.102482,30));
		p.add(new Point3D(35.208462,32.103482,30)); //different
		p.add(new Point3D(35.203462,32.103482,30)); //different
		p.add(new Point3D(35.204462,32.104082,30)); //different
		System.out.println("Path length: "+ p.length());
		
		p1.setPath(p);
		
	}

}
