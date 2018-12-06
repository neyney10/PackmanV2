package GUI;

import javax.swing.JPanel;

import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Maps.Map;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JBackground extends JPanel implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3817775198749911544L;
	public Image img; //TODO: make getters and setters
	public boolean dropMode = false; //TODO: make getters and setters
	public Image dropItem; // temp, should be GameObject(?)
	private Game game;
	
	
	public JBackground(Image img) {
		this.img = img;
		setLayout(null);
		addMouseListener(this);
	
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(),this);
	}
	
	/**
	 * reloading all game components and objects.
	 */
	public void refreshGameUI() {
		if(game == null) return;
		
		removeAll();
		
		for(GameObject obj : game.getObjects()) {
			// TODO: set the width and height inside the
			if(obj instanceof Packman)
				add(new GameSpirit(obj, Packman.width, Packman.height, game.getMap())); 
			else if(obj instanceof Fruit)
				add(new GameSpirit(obj, Fruit.width, Fruit.height, game.getMap()));
		}
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
		
		Map m = game.getMap();
		
		GameSpirit gs = new GameSpirit(e.getX(), e.getY(), 44, 44, dropItem);
		add(gs);
		m.updateLocationOnScreen(gs);
		repaint();
		System.out.println("CLICKED "+e);
		
		System.out.println(m.getDistance(new Point(0,0), new Point(e.getX(), e.getY())));
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
