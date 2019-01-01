package Algorithms;

import java.util.ArrayList;
import java.util.Comparator;
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

public class SonicAlgorithm {

	public ArrayList<Packman> pacmans;
	public ArrayList<Fruit>  fruits;
	public ArrayList<Box> boxes;
	Player player;
	JBackground GUI;
	/////////////
	MyCoords c = new MyCoords(); 

	public SonicAlgorithm(JBackground GUI) {
		this.GUI = GUI;
		refreshData();
	}

	private void refreshData() {
		Game game = GUI.getGame();
		Iterator<GameObject> iter;

		// PACMANS
		pacmans = new ArrayList<Packman>();
		iter = game.typeIterator(new Packman(0));
		while(iter.hasNext()) 
			pacmans.add((Packman) iter.next());

		// FRUITS
		fruits = new ArrayList<Fruit>();
		iter = game.typeIterator(new Fruit(0));
		while(iter.hasNext()) 
			fruits.add((Fruit) iter.next());

		// BOXES
		boxes = new ArrayList<Box>();
		iter = game.typeIterator(new Box(0));
		while(iter.hasNext()) 
			boxes.add((Box) iter.next()); 
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

	public void calcPacmanPathV2() {
		Cost cost;
		double time, totalTime = 0;
		LinkedList<Cost> costs = new LinkedList<>();
		// a Cost comperator for sorting the "distCost" data structure.
		CostComperator costComp = new CostComperator();

		for(Packman pacman : pacmans) {
			costs.addAll(calculateDistances(pacman));
			pacman.setPath(new Path(new PathPoint(pacman.getPoint(),0)));
		}

		while(!costs.isEmpty()) {
			// Sort Costs
			costs.sort(costComp);

			// remove min
			cost = costs.removeFirst();
			time = cost.cost/cost.p.getSpeed();
			totalTime += time;
			cost.p.getPath().add(new PathPoint(cost.f.getPoint(), totalTime));
			cost.p.setPoint(cost.f.getPoint());


			// update positions
			Cost c;
			double vecNorm;
			Point3D newPos;
			Point3D moveVector;
			Iterator<Cost> iterCost = costs.iterator();
			while(iterCost.hasNext()) {
				c = iterCost.next();
				if(cost.p == c.p)
					continue;
				if(cost.f == c.f) {
					iterCost.remove();
					continue;
				}

				newPos = c.p.getPoint();
				moveVector = this.c.vector3D(newPos, c.f.getPoint()); 
				vecNorm = this.c.vectorNormal2D(moveVector);
				moveVector = new Point3D(moveVector.x()/vecNorm, moveVector.y()/vecNorm);
				moveVector = new Point3D(moveVector.x()*c.p.getSpeed()*time, moveVector.y()*c.p.getSpeed()*time);
				newPos = this.c.add(newPos, moveVector);
				cost.cost = this.c.distance3d(newPos, c.f.getPoint());
				cost.p.setPoint(newPos);

			}
		}

	}

	public void calcPacmanPathV3() {
		Cost cost;
		double time, totalTime = 0;
		LinkedList<Cost> costs = new LinkedList<>();
		// a Cost comperator for sorting the "distCost" data structure.
		CostComperator costComp = new CostComperator();

		for(Packman pacman : pacmans) {
			costs.addAll(calculateDistances(pacman));
			pacman.setPath(new Path(new PathPoint(pacman.getPoint(),0)));
		}

		while(!costs.isEmpty()) {
			// Sort Costs
			costs.sort(costComp);

			// remove min
			cost = costs.removeFirst();
			time = cost.cost/cost.p.getSpeed();
			totalTime += time;
			cost.p.getPath().add(new PathPoint(cost.f.getPoint(), totalTime));
			cost.p.setPoint(cost.f.getPoint());


			// update positions
			Cost c;
			double vecNorm;
			Point3D newPos;
			Point3D moveVector;
			Iterator<Cost> iterCost = costs.iterator();
			while(iterCost.hasNext()) {
				c = iterCost.next();
				if(cost.p == c.p)
					continue;
				if(cost.f == c.f) {
					iterCost.remove();
					continue;
				}
				

				newPos = c.p.getPoint();
				moveVector = this.c.vector3D(newPos, c.f.getPoint()); 
				vecNorm = this.c.vectorNormal2D(moveVector);
				moveVector = new Point3D(moveVector.x()/vecNorm, moveVector.y()/vecNorm);
				moveVector = new Point3D(moveVector.x()*c.p.getSpeed()*time, moveVector.y()*c.p.getSpeed()*time);
				newPos = this.c.add(newPos, moveVector);
				cost.cost = this.c.distance3d(newPos, c.f.getPoint());
				cost.p.setPoint(newPos);

			}
		}

	}
	
	private LinkedList<Cost> calculateDistances(Packman p) {
		LinkedList<Cost> costs = new LinkedList<SonicAlgorithm.Cost>();
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

	private void recalculateDistances(LinkedList<Cost> costs, Point3D p) {
		costs.forEach((cost) -> {
			cost.cost = c.distance3d(cost.p.getPoint(), cost.f.getPoint());
		});

	}

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
	 * Sonic movement algorithm.
	 */
	public void play() {

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