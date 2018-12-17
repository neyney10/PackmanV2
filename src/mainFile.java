import java.awt.Point;
import java.util.TreeSet;

import GUI.MyFrame;
import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Geom.Point3D;
import Maps.Map;
import Maps.MapFactory;
import Maps.MapFactory.MapType;
import Path.Path;

public class mainFile {


	
	public static void main(String[] args) {
		System.out.println("Main starting...");

		// create new GameObjects
		Packman p1 = new Packman(35.21166773101956,32.10505666762498,30,0, 1, 1);
		
		Packman p2 = new Packman(35.211011083992894,32.102860721100896,30,1, 1, 1);
	
		Fruit f1 = new Fruit(35.21116678380334,32.10302786663685,30,3);
		Fruit f2 = new Fruit(35.208522007315274,32.103762444649036,30,4);

		// create game object
		TreeSet<GameObject> objs = new TreeSet<GameObject>();
		objs.add(p1);
		objs.add(p2);
		objs.add(f1);
		objs.add(f2);
		
		Game game = new Game(objs);
		
		MyFrame f = MyFrame.getInstance();
		f.setVisible(true);
		System.out.println("Making MyFrame visible...");
		f.setGame(game);

		
	}

}
