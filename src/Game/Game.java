package Game;

import Files_format.Csv;
import GameObjects.GameObject;
import Maps.Map;

import java.util.TreeSet;

public class Game {
    //private ArrayList<GameObject> objects;
    private TreeSet<GameObject> objects;
    private Map map = new Map(); // temp
    
    
    public Game(TreeSet<GameObject> objects) {
        setObjects(objects);
    }


    public Game(String path) {
        setObjects(Csv.read(path));
    }
    public void toCsv(String path){
        Csv.build(path,getObjects());
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
