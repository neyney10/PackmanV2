package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameSpirit extends JComponent {
	
	private static final long serialVersionUID = 3817775198749911544L;
	public Image img;
	public MyFrame j;
	int startWidth, startHeight; // TEMP
	
	public GameSpirit(int x,int y, int width, int height, Image img) {
		this.img = img;
		setBounds(x, y, width, height); 
		this.startWidth = width; // TEMP
		this.startHeight = height; // TEMP
	}
	
	/**
	 * INCOMPLETE!
	 * TODO: RESCALE WIDTH AND HEIGHT, RESCALE X AND Y POSITIONS.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		double scaleFactorX, scaleFactorY;
		scaleFactorX = ((double)j.getWidth())/j.SIZEW;
		scaleFactorY = ((double)j.getHeight())/j.SIZEH;
		//setBounds(getX(),getY(),(int)(getWidth()*scaleFactorX),(int)(getHeight()*scaleFactorY));
		System.out.println(scaleFactorX);
		/*
		 * Drawing this Object's Image at Current X,Y position with the dimensions of (from (0,0) to (width,height)).
		 */
		g.drawImage(img, 0,0,getWidth(),getHeight(),this);
	

	}
}
