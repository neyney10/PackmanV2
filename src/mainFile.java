import java.awt.Image;
import java.awt.Point;
import java.util.TreeSet;

import GUI.MyFrame;
import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.TYPE;
import Maps.Map;

public class mainFile {

	static String packmanImagePath = "GameData\\PACMAN.png";
	static String fruitImagePath ="GameData\\fruit.png";
	static Image packmanImage, fruitImage;
	
	public static void main(String[] args) {
		System.out.println("Main starting...");
		
		//// starting user simulation for testing
		// get data
		packmanImage = MyFrame.loadImage(packmanImagePath);
		fruitImage = MyFrame.loadImage(fruitImagePath);
		
		// create new GameObjects
		Packman p1 = new Packman(32.10394266, 35.20724539 ,30,0, 1, 1);
		p1.setSpirit(packmanImage);
		Packman p2 = new Packman(32.10404121,35.20758436,30,1, 1, 1);
		p2.setSpirit(MyFrame.rotateImage(packmanImage,33));
		Packman p3 = new Packman(32.10333584,35.20895676,30,2, 1, 1);
		p3.setSpirit(MyFrame.rotateImage(packmanImage,77));
		Fruit f1 = new Fruit(32.10441982,35.20674934,30,3);
		f1.setSpirit(fruitImage);
		
		Map m = new Map();
		System.out.println(m.getDistance(new Point(551,331), new Point(555,555)));
		System.out.println(m.getDistanceByPixel(new Point(551,331), new Point(555,555)));
		System.out.println(m.getAngle(new Point(551,331), new Point(551,0)));
		System.out.println(m.getAngle(new Point(551,331), new Point(251,331)));
		System.out.println(m.getAngle(new Point(551,331), new Point(251,111)));
		
		// create game object
		TreeSet<GameObject> objs = new TreeSet<GameObject>();
		objs.add(p1);
		objs.add(p2);
		objs.add(p3);
		objs.add(f1);
		
		Game game = new Game(objs);
		
		MyFrame f = new MyFrame();
		f.setGame(game);
	}

}
