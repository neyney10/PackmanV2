package Files_format;

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
import java.util.Iterator;

public class Path2Kml {
    private static DocumentBuilderFactory factory;
    private static DocumentBuilder builder;
    private static Document document;
    private static Element doc;

    private Path2Kml(){}

    public static void create(Solutions solutions, String filePath){
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            setupKml(solutions.iterator());//setup
            int count = 1;
            for (Path p:solutions) {
                addPath(p,count);
                count++;
            }
            createFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * addPath add a new path to kml
     * @param path Path a collection of coordinates
     * @param num int id of path
     */
    private static void addPath(Path path,int num){

        Element placemark = document.createElement("Placemark");
        doc.appendChild(placemark);
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode("Path "+num));
        placemark.appendChild(name);

        Element styleUrl = document.createElement("styleUrl");
        styleUrl.appendChild(document.createTextNode("#color_path@"+num));
        placemark.appendChild(styleUrl);

        Element lineString = document.createElement("LineString");
        placemark.appendChild(lineString);

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
            strCoords.append(point.z());
            strCoords.append("\n");
        }
        coord.appendChild(document.createTextNode(strCoords.toString()));
        lineString.appendChild(coord);
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
        //setStyleIcon("Packman","GameData/pacman_icon.png");
        //setStyleIcon("Fruit","GameData/fruit_icon.png");
        //set colors for paths
        Path curr_path;
        int count = 1;
        while(solutionsIterator.hasNext()){
            curr_path = solutionsIterator.next();
            setStylePath(curr_path.getColor(),"color_path@"+count);
            count++;
        }
    }

    /**
     * setStyleIcon creates icon of game objects
     * @param name String icon name
     * @param fileName String path and filename
     */
    private static void setStyleIcon(String name,String fileName){
        Element style = document.createElement("style");
        style.setAttribute("id",name);
        doc.appendChild(style);

        Element iconStyle = document.createElement("IconStyle");
        style.appendChild(iconStyle);

        Element icon = document.createElement("Icon");
        iconStyle.appendChild(icon);

        Element href = document.createElement("href");
        href.appendChild(document.createTextNode(fileName));
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
        char[] charArray = rgb.toCharArray();
        char[] bgr = new char[6];
        for (int i = 0; i < bgr.length; i++) {
            bgr[i] = charArray[bgr.length-1-i];
        }
        return String.valueOf(bgr);
    }
}
