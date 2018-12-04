package GUI;

import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JBackground extends JPanel implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3817775198749911544L;
	public Image img;
	
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
		GameSpirit gs = new GameSpirit(e.getX(), e.getY(), 44, 44, MyFrame.loadImage("Ex3\\PACMAN.png"));
		add(gs);
		repaint();
		System.out.println("CLICKED "+e);
}


	@Override
	public void mouseReleased(MouseEvent e) {}

}
