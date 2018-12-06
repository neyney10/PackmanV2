package GUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import GameObjects.GameObject;
import Maps.Map;

public class GameSpirit extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 3817775198749911544L;
	public Image img;
	private int startWidth, startHeight, startX, startY;
	

	public GameSpirit(int x,int y, int width, int height, Image img) {
		super();
		this.img = img;
		setBounds(x - width/2, y - height/2, width, height); 
		addMouseListener(this);
		setToolTipText("<html><h3>[GameSpirit] " + "("+x+","+y+")</h3></html>");  // TODO: update it every move.
		
		/////////////////////////////////////
		// Used for Re-scaling of MyFrame. //
		/////////////////////////////////////
		this.startWidth = width;
		this.startHeight = height; 
		this.startX = x - width/2;
		this.startY = y - height/2; 
	}
	
	// TEMP ONLY
	public GameSpirit(GameObject obj, Map map) {
		this(map.getLocationOnScreen(obj).x, map.getLocationOnScreen(obj).y, obj.getInitialWidth(), obj.getInitialHeight(), obj.getSpirit());
	}
	
	/**
	 *  must have (x,y) pixel coordinates already processed position by a Map object. <br>
	 *  preferable way is to generate this object with the "Game" object with "createGameSpirit(GameObject obj)".
	 * @param obj GameObject
	 */
	public GameSpirit(GameObject obj, int x, int y) {
		this(x, y, obj.getInitialWidth(), obj.getInitialHeight(), obj.getSpirit());
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("CLICKED ON A GAMESPIRIT!!");
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
