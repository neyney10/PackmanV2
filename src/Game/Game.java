package Game;

import java.awt.Point;
import java.util.Iterator;
import java.util.TreeSet;

import Files_format.Csv;
import GUI.GameSpirit;
import GameObjects.BasicGameSpirit;
import GameObjects.GameObject;
import Maps.Map;
import Maps.MapFactory;
import Maps.MapFactory.MapType;

public class Game implements BasicGameSpiritFactory {

    private TreeSet<GameObject> objects;
    private Map map = MapFactory.getMap(MapType.ArielUniversity); // temp

    /**
     * [Constructor] <br>
     * an empty constructor, creates an empty Game object.
     */
    public Game() {
        this.objects = new TreeSet<>();
    }

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
    
    public Iterator<GameObject> iterator() {
    	return objects.iterator();
    }

    public boolean isEmpty() {
        if(objects == null)
            return true;
            
        return objects.isEmpty();
    }

    public void addGameObject(GameObject obj) {
        objects.add(obj);
    }
    
    /**
     * creates a copy of TreeSet containing same type object only, and returning it's iterator <br>
     * example: set "Pacman" object which is a subclass of GameObject and it will return an iterator over all pacmans in the set.<br>
     * usage example: typeIterator(new Pacman()); <br>
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
    

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }


	@Override
	public GameSpirit createGameSpirit(BasicGameSpirit obj) {
		Point p = map.getLocationOnScreen(obj);
		GameSpirit gameComponent = new GameSpirit(obj, p.x, p.y);
		map.updateLocationOnScreen(gameComponent);
		return gameComponent;
	}


	@Override
	public GameSpirit createGameSpiritXY(BasicGameSpirit obj, int x, int y) {
		Point p = map.transformByScale(x, y);
		GameSpirit gameComponent = new GameSpirit(obj, p.x, p.y);
		map.updateLocationOnScreen(gameComponent);
		return gameComponent;
	}
	
}
