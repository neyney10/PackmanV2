import java.awt.Image;
import java.util.ArrayList;

import GUI.MyFrame;
import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.TYPE;

public class mainFile {

	static String packmanImagePath = "Ex3\\PACMAN.png";
	static String fruitImagePath ="Ex3\\fruit.png";
	static Image packmanImage, fruitImage;
	
	public static void main(String[] args) {
		System.out.println("Main starting...");
		
		//// starting user simulation for testing
		// get data
		packmanImage = MyFrame.loadImage(packmanImagePath);
		fruitImage = MyFrame.loadImage(fruitImagePath);
		
		// create new GameObjects
		Packman p1 = new Packman(35.207805, 32.103723 ,30,TYPE.P, 1, 1); 
		p1.setSpirit(packmanImage);
		Packman p2 = new Packman(35.207446,32.104424,30,TYPE.P, 1, 1);
		p2.setSpirit(packmanImage);
		Fruit f1 = new Fruit(35.2024,32.1056,30,TYPE.F,0,0);
		f1.setSpirit(fruitImage);
		
		// create game object
		ArrayList<GameObject> objs = new ArrayList<GameObject>();
		objs.add(p1);
		objs.add(p2);
		objs.add(f1);
		
		Game game = new Game(objs);
		
		MyFrame f = new MyFrame();
		f.setGame(game);
	}

}
