package Files_format;

import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.TYPE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CsvReader {
    private static ArrayList<GameObject> gameObjects;
    private static int type,id,lat,lon,alt,speed,radius;
    private CsvReader(){}
    static public ArrayList<GameObject> read(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            gameObjects = new ArrayList<>();
            String line;
            String[] column;
            int count = 0;
            while ((line = reader.readLine()) != null){
                column = line.split(",");
                if (count==0){
                    for (int i = 0; i < column.length; i++) {
                        dynamicPosition(column[i],i);
                    }
                }
                else {
                    if (column[type].compareTo("P")==0) {
                        gameObjects.add(new Packman(Double.parseDouble(column[lat]), Double.parseDouble(column[lon]), Double.parseDouble(column[alt]),Integer.parseInt(column[id]), TYPE.P, Double.parseDouble(column[speed]), Double.parseDouble(column[radius])));
                    }
                    else
                        gameObjects.add(new Fruit(Double.parseDouble(column[lat]),Double.parseDouble(column[lon]),Double.parseDouble(column[alt]),Integer.parseInt(column[id]), TYPE.F));
                }
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gameObjects;
    }
    public static void dynamicPosition(String str, int i){
        switch (str){
            case "Type":
                type = i;
                break;
            case "id":
                id = i;
                break;
            case "Lat":
                lat = i;
                break;
            case "Lon":
                lon = i;
                break;
            case "Alt":
                alt = i;
                break;
            case "Speed/Weight":
                speed = i;
                break;
            case "Radius":
                radius = i;
                break;
        }
    }
}
