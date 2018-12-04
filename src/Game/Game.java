package Game;

import GameObjects.GameObject;
import Maps.Map;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Game {
    private ArrayList<GameObject> objects;
    private Map map;
    
    
    public Game(ArrayList<GameObject> objects) {
        setObjects(objects);
    }
    
    public Game(String path) {
    	
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<GameObject> objects) {
        this.objects = objects;
    }
}
