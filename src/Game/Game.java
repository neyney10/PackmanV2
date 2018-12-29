package Game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import Files_format.Csv;
import GUI.GameSpirit;
import GameObjects.*;
import Geom.Point3D;
import Maps.Map;

/**
 * Game object is a set of many GameObjects.
 * it contains a Map for converting from geodetic coordinates to XY pixel coordinates on screen.
 * it's also a BasicGameSpiritFactory allowing it to convert GameObjects into GameSpirit for visual representation.
 */
public class Game implements BasicGameSpiritFactory {

    // GameObjects
    private TreeSet<GameObject> objects;
    // Map, have current map coordinates and background image.
    private Map map;
    // id counter for GameObjects.
    private int id = 0;

    /**
     * [Constructor] <br>
     * an empty constructor, creates an empty Game object.
     */
    public Game() {
        this.objects = new TreeSet<>();
    }
    public Game(ArrayList<String> gameData){
        Iterator<String> iter = gameData.iterator();
        String line;
        String[] data;
        GameObject obj = null;
        objects = new TreeSet<>();
            while (iter.hasNext()){
                line = iter.next();
                data = line.split(",");
                if (data[0].compareTo("P")==0)
                    obj = new Packman(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4]),Integer.parseInt(data[1]),Double.parseDouble(data[5]),Double.parseDouble(data[6]));
                if (data[0].compareTo("F")==0)
                    obj = new Fruit(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4]),Integer.parseInt(data[1]));
                if (data[0].compareTo("G")==0)
                    obj = new Ghost(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4]),Integer.parseInt(data[1]),Double.parseDouble(data[5]),Double.parseDouble(data[6]));
                if (data[0].compareTo("M")==0)
                    obj = new Player(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4]),Integer.parseInt(data[1]),Double.parseDouble(data[5]),Double.parseDouble(data[6]));
                if (data[0].compareTo("B")==0)
                    obj = new Box(new Point3D(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4])),new Point3D(Double.parseDouble(data[5]),Double.parseDouble(data[6]),Double.parseDouble(data[7])));
                objects.add(obj);
            }
    }
    /**
     * [Constructor] <br>
     * a constructor that receiveds a TreeSet of game Objects.
     * @param objects
     */
    public Game(TreeSet<GameObject> objects) {
        setObjects(objects);
        id = getMaxID()+1;
    }

    /**
     * [Constructor] <br>
     * a Constructor that receives a CSV game file's path and using the "Csv" class to build it
     * into a Game object.
     * @param path
     */
    public Game(String path) {
        setObjects(Csv.read(path));
        id = getMaxID()+1;
    }

    /**
     * Export this GameObject into a CSV file (save), using the "Csv" class to build the save file.
     * @param path - muts contain path + file name + extension of ".csv".
     */
    public void toCsv(String path){
        Csv.build(path,getObjects());
    }

    /**
     * Returning this GameObjects TreeSet.
     * @return TreeSet<GameObject> of gameobjects.
     */
    public TreeSet<GameObject> getObjects() {
        return objects;
    }

    /**
     * set a new GameObjects set.
     * @param objects
     */
    public void setObjects(TreeSet<GameObject> objects) {
        this.objects = new TreeSet<>(objects);
        id = getMaxID()+1;
        
    }
    
    /**
     * Retrieves this game's iterator. of GameObjects.
     * @return Iterator<GameObject> - the gameObjects are ordered by Type.
     */
    public Iterator<GameObject> iterator() {
    	return objects.iterator();
    }

    /**
     * Returns if the game contain no objects (true), else
     * false.
     * @return true if empty, else false.
     */
    public boolean isEmpty() {
        if(objects == null)
            return true;
            
        return objects.isEmpty();
    }

    /**
     * Adding a single GameObject. <br>
     * NOTE: changing the GameObjects ID upon inserting.
     * @param GameObject of gameobject.
     */
    public void addGameObject(GameObject obj) {
        obj.setId(generateID());
        objects.add(obj);
    }
    
    /**
     * creates a copy of TreeSet containing same type object only, and returning it's iterator <br>
     * example: set "Pacman" object which is a subclass of GameObject and it will return an iterator over all pacmans in the set.<br>
     * usage example: typeIterator(new Pacman(0)); <br>
     * or if have a current pacman object already named "p1" then: typeIterator(p1);
     * @return iterator.
     */
    public Iterator<GameObject> typeIterator(GameObject example) {
    	GameObject lowestObject = example.clone();
    	lowestObject.setId(0);
    	
    	GameObject heighestObject = example.clone();
    	heighestObject.setId(Integer.MAX_VALUE);

    	return objects.subSet(lowestObject, heighestObject).iterator();
    }
    

    /**
     * Get the Game's map.
     * @return
     */
    public Map getMap() {
        return map;
    }

    /**
     * set a new map.
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * getx the highest id number that the GameObject has.
     * @return the highest id found.
     */
    private int getMaxID() {
        if(isEmpty())
            return -1;

        int max = -5;
        int objectID;
        Iterator<GameObject> iter = iterator();
        while(iter.hasNext()) {
            objectID = iter.next().getId();
            if(objectID > max)
                max = objectID;
        }

        return max;
    }

    /**
     * Get a new ID from this game container.
     * @return a brand new ID!
     */
    public int generateID() {
        return id++;
    }


	@Override
	public GameSpirit createGameSpirit(BasicGameSpirit obj) {
		Point p = map.getLocationOnScreen(obj);
		return createGameSpiritXY(obj,p.x,p.y);

	}


	@Override
	public GameSpirit createGameSpiritXY(BasicGameSpirit obj, int x, int y) {
		Point p = map.transformByScale(x, y);
		GameSpirit gameComponent = new GameSpirit(obj, p.x, p.y);
		map.updateLocationOnScreen(gameComponent);
		return gameComponent;
	}
	
}
