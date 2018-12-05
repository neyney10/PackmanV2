package GUI;

import javax.swing.JComponent;
import javax.swing.JPanel;

import GameObjects.GameObject;
import Maps.Map;

import java.awt.Component;
import java.awt.Dimension;
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
		
		GameSpirit gs = new GameSpirit(e.getX(), e.getY(), 44, 44, dropItem);
		add(gs);
		repaint();
		System.out.println("CLICKED "+e);
		Map m = new Map();
		System.out.println(m.getDistance(new Point(0,0), new Point(e.getX(), e.getY())));
}


	@Override
	public void mouseReleased(MouseEvent e) {}

}
