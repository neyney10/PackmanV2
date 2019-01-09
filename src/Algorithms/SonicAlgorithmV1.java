package Algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import Coords.MyCoords;
import GUI.JBackground;
import Game.Game;
import GameObjects.Box;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.Player;
import Geom.Point3D;
import Path.Path;

/**
 * Sonic's algorithm for robot. Version 1.0 <br>
 * Strategy: <br>
 * General strategy: help the pacmans to eat all fruits the fastest you can and earn the time left bonus. <br>
 * Starting Position: start in the latest fruit that will be eaten. <br>
 * How to use:  <br>
 * [1] pass into constructor the Game object <br>
 * [2] call getPlayerStartingPosition for initializing player position into "Play" server using
 * the setInitLocation function. <br>
 * [3] then in every game's step call getPlayerOrientation to calculate next move.
 * @author Ofek Bader
 */
public class SonicAlgorithmV1 implements RobotAlgorithm {

	private ArrayList<Packman> pacmans;
	private ArrayList<Fruit>  fruits;
	private ArrayList<Box> boxes;
	Player player;
	Game game;
	/////////////
	MyCoords c = new MyCoords(); 
	boolean calculated = false; // TODO

	/**
	 * [Constructor] <br>
	 * Generate a new SonicAlgorithmV1, must provide a "Game" object later on on the refreshGameStatus at least once.
	 */
	public SonicAlgorithmV1() {
	}
	
	/**
	 * [Constructor] <br>
	 * @param game - the game status to set.
	 */
	public SonicAlgorithmV1(Game game) {
		this();
		this.game = game;
		refreshGameStatus(game);
	}

	@Override
	public void refreshGameStatus(Game game) {
		
		Iterator<GameObject> iter;

		// PACMANS
		pacmans = new ArrayList<Packman>();
		iter = game.typeIterator(new Packman(0));
		while(iter.hasNext()) 
			pacmans.add((Packman) iter.next().clone());

		// FRUITS
		fruits = new ArrayList<Fruit>();
		iter = game.typeIterator(new Fruit(0));
		while(iter.hasNext()) 
			fruits.add((Fruit) iter.next().clone());

		// BOXES
		boxes = new ArrayList<Box>();
		iter = game.typeIterator(new Box(0));
		while(iter.hasNext()) 
			boxes.add((Box) iter.next().clone()); 
		
		player = game.getPlayer();
	}

	/**
	 * Get path for each pacman.
	 */
	public void calcPacmansPath() {

		// list of costs for each pacman
		LinkedList<Cost>  distCost = new LinkedList<>();
		// iterator of pacmans
		Iterator<Packman> iterPack = pacmans.iterator();
		// a Cost comperator for sorting the "distCost" data structure.
		CostComperator costComp = new CostComperator();

		if(pacmans.size() ==0)
			return;
		
		double time;
		Path path;
		Point3D startingPoint;
		Cost cost; // refrence holder
		Packman pacman; // reference holder
		while(iterPack.hasNext()) {
			// get the next pacman
			pacman = iterPack.next();
			// save starting point to return to
			startingPoint = pacman.getPoint();
			// create new path for it
			path = new Path();
			path.add(startingPoint);
			time = 0;
			// start a clean list of costs for this pacman.
			distCost = calculateDistances(pacman);

			// Sort list
			distCost.sort(costComp);

			while(!distCost.isEmpty()) {
				cost = distCost.removeFirst();
				// calculate time
				time += cost.cost/pacman.getSpeed();
				// add the closest fruit to the pacman's path.
				path.add(new PathPoint(cost.f.getPoint(), time));
				// update the pacman's position, it is now standing on the fruit.
				pacman.setPoint(cost.f.getPoint());

				//recalculate edges of cost - recalculate all the distanced for this pacman to all fruits.
				recalculateDistances(distCost, pacman.getPoint());

				// Re-sort after relax
				distCost.sort(costComp);
			}

			pacman.setPath(path);
			pacman.setPoint(startingPoint);
		}
	}

	/**
	 * Calculate distances of pacman to fruits.
	 * @param p - pacman
	 * @return a heap of costs (distances)
	 */
	private LinkedList<Cost> calculateDistances(Packman p) {
		LinkedList<Cost> costs = new LinkedList<SonicAlgorithmV1.Cost>();
		// iterator of fruits
		Iterator<Fruit> iterFruit;
		// build the "costs" costs data structure.
		double distance;
		Fruit fruit; // reference holder
		iterFruit = fruits.iterator();
		while(iterFruit.hasNext()) {
			fruit = iterFruit.next();
			// calculate distance between the pacman and fruit.
			distance = c.distance3d(p.getPoint(), fruit.getPoint());
			// add it to the list.
			costs.add(new Cost(p, fruit, distance));
		}

		return costs;
	}

	/**
	 * Given a already calculated heap, recalculate / refresh the costs of each edge.
	 * @param costs - the heap of costs
	 * @param p - relative to point p.
	 */
	private void recalculateDistances(LinkedList<Cost> costs, Point3D p) {
		costs.forEach((cost) -> {
			cost.cost = c.distance3d(cost.p.getPoint(), cost.f.getPoint());
		});

	}

	/**
	 * Get the latest fruit that gets eaten.
	 * @return the position of the fruit that gets eaten.
	 */
	public Point3D findLatestEatenFruitPosition() {
		double maxTime = -1;
		Point3D p, latestPoint = null;
		PathPoint pp;
		for(Packman pacman : pacmans) {
			if(pacman.getPath() == null)
				return fruits.get(0).getPoint();
			
			Iterator<Point3D> iterPath = pacman.getPath().iterator();
			while(iterPath.hasNext()) {
				p = iterPath.next();
				if(p instanceof PathPoint) {
					pp = (PathPoint) p;
					if(pp.time > maxTime) {
						maxTime = pp.time;
						latestPoint = p;
					}

				} else continue;
			}
		}

		return latestPoint;
	}
	

	/**
	 * Get the closest fruit to player, in raw air-distance calculation.
	 * @return Closest fruit.
	 */
	private Fruit calculateClosestFruitPosition() {
		// iterator of fruits
		Iterator<Fruit> iterFruit;
		// reference holder to the cloest fruit
		Fruit closestFruit = null;
		
		double distance, minDistance = Double.MAX_VALUE;
		Fruit fruit; // reference holder
		iterFruit = fruits.iterator();
		while(iterFruit.hasNext()) {
			fruit = iterFruit.next();
			// calculate distance between the pacman and fruit.
			distance = c.distance3d(player.getPoint(), fruit.getPoint());
			if(distance < minDistance) {
				minDistance = distance;
				closestFruit = fruit;
			}
		}

		return closestFruit;
	}
	
	/**
	 * Sonic movement algorithm.
	 */
	@Override
	public void calculate() {
		calcPacmansPath();
	}
	
	@Override
	public double getPlayerOrientation() {
		Fruit closestFruit = calculateClosestFruitPosition();
		if(closestFruit == null)
			return 0; //default
		
		double orientation = 360 -  this.c.azimuth_elevation_dist(player.getPoint(), closestFruit.getPoint())[0] + 90;
		if(orientation > 360)
			orientation -= 360;
		if(orientation < 0)
			orientation += 360;
		return orientation; 
	}
	
	@Override
	public Point3D getPlayerStartPosition() {
		Point3D point;
		try {
			point = findLatestEatenFruitPosition();
			if(point == null) 
				point = fruits.get(0).getPoint();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return point;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	//////////// OTHER ////////////
	

	@Override
	public RobotAlgorithm clone() {
		return new SonicAlgorithmV1();
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

	class PathPoint extends Point3D {

		double time;

		public PathPoint(double x, double y, double time) {
			super(x, y);
			this.time = time;
		}

		public PathPoint(Point3D point, double time) {
			this(point.x(), point.y(), time);
		}

	}



}