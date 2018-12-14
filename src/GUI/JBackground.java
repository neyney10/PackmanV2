package GUI;

import javax.swing.JPanel;

import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Geom.Point3D;
import Maps.Map;
import Path.Path;
import sun.security.action.GetIntegerAction;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

public class JBackground extends JPanel implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3817775198749911544L;
	public boolean dropMode = false; //TODO: make getters and setters
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
		g.drawImage(game.getMap().getBackground(), 0, 0, getWidth(), getHeight(),this);
		
		// paint paths
		// temp
		if(MyFrame.DEBUG) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			
			g2.setColor(Color.blue);
			Map m = game.getMap();
			Iterator<GameObject> iter = game.typeIterator(new Packman());
			while(iter.hasNext()) {
				Packman p = (Packman) iter.next();
				Path path = p.getPath();
				
				if(path == null || path.getPointAmount() < 2)
					continue;
				
				Iterator<Point3D> iterPath = path.iterator();
				Point3D p1 = iterPath.next();
				Point3D p2;
				while(iterPath.hasNext()) {
					p2 = iterPath.next();
					Point px1 = m.getLocationOnScreen(p1);
					Point px2 = m.getLocationOnScreen(p2);
					g2.drawLine(px1.x, px1.y, px2.x, px2.y);
					p1 = p2;
				}
			}
		}
		
	}

	/**
	 * reloading all game components and objects.
	 */
	public void refreshGameUI() {
		if(game == null) return;

		removeAll();

		for(GameObject obj : game.getObjects())  {
			add(game.createGameSpirit(obj));
			System.out.println(obj.getPoint() + " | "+ game.createGameSpirit(obj).getLocation() + " | " + game.getMap().getLocationFromScreen(game.createGameSpirit(obj).getLocation()));
		}

		repaint();

	}

	@Override
	public void mouseClicked(MouseEvent e) {}


	@Override
	public void mouseEntered(MouseEvent e) {}


	@Override
	public void mouseExited(MouseEvent e) {}


	@Override
	public void mousePressed(MouseEvent e) {		
		// ONLY FOR DEBUG
		if(!dropMode) return;

		add(game.createGameSpiritXY(dropItem, e.getX(), e.getY()));
		repaint();
		if(MyFrame.DEBUG) {
			System.out.println("CLICKED "+e);
			Map m = game.getMap();
			GameSpirit gs = game.createGameSpiritXY(dropItem, e.getX(), e.getY());
			System.out.println(gs.getLocation()+ " | "+ m.getLocationFromScreen(gs.getLocation()));
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {}

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

		if(game == null) 
			return;

		refreshGameUI();
	}

}
