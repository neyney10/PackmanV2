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
		System.out.println("CLICKED "+e);
		System.out.println(e.getLocationOnScreen()+ " | "+game.getMap().getLocationFromScreen(e.getLocationOnScreen()));
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
