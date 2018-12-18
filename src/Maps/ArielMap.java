package Maps;

import java.awt.Rectangle;
import GUI.ImageFactory;

/**
 * A game map of Ariel University, (Enum value: MapType.ArielUniversity)
 */
public final class ArielMap extends Map{
	/**
	 * Creates a new ArielMap map, with Ariel's coordinates and background image. <Br>
	 * default image path: "GameData\\Ariel1.png"
	 */
	public ArielMap() {
		super();
		// default values
		double x1 = 35.2024f; // upper left corner
		double y1 = 32.1056f;
		double x2 = 35.2121f; // lower right corner
		double y2 = 32.1019f;
		 
		mapRange = new MapRange(x1,y1,x2,y2);
		screenRange = new Rectangle(0, 0, 1433, 642);

		background = ImageFactory.getImageFromDisk("GameData\\Ariel1.png");
	}
	


}
