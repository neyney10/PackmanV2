package Algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import Coords.MyCoords;
import GUI.JBackground;
import GUI.MyFrame;
import Game.Game;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.Fruit;
import Path.Path;

public class PathFinding {

	ArrayList<Cost> costs;
	private JBackground gameUI;
	private Game game;

	public PathFinding(JBackground gameUI) {
		this.gameUI = gameUI;
		this.game = gameUI.getGame();
		this.costs = new ArrayList<>();

		int packCount = 0;
		int fruitCount = 0;
		GameObject obj;
		Iterator<GameObject> iter = game.iterator();
		while (iter.hasNext()) {
			obj = iter.next();
			if (obj instanceof Packman) {
				packCount++;

				((Packman) obj).setPath(new Path(obj.getPoint()));
			}

			else if (obj instanceof Fruit)
				fruitCount++;
		}

	}

	/**
	 * NAIVE
	 * 
	 * @param game
	 * @return
	 */
	public LinkedList<Path> calcPath() {
		LinkedList<Path> solution = new LinkedList<Path>();
		MyCoords co = new MyCoords();

		// SETUP
		Iterator<GameObject> iterPack;
		Iterator<GameObject> iterFruit = game.typeIterator(new Fruit());
		Packman pacman;
		Fruit fruit;

		while (iterFruit.hasNext()) {
			fruit = (Fruit) iterFruit.next();
			iterPack = game.typeIterator(new Packman());
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

		// DEBUG
		if (MyFrame.DEBUG) {
			System.out.println(costs.toString());
		}

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

			// DEBUG
			if (MyFrame.DEBUG) {
				System.out.println("Ate Fruit: ");
				System.out.println(costs.toString());
			}

		}

		return solution;
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
