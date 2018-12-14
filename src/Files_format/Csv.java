package Files_format;

import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.TYPE;
import Geom.Point3D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.TreeSet;

public class Csv {
    //private static ArrayList<GameObject> gameObjects;
    private static TreeSet<GameObject> gameObjects;//temp
    private static int type,id,lat,lon,alt,speed,radius;

    /**
     * Csv is a static class
     */
    private Csv(){}

    /**
     * read method parse Csv to java objects
     * @param path String path of file to parse
     * @return TreeSet<GameObject> parse of Csv data in file
     */
    static public TreeSet<GameObject> read(String path){
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            gameObjects = new TreeSet<>();
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
                    if (column[type].equals("P")) {
                        gameObjects.add(new Packman(Double.parseDouble(column[lon]), Double.parseDouble(column[lat]), Double.parseDouble(column[alt]),Integer.parseInt(column[id]), Double.parseDouble(column[speed]), Double.parseDouble(column[radius])));
                    }
                    else
                        gameObjects.add(new Fruit(Double.parseDouble(column[lon]),Double.parseDouble(column[lat]),Double.parseDouble(column[alt]),Integer.parseInt(column[id])));
                }
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gameObjects;
    }

    /**
     * dynamicPosition allows for the read to know which column is by its header
     * @param str String current title
     * @param i int position in array
     */
    private static void dynamicPosition(String str, int i){
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
            case "Speed":
            case "Speed/Weight":
                speed = i;
                break;
            case "Radius":
                radius = i;
                break;
        }
    }

    /**
     * build allows Csv to get a TreeSet of GameObject and build it in to Csv file
     * @param path String path of file to build and file name
     * @param gameObjects TreeSet<GameObject> data to insert to Csv file
     */
    public static void build(String path,TreeSet<GameObject> gameObjects){
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<GameObject> iterator = gameObjects.iterator();
            GameObject object;
            Packman packman;
            Fruit fruit;
            Point3D point;

            sb.append("Type,id,Lat,Lon,Alt,Speed,Radius\n");
            //setup data of GameObjects
            while (iterator.hasNext()) {
                object = iterator.next();
                point = object.getPoint();
                if (object.getType() == TYPE.P) {
                    packman = (Packman) object;
                    sb.append("P," + packman.getId() + "," + point.x() + "," + point.y() + "," + point.z() + "," + packman.getSpeed() + "," + packman.getRadius() + "\n");
                } else {
                    fruit = (Fruit) object;
                    sb.append("F," + fruit.getId() + "," + point.x() + "," + point.y() + "," + point.z() + "\n");
                }
            }
            //create file
            PrintWriter printWriter = new PrintWriter(new File(path));
            printWriter.write(sb.toString());
            printWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
