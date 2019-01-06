package Algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import Coords.MyCoords;
import GUI.JBackground;
import GUI.MyFrame;
import Game.Game;
import GameObjects.Box;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.Player;
import Geom.Point3D;
import Path.Path;

/**
 * Sonic's algorithm for robot. Version 2.0 <br>
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
public class SonicAlgorithmV3 implements RobotAlgorithm {

	////////////////////////
	// Game's status data //
	////////////////////////
	public ArrayList<Packman> pacmans; // list of pacmans
	public ArrayList<Fruit>  fruits; // list of fruits
	public ArrayList<Box> boxes; // list of boxes
	Player player; // player object itself
	Game game; // game's object.

	///////////////////
	// Path handling //
	///////////////////
	RobotPathFindingAlgorithm pathAlgorithm; // the algorithm for handling path's to certain locations in game.
	Path path; // player's movement path
	Iterator<Point3D> iterPath; // players' path iterator.
	Point3D nextPoint; // the current next position the player is going to.
	int step, steps;

	////////////////
	// utillities //
	////////////////
	MyCoords c = new MyCoords(); 
	boolean calculated = false; // TODO

	/// OTHER ///
	LinkedList<Cost> costs;


	/**
	 * [Constructor] <br>
	 * Create a new Sonic Algorithm Version 2.0, supply a path Finding algorithm for reaching next's goal.
	 * @param algorithm Path-Finding algorithm.
	 */
	public SonicAlgorithmV3(RobotPathFindingAlgorithm algorithm) {
		this.pathAlgorithm = algorithm;
	}

	/**
	 * [Constructor] <br>
	 * Create a new Sonic Algorithm Version 2.0, supply a path Finding algorithm for reaching next's goal.
	 * @param game - game object to refresh game status, calling "refreshGameStatus(game)".
	 * @param algorithm Path-Finding algorithm.
	 */
	public SonicAlgorithmV3(Game game, RobotPathFindingAlgorithm algorithm) {
		this(algorithm);
		refreshGameStatus(game);
	}

	@Override
	public void refreshGameStatus(Game game) {
		Iterator<GameObject> iter;
		this.game = game;
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
		
		
		// temp X2
		if(path != null) {
			boolean found = false;
			for(int i =0; i< fruits.size();i++) {
				if(fruits.get(i).getPoint().equals(path.getLast())) {
					found = true;
					break;
				}

			}
			if(!found)
				nextPath();
		}

	}

	/**
	 * Try to predict all pacman's path and positions across he game
	 * in order to determine the complete sonic's strategy for maximum points in minimum effort.
	 */
	public void calcPacmanPathV2() {

		if(pacmans.size() == 0 || fruits.size() ==0)
			return;

		Cost cost;
		double time, totalTime = 0;
		costs = new LinkedList<>();
		// a Cost comparator for sorting the "distCost" data structure.
		CostComperator costComp = new CostComperator();
		LinkedList<Fruit> tempFruits = new LinkedList(fruits);
		HashMap<Packman, Point3D> temp = new HashMap<>();
		//init
		for(Packman pacman : pacmans) {
			pacman.setPath(new Path(new PathPoint(pacman.getPoint(), 0)));
			temp.put(pacman, pacman.getPoint()); // temp
		}


		while(!tempFruits.isEmpty()) {
			costs.clear();
			Cost minCost;
			for(Packman pacman : pacmans) {
				minCost = new Cost(pacman,null,Double.MAX_VALUE);
				for(Fruit fruit : tempFruits) {
					double distance = this.c.distance3d(pacman.getPoint(), fruit.getPoint());

					if(distance < minCost.cost) {
						minCost.cost = distance;
						minCost.f = fruit;
					}
				}
				costs.add(minCost);
			}

			// Sort costs
			costs.sort(costComp);
			// get minimum cost
			cost = costs.removeFirst();
			time = cost.cost/cost.p.getSpeed();
			totalTime += time;
			tempFruits.remove(cost.f);
			cost.p.getPath().add(new PathPoint(cost.f.getPoint(), totalTime));
			// advance pacman
			cost.p.setPoint(cost.f.getPoint());

			Iterator<Cost> iterCost = costs.iterator();
			while(iterCost.hasNext()) {
				Cost c = iterCost.next();

				if(c.p == cost.p)
					continue;

				Point3D newPos = c.p.getPoint();
				Point3D moveVector = this.c.vector3D(newPos, c.f.getPoint()); 
				double vecNorm = this.c.vectorNormal2D(moveVector);
				moveVector = new Point3D(moveVector.x()/vecNorm, moveVector.y()/vecNorm);
				moveVector = new Point3D(moveVector.x()*c.p.getSpeed()*time, moveVector.y()*c.p.getSpeed()*time);
				newPos = this.c.add(newPos, moveVector);
				c.p.setPoint(newPos);
				c.p.getPath().add(newPos);
			}
		}

		// temp
		temp.forEach((pack, point) -> {
			pack.setPoint(point);
		});

	}

	/**
	 * Get the latest fruit position eaten by pacmans, must first call "calcPacmanPathV2()".
	 * @return Point3D as the position of the fruit.
	 */
	public Point3D findLatestEatenFruitPosition() {
		double maxTime = -1;
		Point3D p, latestPoint = null;
		PathPoint pp;
		for(Packman pacman : pacmans) {
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
	 * Advancing by a single step 
	 * if the player is already close enough to the next point or step >= steps then
	 * calculating a new point by calling the "nextPoint()" function. <br>
	 * NOTE: the three functions "nextStep()", "nextPoint() and "nextPath()" are integrated inside each-other
	 */
	private void nextStep() {
		++step; // increase the step counter
		// if the player is close enough to destination point (next point) then just get the next point.
		if(step >= steps || this.c.distance3d(player.getPoint(), nextPoint) < player.getSpeed()/10 )
			nextPoint();

		//System.out.println(step+"/"+steps);
	}

	/**
	 * Advancing by a single point in calculated path. 
	 * if the path is over then calculating a new path by calling the "nextPath()" function. <br>
	 * NOTE: the three functions "nextStep()", "nextPoint() and "nextPath()" are integrated inside each-other
	 */
	private void nextPoint() {
		if(path != null && path.getPointAmount() > 0 && iterPath.hasNext()) {

			Point3D prevPoint = player.getPoint();

			// fix: if not close enough to the point.
			if(!(this.c.distance3d(player.getPoint(), nextPoint) > player.getSpeed()/10)) 
				nextPoint = iterPath.next();

			// calculate number of steps required to reach the next point.
			step = 0;
			steps = (int) Math.round(this.c.distance3d(prevPoint, nextPoint)*11/player.getSpeed()) + 1;

			//System.out.println("New Point: "+step+"/"+steps);
		} else nextPath();

	}

	/**
	 * Advancing to the next objective by calculating the path using the path algorithm given to this object. <br>
	 * NOTE: the three functions "nextStep()", "nextPoint() and "nextPath()" are integrated inside each-other
	 */
	private void nextPath() {
		calculate();
		DijkstraAlgorithm da = new DijkstraAlgorithm(game);
		Point3D dest[]  = new Point3D[fruits.size()];
		for(int i =0; i< dest.length;i++)
			dest[i] = fruits.get(i).getPoint();
		Path[] paths = da.test(player.getPoint(), dest);

		if(paths.length == 0) {
			path = null;
			return;
		}
			
		
		path = paths[0];
		double minTime = path.length()/player.getSpeed()*20;
		int k = 0;
		boolean fastest = false;

		while(!fastest && k<paths.length) {
			fastest = true;
			Iterator<Packman> iterPack = pacmans.iterator();
			while(iterPack.hasNext()) {
				Packman pacman = iterPack.next();

				if(pacman.getPath() == null)
					continue;

				Iterator<Point3D> iterPath = pacman.getPath().iterator();
				while(iterPath.hasNext()) {
					Point3D p3d = iterPath.next();
					PathPoint pp;
					if(p3d instanceof PathPoint) {
						pp = (PathPoint) p3d;
						if(path.getLast().equals(pp)) {
							//System.out.println("P Time: "+pp.time + "min Time: "+minTime);
							if(pp.time < minTime) {
								path = paths[k++];
								minTime = path.length()*1.49;
								fastest = false;
							}
							break;
						}
					}
				}
				if(!fastest)
					break;
			}
		}
		//System.out.println(k);
		if(k == paths.length)
			path = paths[0];

		MyFrame.getInstance().path = path; // TEMP

		iterPath = path.iterator();
		nextPoint = iterPath.next();

		nextPoint();
	}

	/**
	 * Sonic movement algorithm.
	 */
	@Override
	public void calculate() {
		calcPacmanPathV2();
	}

	@Override
	public double getPlayerOrientation() {
		nextStep();

		double orientation = 360 -  this.c.azimuth_elevation_dist(player.getPoint(), nextPoint)[0] + 90;
		if(orientation > 360)
			orientation -= 360;
		if(orientation < 0)
			orientation += 360;

		return orientation; 
	}

	@Override
	public Point3D getPlayerStartPosition() {
		Point3D point = null;
		try {
			point = findLatestEatenFruitPosition();
		} catch (Exception e) {
			System.out.println("Error, couldn't calculate pacman's paths, are there any pacmans?");
		}

		if(point == null)
			point = fruits.get(0).getPoint();

		return point;
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