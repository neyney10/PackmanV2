package Algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import Coords.MyCoords;
import GUI.JBackground;
import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Path.Path;

public class PathFinding {

	ArrayList<Cost> costs;
	private JBackground gameUI;
	private Game game;

	public PathFinding(JBackground gameUI) {
		this.gameUI = gameUI;
		this.game = gameUI.getGame();
		this.costs = new ArrayList<>();

		GameObject obj;
		Iterator<GameObject> iter = game.iterator();
		while (iter.hasNext()) {
			obj = iter.next();
			if (obj instanceof Packman) {
				;

				((Packman) obj).setPath(new Path(obj.getPoint()));
			}

		}

	}

	/**
	 * NAIVE
	 * 
	 * @param game
	 */
	public void calcPath() {
		MyCoords co = new MyCoords();

		// SETUP
		Iterator<GameObject> iterPack;
		Iterator<GameObject> iterFruit = game.typeIterator(new Fruit(0));
		Packman pacman;
		Fruit fruit;

		while (iterFruit.hasNext()) {
			fruit = (Fruit) iterFruit.next();
			iterPack = game.typeIterator(new Packman(0));
			while (iterPack.hasNext()) {
				pacman = (Packman) iterPack.next();
				double c = ((co.distance3d(pacman.getPoint(), fruit.getPoint())) - pacman.getRadius())
						/ pacman.getSpeed();
				if (c < 0)
					c = 0;
				costs.add(new Cost(pacman, fruit, c));
			}
		}

		// sort
		costs.sort(new CostComperator());

		// START
		while (costs.size() > 0) {

			// get first, minimum effort/cost to eat a fruit.
			Cost minCost = costs.get(0);
			costs.remove(minCost);
			minCost.p.getPath().add(minCost.f.getPoint());
			double timeCost = minCost.cost;
			Fruit f = minCost.f;
			Packman p = minCost.p;
			// updates times
			Cost c;
			Iterator<Cost> iterCost = costs.iterator();
			while (iterCost.hasNext()) {
				c = iterCost.next();
				if (c.f == f)
					iterCost.remove();
				else if(c.p != p)
					c.cost -= timeCost;
				else if(c.p == p) {
					// recalc
					//TODO: fix the radius while calculating distance from first fruit 
					c.cost = ((co.distance3d(f.getPoint(), c.f.getPoint())) - c.p.getRadius())/ c.p.getSpeed();
				}
					
			}
					// sort
		costs.sort(new CostComperator());

		}


	}

	class Cost {
		Packman p;
		Fruit f;
		double cost;

		public Cost(Packman pacman, Fruit fruit, double cost) {
			this.p = pacman;
			this.f = fruit;
			this.cost = cost;
		}

		@Override
		public String toString() {
			return cost + "";
		}
	}

	class CostComperator implements Comparator<Cost> {

		@Override
		public int compare(Cost o1, Cost o2) {
			return (int) (o1.cost - o2.cost);
		}

	}

}
