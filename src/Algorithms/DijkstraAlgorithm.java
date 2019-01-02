package Algorithms;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import Coords.MyCoords;
import Game.Game;
import GameObjects.Box;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Geom.Point3D;
import Maps.Map;
import Path.Path;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;
import graph.Node_Info;

public class DijkstraAlgorithm implements RobotPathFindingAlgorithm {

	Graph graph;
	Game game;
	////////
	MyCoords mc = new MyCoords();
	Intersect in;

	public DijkstraAlgorithm(Game game) {
		graph = new Graph();
		in = new Intersect(game.getMap());
		refreshGameStatus(game);
	}

	public void refreshGameStatus(Game game) {
		this.game = game;
		Iterator<GameObject> iter;

		// BOXES
		Box box;
		iter = game.typeIterator(new Box(0));
		while(iter.hasNext()) {
			box = (Box) iter.next();
			addBoxToGraph(box, game.getMap());
		}
		
		for(int i =0 ; i < graph.size() ; i++) {
			Vertex v1 = (Vertex) graph.getNodeByIndex(i);
			for(int j = 0 ; j < i ; j++) {
				Vertex v2 = (Vertex) graph.getNodeByIndex(j);

				if(!checkIntersect(v1,v2))  {
					graph.addEdge(v1.get_name(), v2.get_name(), mc.distance3d(v1.point, v2.point));
					//System.out.println(v1.get_name() + " | "+v2.get_name()); // TEMP
				}
			}
		}
		

	}

	
	@Override
	public Path calculate(Point3D source, Point3D destination) {
		Vertex sourceNode = new Vertex("0"+source, source);
		Vertex destinationNode = new Vertex("1"+destination, source);
		graph.add(sourceNode);
		graph.add(destinationNode);
		
		System.out.println(graph.size());
		// connect those two points to the graph by edges.
		for(int i = 0 ; i < graph.size() ; i ++) {
			Vertex node = (Vertex) graph.getNodeByIndex(i);

			//source
			double distance;
			if(!checkIntersect(sourceNode,node) && sourceNode != node) {
				distance = mc.distance3d(source, ((Vertex) graph.getNodeByIndex(i)).point);
				graph.addEdge(sourceNode.get_name(), node.get_name(), distance);
				//System.out.println("SOURCE ADD EDGE");
			}
			
			//destination
			if(!checkIntersect(destinationNode,node) && destinationNode != node) {
				distance = mc.distance3d(destination, ((Vertex) graph.getNodeByIndex(i)).point);
				graph.addEdge(destinationNode.get_name(),node.get_name(), distance);
				//System.out.println(graph.getNodeByName(destinationNode.get_name()) +" | "+(graph.getNodeByName(node.get_name())));
				//System.out.println("destination ADD EDGE");
			}
			
			System.out.println(node.get_ni());
		}
		System.out.println(sourceNode.get_ni());
		System.out.println(destinationNode.get_ni());

		// compute the shortest graph using the algorithm
		Graph_Algo.dijkstra(graph, sourceNode.get_name());
		//System.out.println(Arrays.toString(destinationNode.getPath().toArray())); //TEMP DEBUG
		return parseNodePath(destinationNode.getPath());
	}
	
	private Path parseNodePath(ArrayList<String> nodePath) {
		Path path = new Path();
		Vertex v;
		for(String nodeName : nodePath) {
			v = (Vertex) graph.getNodeByName(nodeName);
			path.add(v.point);
		}
		
		return path;
	}
	
	private boolean checkIntersect(Vertex v1, Vertex v2) {
		//TEMP TEST:
		Box box;
		Iterator<GameObject> iter = game.typeIterator(new Box(0));
		while(iter.hasNext()) {
			box = (Box) iter.next();
			Point vp1 = game.getMap().getLocationOnScreen(v1.point);
			Point vp2 = game.getMap().getLocationOnScreen(v2.point);
			Line2D line = new Line2D.Double(vp1,vp2);

			if(in.isLineIntersectsWithBox(line, box)) {
				return true;
			}
		}
		
		return false;
	}

	private void addBoxToGraph(Box box, Map map) {
		Point upperLeftP = map.getLocationOnScreen(box.getMin());
		Point lowerRightP = map.getLocationOnScreen(box.getMax());
		
		Point3D upperRight, lowerLeft, upperLeft, lowerRight;
		upperRight = map.getLocationFromScreen(new Point(lowerRightP.x + 3, upperLeftP.y - 3));
		lowerLeft = map.getLocationFromScreen(new Point(upperLeftP.x - 3, lowerRightP.y + 3));
		upperLeft = map.getLocationFromScreen(new Point(lowerRightP.x - 3, upperLeftP.y - 3));
		lowerRight = map.getLocationFromScreen(new Point(upperLeftP.x + 3, lowerRightP.y + 3));
		
		graph.add(new Vertex(""+(upperLeft), upperLeft)); 
		graph.add(new Vertex(""+(lowerRight), lowerRight));
		graph.add(new Vertex(""+(upperRight),upperRight)); 
		graph.add(new Vertex(""+(lowerLeft), lowerLeft));
	}
}



class Vertex extends Node {
	Point3D point;
	public Vertex(String s, Point3D point) {
		super(s);
		this.point = point;
	}
}


