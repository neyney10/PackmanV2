package Algorithms;

import java.util.Iterator;
import java.util.LinkedList;

import Game.Game;
import GameObjects.GameObject;
import GameObjects.Packman;
import Path.Path;

public class PathFinding {
	
	/**
	 * NAIVE
	 * @param game
	 * @return
	 */
	public LinkedList<Path> calcPath(Game game) {
		LinkedList<Path> solution = new LinkedList<Path>();
		
		Iterator<GameObject> iter = game.typeIterator(new Packman());
		while(iter.hasNext()) {
			GameObject obj = iter.next();
			
		}
		
		
		return solution;
	}
	

}
