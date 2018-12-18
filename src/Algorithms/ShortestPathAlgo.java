package Algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import Coords.MyCoords;
import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Path.Path;
import Path.Solutions;

/**
 * Path finding algorithm,
 * getting a Game, with pacmans and fruits, calculating the shortest path for all
 * pacmans to move for eating all fruits in the fastest way possible.
 * using a simillar algorithm to Dijkstra. <br>
 * Use: first create a new instance of this class by giving it a game object to work on,
 * and then use the "calcPath()" function to compute.
 * NOTE: the algorithm setting its result of each pacman's path in the Packman's Path object in each pacman.
 */
public class ShortestPathAlgo {

	// Costs MINHEAP, COST = time to go from point A to point B.
	ArrayList<Cost> costs;

	// the Game object to compute.
	private Game game;

	/**
	 * Create a new ShortestPathAlgo object for computing each Pacman's paths.
	 * @param game
	 */
	public ShortestPathAlgo(Game game) {
		this.game = game;
		this.costs = new ArrayList<>();

		GameObject obj;
		Iterator<GameObject> iter = game.typeIterator(new Packman(0));
		while (iter.hasNext()) {
			obj = iter.next();
				((Packman) obj).setPath(new Path(obj.getPoint()));
		}

	}

	/**
	 * Using a custom Dijkstra algoritm to compute path's of each pacman.
	 * setting the Path object in each Packman's object as a result.
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

	/**
	 * Cost object for saving the cost of each Edge in the graph.
	 * From origin point P to destination point of fruit F.
	 */
	class Cost {
		Packman p;
		Fruit f;
		double cost;

		/**
		 * creates new cost wrapper obj
		 * @param pacman
		 * @param fruit
		 * @param cost - distance/speed = time or any other "cost" calculation. that can be saved.
		 */
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

	/**
	 * Comperator for "Cost" object, comparing the Cost.cost field of each two Cost objects
	 * Ordering by cost (primitive Double).
	 */
	class CostComperator implements Comparator<Cost> {

		@Override
		public int compare(Cost o1, Cost o2) {
			return (int) (o1.cost - o2.cost);
		}

	}

	/**
	 * Get a Game object, and iterate over all pacmans in this game, combining all of thier path's into
	 * one Solutions object for using for KML conversion, manipulating and sharing.
	 * @param game
	 * @return Solutions as a Paths array.
	 */
	public static Solutions convertIntoPathSolutions(Game game) {
		Solutions solution = new Solutions();
		Iterator<GameObject> iter = game.typeIterator(new Packman(0));
		while(iter.hasNext()) {
			solution.add(((Packman)(iter.next())).getPath());
		}

		return solution;
	}

}
