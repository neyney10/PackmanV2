package GUI;

import javax.swing.JComponent;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class JBackground extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3817775198749911544L;
	public Image img;
	
	public JBackground(Image img) {
		this.img = img;
		setLayout(null);
		
	
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(),this);

		//float scaleFactor = getWidth()/getHeight();
		
		
	}

}
