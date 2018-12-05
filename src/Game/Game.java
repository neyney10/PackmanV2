package Game;

import GameObjects.GameObject;
import Maps.Map;
import jdk.nashorn.api.tree.Tree;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Game {
    //private ArrayList<GameObject> objects;
    private TreeSet<GameObject> objects;
    private Map map = new Map(); // temp
    
    
    public Game(TreeSet<GameObject> objects) {
        setObjects(objects);
    }
    
    public Game(String path) {
    	
    }

    public TreeSet<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(TreeSet<GameObject> objects) {
        this.objects = objects;
    }

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
}
