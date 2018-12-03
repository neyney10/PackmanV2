package Game;

import GameObjects.GameObject;

import java.util.ArrayList;

public class Game {
    ArrayList<GameObject> objects;

    public Game(ArrayList<GameObject> objects) {
        setObjects(objects);
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<GameObject> objects) {
        this.objects = objects;
    }
}
