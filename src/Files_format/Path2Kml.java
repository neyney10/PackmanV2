package Files_format;

import Coords.MyCoords;
import Game.Game;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.TYPE;
import Geom.Point3D;
import Path.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Path2Kml creates a kml from given paths from class solutions.
 * with basic features such as creating color for each path and basic animation.
 * NOTE: !!!!!the kml animation of packman motion is used by timespan in order to see it work you need both begin&end to work
 *      on slide!!!!!
 */
public class Path2Kml {
    private static DocumentBuilderFactory factory;
    private static DocumentBuilder builder;
    private static Document document;
    private static Element doc;
    private static Date start;
    private Path2Kml(){}

    /**
     * create creates a kml file from given solutions
     * @param game Game object of game
     * @param solutions
     * @param filePath
     */
    public static void create(Game game,Solutions solutions, String filePath){
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            setupKml(solutions.iterator());//setup
            start = new Date();
            Iterator<GameObject> iterator = game.typeIterator(new Packman(0));
            for (Path p:solutions) {
                addIconAnimation(p,(Packman) iterator.next());
                addPath(p);
            }
            createFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * addPath add a new path to kml
     * @param path Path a collection of coordinates
     */
    private static void addPath(Path path){
        int id = path.getID();
        Element pathmark = document.createElement("Placemark");
        doc.appendChild(pathmark);
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode("Path "+id));
        pathmark.appendChild(name);

        Element styleUrl = document.createElement("styleUrl");
        styleUrl.appendChild(document.createTextNode("#color_path@"+id));
        pathmark.appendChild(styleUrl);

        Element lineString = document.createElement("LineString");
        pathmark.appendChild(lineString);

        Element extrude = document.createElement("extrude");
        extrude.appendChild(document.createTextNode("1"));
        lineString.appendChild(extrude);

        Element tessellate = document.createElement("tessellate");
        tessellate.appendChild(document.createTextNode("1"));
        lineString.appendChild(tessellate);

        Element altitudeMode = document.createElement("altitudeMode");
        altitudeMode.appendChild(document.createTextNode("relativeToGround"));
        lineString.appendChild(altitudeMode);

        Element coord = document.createElement("coordinates");
        StringBuilder strCoords = new StringBuilder();
        Iterator<Point3D> points = path.iterator();
        Point3D point;
        while (points.hasNext()){
            point = points.next();
            strCoords.append(point.x());
            strCoords.append(",");
            strCoords.append(point.y());
            strCoords.append(",");
            strCoords.append(point.z()+1);
            strCoords.append("\n");
        }
        coord.appendChild(document.createTextNode(strCoords.toString()));
        lineString.appendChild(coord);
    }

    /**
     * AddIconAnimation add icons for current path with animation on time slide
     * @param path points of path
     * @param packman points of packman
     */
    private static void addIconAnimation(Path path,Packman packman){

        MyCoords myCoords = new MyCoords();
        Iterator<Point3D> points = path.iterator();
        Point3D p1,p2;

        Date date = new Date();
        date.setTime(start.getTime());
        String startingPoint = setTimeStamp(start);
        String begin = setTimeStamp(date);
        String end = setTimeStamp(date);

        if (points.hasNext()) {

            p1 = points.next();//point 1
            while (points.hasNext()) {
                p2 = points.next();//point 2
                date.setTime(date.getTime()+((long) (myCoords.distance3d(p1,p2)/packman.getSpeed()*1000)));//added time
                end = setTimeStamp(date);//end span
                createIcon(TYPE.P,p1,begin, end);//begin->end
                createIcon(TYPE.F,p2,startingPoint, end);//start->end
                begin = end;//progress
                p1 = p2;//progress
            }

            if (end!=startingPoint) {
                date.setTime(date.getTime() + 1000);//added time
                end = setTimeStamp(date);
            }

            createIcon(TYPE.P,p1,end,"non");
        }
    }

    /**
     * createIcon creates icon for given placemark
     * @param type enum type of game object
     * @param point Point3D coordinates of point
     * @param from String begin timespan begin time
     * @param to String end timespan end time
     */
    private static void createIcon(TYPE type,Point3D point,String from,String to){
        Element iconmark = document.createElement("Placemark");
        doc.appendChild(iconmark);

        Element timespan = document.createElement("TimeSpan");
        iconmark.appendChild(timespan);

        Element begin = document.createElement("begin");
        begin.appendChild(document.createTextNode(from));
        timespan.appendChild(begin);

        if (to.compareTo("non")!=0) {//if end appearance doesn't exist
            Element end = document.createElement("end");
            end.appendChild(document.createTextNode(to));
            timespan.appendChild(end);
        }

        Element styleIcon = document.createElement("styleUrl");
        if (type==TYPE.P)//image by type
            styleIcon.appendChild(document.createTextNode("#Packman"));
        else
            styleIcon.appendChild(document.createTextNode("#Fruit"));
        iconmark.appendChild(styleIcon);

        Element iconpoint = document.createElement("Point");
        iconmark.appendChild(iconpoint);

        Element coords = document.createElement("coordinates");
        coords.appendChild(document.createTextNode(point.x() + "," + point.y() + "," + (point.z()+1)));
        iconpoint.appendChild(coords);
    }
    /**
     * creates String timestamp for kml
     * @param date Date time of current point
     * @return String of Date for kml
     */
    private static String setTimeStamp(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = dateFormat.format(date);
        str = str.replace(' ','T')+"Z";
        return str;
    }
    /**
     * setupKml setup of kml
     * @param solutionsIterator Iterator of path for colors and id.
     */
    private static void setupKml(Iterator<Path> solutionsIterator) {
        Element root = document.createElement("kml");
        root.setAttribute("xmlns","http://www.opengis.net/kml/2.2");
        document.appendChild(root);

        doc = document.createElement("Document");
        root.appendChild(doc);

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode("Paths"));
        doc.appendChild(name);
        //set icons
        setStyleIcon("Packman","https://upload.wikimedia.org/wikipedia/en/thumb/e/e4/Original_PacMan2.png/170px-Original_PacMan2.png");
        setStyleIcon("Fruit","https://i.pinimg.com/originals/89/15/31/891531acedba5bf6d96ee66a87e7d696.png");
        //set colors for paths
        Path curr_path;
        while(solutionsIterator.hasNext()){
            curr_path = solutionsIterator.next();
            setStylePath(curr_path.getColor(),"color_path@"+curr_path.getID());
        }
    }

    /**
     * setStyleIcon creates icon of game objects
     * @param name String icon name
     * @param filePath String path and filename
     */
    private static void setStyleIcon(String name,String filePath){
        Element style = document.createElement("Style");
        style.setAttribute("id",name);
        doc.appendChild(style);

        Element iconStyle = document.createElement("IconStyle");
        style.appendChild(iconStyle);

        Element icon = document.createElement("Icon");
        iconStyle.appendChild(icon);

        Element href = document.createElement("href");
        href.appendChild(document.createTextNode(filePath));
        icon.appendChild(href);
    }

    /**
     * setStylePath creates a path style color to use, will match color from GUI.
     * @param hexColor String receive rgb in hex of color
     * @param colorName String name of color by path id
     */
    private  static void setStylePath(String hexColor, String colorName){
        hexColor = rgbToBgr(hexColor.substring(1));//turns rgb to bgr
        Element style = document.createElement("Style");
        style.setAttribute("id",colorName);
        doc.appendChild(style);

        Element lineStyle = document.createElement("LineStyle");
        style.appendChild(lineStyle);

        Element colorLine = document.createElement("color");
        colorLine.appendChild(document.createTextNode("ff"+hexColor));
        lineStyle.appendChild(colorLine);

        Element width = document.createElement("width");
        width.appendChild(document.createTextNode("4"));
        lineStyle.appendChild(width);

        Element polyStyle = document.createElement("PolyStyle");
        style.appendChild(polyStyle);

        Element colorPoly = document.createElement("color");
        colorPoly.appendChild(document.createTextNode("5f"+hexColor));
        polyStyle.appendChild(colorPoly);

    }

    /**
     * createFile creates file of kml with given data in class
     * @param fileName String path and filename
     * @throws TransformerException encase of Transformer Exception will throw to upper Method.
     */
    private static void createFile(String fileName) throws TransformerException {
        if (!fileName.toLowerCase().endsWith(".kml")){
            fileName += ".kml";
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName));
        transformer.transform(domSource,streamResult);
    }

    /**
     * rgbToBgr reverse order of rgb,(kml works with abgr which stand for alpha,blue,green,red)
     * @param rgb String hex of rgb
     * @return String hex of rgbToBgr
     */
    private static String rgbToBgr(String rgb){
        char[] arr = rgb.toCharArray();
        String bgr = ""+arr[4]+arr[5]+arr[2]+arr[3]+arr[1]+arr[2];
        return String.valueOf(bgr);
    }
}
