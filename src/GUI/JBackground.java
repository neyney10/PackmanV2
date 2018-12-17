package GUI;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.JPanel;

import Game.Game;
import GameObjects.GameObject;
import GameObjects.Packman;
import Geom.Point3D;
import Maps.Map;
import Path.Path;

public class JBackground extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3817775198749911544L;
	public boolean dropMode = false; // TODO: make getters and setters
	public GameObject dropItem;
	private Game game;

	public JBackground() {
		super();
		setLayout(null);
		addMouseListener(this);
	}

	public JBackground(Game game) {
		this();
		setGame(game);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(game.getMap().getBackground(), 0, 0, getWidth(), getHeight(), this);

		// PAINT PATHS

		if(game == null || game.isEmpty())
			return;

		Map m = game.getMap();
		Iterator<GameObject> iter = game.typeIterator(new Packman());

		Packman p;
		Path path;

		while (iter.hasNext()) {
			p = (Packman) iter.next();
			path = p.getPath();

			if (path == null || path.getPointAmount() < 2)
				continue;

			path.paint(g, m);
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

		// get a new Clone of the item.
		dropItem = dropItem.clone();
		
		// get the game's map for calculating coordinates.
		Map m = game.getMap();

		// get the point from click and calculate it's position and coordinates with the Map object.
		Point3D p3d = m.getLocationFromScreen(e.getPoint()); //m.transformByScale(e.getX(), e.getY())

		// set the new computed point.
		dropItem.setPoint(p3d);
		dropItem.setId((int) (Math.random() * 42543)); //TODO: change. not to do random.

		// genereate a Graphic element from this object.
		GameSpirit gs = game.createGameSpiritXY(dropItem,e.getX(),e.getY());

		// add this game spirit and game object into game and to this graphic component.
		getGame().addGameObject(dropItem);
		add(gs);

		repaint();

		if (MyFrame.DEBUG) {
			System.out.println("CLICKED " + e.getPoint());
			System.out.println(p3d);
			System.out.println(gs.getLocation());
			System.out.println(gs.getStartLocation());
			System.out.println(m.getLocationOnScreen(p3d));
			// GameSpirit gs = game.createGameSpiritXY(dropItem, e.getX(), e.getY());
			// System.out.println(gs.getLocation()+ " | "+
			// m.getLocationFromScreen(gs.getLocation()));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
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

		refreshGameUI();
	}

}
