package GUI;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JPanel;

import Game.Game;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.TYPE;
import Geom.Point3D;
import Maps.Map;
import Maps.MapFactory;
import Maps.MapFactory.MapType;
import Path.Path;
import Robot.Play;

/**
 * A JPanel component which handles all the game interface, updating locations,
 * handles gameSpirits and drawing.
 */
public class JBackground extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3817775198749911544L;
	private boolean dropMode = false; 
	private GameObject dropItem;
	private Game game;
	private Map map;
	private Play play; // Boaz's Play object manager/server

	// for box creation with mouse click
	private Point3D pbox1, pbox2;

	/**
	 * [Constructor] <br>
	 * Creates a new JBackground object, initializing new defualt Map.
	 */
	public JBackground() {
		super();
		setLayout(null);
		addMouseListener(this);
		// default map
		map = MapFactory.getMap(MapType.ArielUniversity);
	}

	public JBackground(Game game) {
		this();
		setGame(game);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(game == null)
		{
			g.drawImage(this.getMap().getBackground(), 0, 0, getWidth(), getHeight(), this);
			return;
		}
		else
			g.drawImage(game.getMap().getBackground(), 0, 0, getWidth(), getHeight(), this);

		// set new Game object from play.getBoard();
		if(play.isRuning())
			game = new Game(play.getBoard());

		Point position;
		GameObject obj;
		Map m = game.getMap();
		Iterator<GameObject> iter = game.iterator();
		while(iter.hasNext()) {
			obj = iter.next();
			position = map.getLocationOnScreen(obj);
			//obj.setSpirit(MyFrame.rotateImage(obj.getSpirit(), obj.getOrientation()));
			
			//if(obj instanceof Box)  -> then g.drawRect
			
			g.drawImage(
				obj.getSpirit(), 
				position.x-obj.getInitialWidth()/2, 
				position.y-obj.getInitialHeight()/2, 
				(int)(obj.getInitialWidth()*map.getScaleFactorX()), 
				(int)(obj.getInitialHeight()*map.getScaleFactorY()), 
				this);
		}

	}

	/**
	 * reloading all game components and objects.
	 */
	public void refreshGameUI() {
		if (game == null)
			return;

		removeAll();

		if (!game.isEmpty())
			for (GameObject obj : game.getObjects())
				add(game.createGameSpirit(obj));

		repaint();
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
		// ONLY FOR DEBUG
		if (!dropMode || dropItem == null)
			return;

		if(game == null) 
			setGame(new Game());

		// get the game's map for calculating coordinates.
		Map m = game.getMap();

		if(play.isRuning()) {
			Point3D playerPos3D = game.getPlayer().getPoint();
			Point playerPos2D = m.getLocationOnScreen(playerPos3D);
			play.rotate(m.getAngleRaw(playerPos2D, e.getPoint()));
		}

		if(dropItem.getType() == TYPE.B) {
			// get the point from click and calculate it's position and coordinates with the Map object.
			pbox1 = m.getLocationFromScreen(e.getPoint());
			return;
		}
			
		
		// get a new Clone of the item.
		dropItem = dropItem.clone();

		// get the point from click and calculate it's position and coordinates with the Map object.
		Point3D p3d = m.getLocationFromScreen(e.getPoint()); //m.transformByScale(e.getX(), e.getY())

		// set the new computed point.
		dropItem.setPoint(p3d);
		dropItem.setId(game.generateID()); 
		// 
		// generate a Graphic element from this object.
		GameSpirit gs = game.createGameSpiritXY(dropItem,e.getX(),e.getY());

		// add this game spirit and game object into game and to this graphic component.
		getGame().addGameObject(dropItem);
		add(gs);

		repaint();

		if (MyFrame.DEBUG) {

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!dropMode || dropItem == null)
			return;

		if(dropItem.getType() == TYPE.B) {
			Map m = game.getMap();

			// get the point from click and calculate it's position and coordinates with the Map object.
			pbox2 = m.getLocationFromScreen(e.getPoint());

			GameSpirit gs = game.createGameSpiritXY(dropItem,e.getX(),e.getY());
			//Box box = (Box) dropItem;
			//box.setPoints(pbox1,pbox2);

			// add this game spirit and game object into game and to this graphic component.
			//getGame().addGameObject(box);
			add(gs);
	
			repaint();
		}
	}

	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;

		if (game == null)
			return;
		if(game.getMap() == null)
			game.setMap(this.map);
		
		refreshGameUI();
	}
	
	/**
	 * returns this game's map, if the game does not have a map
	 * then returning a default map or last used map.
	 * @return
	 */
	public Map getMap() {
		if(game!=null && game.getMap() != null)
			return game.getMap();
		return map;
	}

	/**
	 * set a new Map for this defualt map of JBackground and set the map into
	 * the "Game" object of JBackground.
	 * and then RefreshingGameUI. in O(N) when N is the GameObjects.
	 * @param map
	 */
	public void setMap(Map map) {
		this.map = map;
		
		if (game == null)
			return;
		
		game.setMap(this.map);
		refreshGameUI();
		
	}
	/**
	 *  is dropping mode for mouse click
	 * @return true if in drop mode
	 */
	public boolean isDropMode() {
		return this.dropMode;
	}

	/**
	 * change the drop mode
	 * @param boolean true to activate, false to deactivate
	 */
	public void setDropMode(boolean dropMode) {
		this.dropMode = dropMode;
	}

	/**
	 * get the item to drop in dropMode
	 * @return GameObject as the item to drop.
	 */
	public GameObject getDropItem() {
		return this.dropItem;
	}

	/**
	 * set a drop item in dropMode
	 * @param dropItem GameObject to drop
	 */
	public void setDropItem(GameObject dropItem) {
		this.dropItem = dropItem;
	}


	/**
	 * get the current play object.
	 * @return Play - Boaz's play objects.
	 */
	public Play getPlay() {
		return this.play;
	}

	/**
	 * set a new play object.
	 * @param Boaz's play object.
	 */
	public void setPlay(Play play) {
		this.play = play;
	}

	
	/**
	 * updates the new window (frame) size.
	 * @param width screen width
	 * @param height screen height
	 */
	public void updateMapWithNewScreenSize(int width, int height) {
		map.updateScreenRange(width, height);
		if(game == null || game.getMap() == null)
			return;
		
		game.getMap().updateScreenRange(width, height);
	}

}
