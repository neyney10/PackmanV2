package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JPanel;

import Algorithms.SonicAlgorithmV2;
import Game.Game;
import GameObjects.Box;
import GameObjects.GameObject;
import GameObjects.Packman;
import GameObjects.Player;
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
	private boolean showStatistics;
	
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
		game = new Game();
		game.setMap(map);

		// default play game
		play = new Play();
		
		//default showing statistics
		setShowStatistics(true);
	}

	public JBackground(Game game) {
		this();
		setGame(game);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (game == null) {
			g.drawImage(this.getMap().getBackground(), 0, 0, getWidth(), getHeight(), this);
			return;
		} else
			g.drawImage(game.getMap().getBackground(), 0, 0, getWidth(), getHeight(), this);

		// set new Game object from play.getBoard();
		if (play.isRuning()) {
			double angle = game.getPlayer().getOrientation();
			synchronized (game) {
				game.refreshGameStatus(play.getBoard());
				game.getPlayer().setOrientation(angle);
			}

		}

		Point position;
		GameObject obj;
		Map m = game.getMap();
		Iterator<GameObject> iter = game.iterator();
		while (iter.hasNext()) {
			obj = iter.next();
			position = map.getLocationOnScreen(obj);

			if (obj instanceof Box) {// -> then g.drawRect
				Box box = (Box) obj;
				Point position2 = map.getLocationOnScreen(box.getMax());

				int width, height;
				width = Math.abs(position2.x - position.x);
				height = Math.abs(position2.y - position.y);


				g.fillRect(position.x, position.y, (int)(width),(int) (height));
				continue;
			} else if (obj instanceof Player) {
				Player player = (Player) obj;
				player.setSpirit(MyFrame.rotateImage(player.getSpirit(), (int) player.getOrientation()));
			}

			g.drawImage(
					obj.getSpirit(),
					position.x - obj.getInitialWidth() / 2,
					position.y - obj.getInitialHeight() / 2, 
					(int) (obj.getInitialWidth() * map.getScaleFactorX()),
					(int) (obj.getInitialHeight() * map.getScaleFactorY()), 
					this);
		}
		if(showStatistics)
			paintGameStatistics(g, 5, 5);

		Iterator<GameObject> iterp = game.typeIterator(new Packman(0));

		Packman p;
		Path path;
		while (iterp.hasNext()) {
			p = (Packman) iterp.next();
			path = p.getPath();

			if (path == null || path.getPointAmount() < 2)
				continue;

			path.paint(g, m);
		}


		// TEMP
		path = null;
		if(MyFrame.getInstance().path != null)
			path = MyFrame.getInstance().path;
		if (path != null && path.getPointAmount() >= 2)
			path.paint(g, game.getMap());



	}

	/**
	 * Painting Play.getStatistics() with a small blue panel
	 * @param g - graphics object to draw with/on
	 * @param x - x position on screen to start painting
	 * @param y - y position on screen to start painting
	 */
	private void paintGameStatistics(Graphics g, int x,int y) {
		Graphics2D g2d = (Graphics2D) g;

		// COLOR
		int alpha = 127; // 50% transparent
		Color rectColor = new Color(111, 111, 222, alpha);
		Color headColor = new Color(55, 55, 244, alpha+35);
		Color borderColor = new Color(55, 55, 155, alpha+55);

		// FONT
		int fontSize = 16;
		Font textFont = new Font("Arial", Font.BOLD, fontSize);

		// SET CUSTOMIZED CONFIGURATION
		g.setColor(rectColor);
		g.setFont(textFont);
		int lineSpace = 6;
		int overflowBarrier = 23;
		int roundDiameter = 15;

		g2d.fillRoundRect(x, y, 200, 133, roundDiameter, roundDiameter);
		g.setColor(headColor);
		g2d.fillRoundRect(x, y, 200, 22, roundDiameter, roundDiameter);
		g.setColor(borderColor);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawRoundRect(x, y, 200, 133,roundDiameter,roundDiameter);

		g.setColor(Color.BLACK);

		String[] lines = play.getStatistics().split(",");
		for(String line : lines) {
			if(line.length() > overflowBarrier)
				line = line.substring(0, overflowBarrier);
			
		    g.drawString(line, x+8, y+fontSize);
			y += fontSize+lineSpace;
		}

	}


	/**
	 * reloading all game components and objects.
	 */
	public void refreshGameUI() {
		if (game == null)
			return;

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
		if (getGame() == null)
			return;

		// get the game's map for calculating coordinates.
		Map m = game.getMap();

		if (play.isRuning()) {
			Point3D playerPos3D = game.getPlayer().getPoint();
			Point playerPos2D = m.getLocationOnScreen(playerPos3D);
			double angle = m.getAngleRaw(playerPos2D, e.getPoint()) + 90;
			play.rotate(angle);
			game.getPlayer().setOrientation(angle);
			repaint();
			return;
		} else if (!dropMode || dropItem == null)
			return;

		if (dropItem.getType() == TYPE.B) {
			// get the point from click and calculate it's position and coordinates with the
			// Map object.
			pbox1 = m.getLocationFromScreen(e.getPoint());
			return;
		} else if (dropItem.getType() == TYPE.M) {
			if (getGame().getPlayer() != null) {
				getGame().getPlayer().setPoint(m.getLocationFromScreen(e.getPoint()));
				repaint();
				return;
			}
		}

		// get a new Clone of the item.
		dropItem = dropItem.clone();

		// get the point from click and calculate it's position and coordinates with the
		// Map object.
		Point3D p3d = m.getLocationFromScreen(e.getPoint()); // m.transformByScale(e.getX(), e.getY())

		// set the new computed point.
		dropItem.setPoint(p3d);
		dropItem.setId(game.generateID());

		if (dropItem.getType() == TYPE.M) {
			getGame().setPlayer((Player) dropItem);
		} else
			getGame().addGameObject(dropItem);

		repaint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!dropMode || dropItem == null || getGame() == null)
			return;

		if (dropItem.getType() == TYPE.B) {
			Map m = game.getMap();

			// get the point from click and calculate it's position and coordinates with the
			// Map object.
			pbox2 = m.getLocationFromScreen(e.getPoint());

			Box box = ((Box) dropItem).clone();
			box.setPoints(pbox1, pbox2);
			box.setId(game.generateID());

			// add this game spirit and game object into game and to this graphic component.
			getGame().addGameObject(box);
			System.out.println("ADDED BLACKY BOX");
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
	 * set a new Game object to this panel, setting a default map.
	 * 
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;

		if (game == null)
			return;
		if (game.getMap() == null)
			game.setMap(this.map);

		//NOTE: TEMP
		SonicAlgorithmV2 sa = new SonicAlgorithmV2(game, null);
		sa.calcPacmanPathV2();
		TreeSet<GameObject> ts = new TreeSet<>();
		ts.addAll(sa.fruits);
		ts.addAll(sa.pacmans);
		ts.addAll(sa.boxes);
		this.game.setObjects(ts);
		Point3D p;
		if((p = sa.findLatestEatenFruitPosition()) != null)
			System.out.println(p +" | "+ game.getMap().getLocationOnScreen(p));

		refreshGameUI();

	}

	/**
	 * set a new Game object to this panel, setting a default map.
	 * 
	 * @param game the game to set
	 */
	private void setGame(Game game, double playerOrientation) {
		//this.game = game;
		game.setMap(this.map);
		game.getPlayer().setOrientation(playerOrientation);

	}

	/**
	 * returns this game's map, if the game does not have a map then returning a
	 * default map or last used map.
	 * 
	 * @return
	 */
	public Map getMap() {
		if (game != null && game.getMap() != null)
			return game.getMap();
		return map;
	}

	/**
	 * set a new Map for this defualt map of JBackground and set the map into the
	 * "Game" object of JBackground. and then RefreshingGameUI. in O(N) when N is
	 * the GameObjects.
	 * 
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
	 * is dropping mode for mouse click
	 * 
	 * @return true if in drop mode
	 */
	public boolean isDropMode() {
		return this.dropMode;
	}

	/**
	 * change the drop mode
	 * 
	 * @param boolean true to activate, false to deactivate
	 */
	public void setDropMode(boolean dropMode) {
		this.dropMode = dropMode;
	}

	/**
	 * get the item to drop in dropMode
	 * 
	 * @return GameObject as the item to drop.
	 */
	public GameObject getDropItem() {
		return this.dropItem;
	}

	/**
	 * set a drop item in dropMode
	 * 
	 * @param dropItem GameObject to drop
	 */
	public void setDropItem(GameObject dropItem) {
		this.dropItem = dropItem;
	}

	/**
	 * get the current play object.
	 * 
	 * @return Play - Boaz's play objects.
	 */
	public Play getPlay() {
		return this.play;
	}

	/**
	 * set a new play object.
	 * 
	 * @param Boaz's play object.
	 */
	public void setPlay(Play play) {
		this.play = play;
	}

	/**
	 * updates the new window (frame) size.
	 * 
	 * @param width  screen width
	 * @param height screen height
	 */
	public void updateMapWithNewScreenSize(int width, int height) {
		map.updateScreenRange(width, height);
		if (game == null || game.getMap() == null)
			return;

		game.getMap().updateScreenRange(width, height);
	}

	/**
	 * @return the showStatistics
	 */
	public boolean isShowStatistics() {
		return showStatistics;
	}

	/**
	 * @param showStatistics the showStatistics to set
	 */
	public void setShowStatistics(boolean showStatistics) {
		this.showStatistics = showStatistics;
	}

}
