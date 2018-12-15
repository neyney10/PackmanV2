package GUI;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Using FlyweightFactory / virtual proxy / factory design pattern
 * @author Neyney
 *
 */
public final class ImageFactory {

	private static HashMap<String, Image> images = new HashMap<>();
	
	private ImageFactory() {};
	
	/**
	 * Caching images...
	 */
	public static Image getImageFromDisk(String imagePath) {
		Image img = images.get(imagePath);
		
		if(img == null) {
			img = loadImage(imagePath);
			images.put(imagePath, img);
		}
		
		return img;
	}
	
	
	/**
	 * Loading an Image from file.
	 * @see: https://stackoverflow.com/questions/18777893/jframe-background-image
	 * @param path to the file, Ex: "GameData\\Pacman.png" for Windows system.
	 * @return Image Object (can be casted into BufferedImage)
	 */
	private static Image loadImage(String path) {
		BufferedImage i = null;
		try {
			i =  ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return i;//i.getScaledInstance(i.getWidth(), i.getWidth(), Image.SCALE_SMOOTH);
	}
}
