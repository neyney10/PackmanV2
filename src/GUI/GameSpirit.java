package GUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import GameObjects.BasicGameSpirit;
import Maps.Map;

/**
 * GameSpirit objects is a graphic representation of GameObjects and is basicly the UI.
 * it extends JComponent for use inside a other swing elements.
 */
public class GameSpirit extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 3817775198749911544L;
	private Image img; // TODO: set getters and setters
	private int startWidth, startHeight, startX, startY;
	private BasicGameSpirit gameObj;

	/**
	 * [Constructor] <Br>
	 * creates new GameSpirit.
	 * @param x - RAW X pixel position.
	 * @param y - RAW Y pixel position.
	 * @param width - width of the image.
	 * @param height - height of the image.
	 * @param img - the image.
	 */
	public GameSpirit(int x,int y, int width, int height, Image img) {
		super();
		this.img = img;
		setBounds(x - width/2, y - height/2, width, height); 
		addMouseListener(this);
		setToolTipText("<html><h3>[GameSpirit] " + "("+x+","+y+")</h3></html>"); 
		
		/////////////////////////////////////
		// Used for Re-scaling of MyFrame. //
		/////////////////////////////////////
		this.startWidth = width;
		this.startHeight = height; 
		this.startX = x - width/2;
		this.startY = y - height/2; 
	}
	
	/**
	 * [Constructor] <br>
	 * Creates new GameSpirit object.
	 * @param obj BasicGameSpirit (can be GameObject)
	 * @param map Map for coordinates conversion of Geodetic points of GameObject to Pixels XY.
	 */
	public GameSpirit(BasicGameSpirit obj, Map map) {
		this(map.getLocationOnScreen(obj).x, map.getLocationOnScreen(obj).y, obj.getInitialWidth(), obj.getInitialHeight(), obj.getSpirit());
		this.setGameObj(obj);
	}
	
	/**
	 *  must have (x,y) pixel coordinates already processed position by a Map object. <br>
	 *  preferable way is to generate this object with the "Game" object with "createGameSpirit(GameObject obj)".
	 * @param obj GameObject
	 */
	public GameSpirit(BasicGameSpirit obj, int x, int y) {
		this(x, y, obj.getInitialWidth(), obj.getInitialHeight(), obj.getSpirit());
		this.setGameObj(obj);
	}
	
	/**
	 * Updating this GameSpirit location using "setLocation(x,y)", but updating GameSpirit's related properties 
	 * to match MyFrame's scaling algorithm. <br>
	 * use it for moving the component around.
	 * @param x position in pixels
	 * @param y position in pixels
	 */
	public void updateLocation(int x, int y)
	{
		startX = x - startWidth/2;
		startY = y - startHeight/2;
		setLocation(startX,startY);
		
		// update tooltip
		setToolTipText("<html><h3>[GameSpirit] " + "("+x+","+y+")</h3></html>");
	}
	
	/**
	 * Updating this GameSpirit size (width and height) using "setSize(w, h)", but updating GameSpirit's related properties
	 * to match MyFrame's scaling algorithm <br>
	 * use it for resizing the component (as Game component size).
	 * @param width
	 * @param height
	 */
	public void updateSize(int width, int height) {
		startWidth = width;
		startHeight = height;
		setSize(width, height);
	}
	
	/**
	 * Moving the GameSpirit by x pixels horizontally and y pixels vertically (as right / down are positive and 
	 * left / up are negative values)
	 * @param x amount of pixels to shift right >>
	 * @param y amount of pixels to shift down 
	 */
	public void moveByPixel(int x, int y) {
		updateLocation((startX + startWidth/2) + x, (startY + startHeight/2) + y);
	}
	
	
	/**
	 * Calling internally "updateLocation(x,y)" and "updateSize(w,h)".
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void updateLocationAndSize(int x, int y, int width, int height) {
		updateLocation(x, y);
		updateSize(width, height);
	}
	

	/**
	 * @return the startWidth
	 */
	public int getStartWidth() {
		return startWidth;
	}

	/**
	 * @param startWidth the startWidth to set
	 */
	public void setStartWidth(int startWidth) {
		this.startWidth = startWidth;
	}

	/**
	 * @return the startHeight
	 */
	public int getStartHeight() {
		return startHeight;
	}

	/**
	 * @param startHeight the startHeight to set
	 */
	public void setStartHeight(int startHeight) {
		this.startHeight = startHeight;
	}
	
	/**
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * @param startX the startX to set
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}

	/**
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * @param startY the startY to set
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	/**
	 * @return startX and startY
	 */
	public Point getStartLocation() {
		return new Point(getStartX(), getStartY());
	}
	
	/*
	 * [Developer Note] Status: Complete.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/*
		 Drawing this Object's Image at Current X,Y position 
		 with the dimensions of (from (0,0) to (width,height)).
		 Note: if using MyFrame than MyFrame class is re-scaling all of it's components when resized.
		 */
		g.drawImage(img, 0,0,getWidth(),getHeight(),this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {	
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("CLICKED ON A GAMESPIRIT!!");
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * @return the gameObj
	 */
	public BasicGameSpirit getGameObj() {
		return gameObj;
	}

	/**
	 * @param obj the gameObj to set
	 */
	public void setGameObj(BasicGameSpirit obj) {
		this.gameObj = obj;
	}

	/**
	 * Get this GameSpirit image, can be different from the underlying GameObject.
	 * @return Image as image sprite.
	 */
	public Image getImage() {
		return img;
	}

	/**
	 * Set new Image. (does not change the underlying GameObject image)
	 * @param img
	 */
	public void setImage(Image img) {
		this.img = img;
	}
	
	
	
}
