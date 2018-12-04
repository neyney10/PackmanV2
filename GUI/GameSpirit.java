package GUI;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public class GameSpirit extends JComponent {
	
	private static final long serialVersionUID = 3817775198749911544L;
	public Image img;
	public int startWidth, startHeight, startX, startY;
	
	public GameSpirit(int x,int y, int width, int height, Image img) {
		this.img = img;
		setBounds(x, y, width, height); 
		
		/////////////////////////////////////
		// Used for Re-scaling of MyFrame. //
		/////////////////////////////////////
		this.startWidth = width;
		this.startHeight = height; 
		this.startX = x;
		this.startY = y; 
	}
	
	public GameSpirit(GameObject obj) {
		
	}
	
	/*
	 * [Developer Note] Status: Complete.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/*
		 Drawing this Object's Image at Current X,Y position 
		 with the dimensions of (from (0,0) to (width,height)).
		 Note: if using MyFrame than MyFrame class is re-scaling all of it's components when resized.
		 */
		g.drawImage(img, 0,0,getWidth(),getHeight(),this);
	

	}
}
