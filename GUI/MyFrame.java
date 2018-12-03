package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyFrame extends JFrame implements ComponentListener{

	private JBackground jb;
	public int SIZEW = 1000;
	public int SIZEH = 600;
	
	
	/**
	 * Serialization version UIDl
	 */
	private static final long serialVersionUID = 121312L;

	/**
	 * Constructor of MyFrame, initialization and showing the frame.
	 */
	public MyFrame() {

		init();
		this.setTitle("Packman!!!");
		this.setVisible(true);
	}

	/**
	 * Initialize JComponents, toolbar, background image and such.
	 */
	private void init() {

		// Set global settings
		setSize(SIZEW,SIZEH);
		setMinimumSize(new Dimension(600,300));
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(this); // for resizing the component


		// Background
		Image img;
		img = loadImage("Ex3\\Ariel1.png");
		jb = new JBackground(img);
		jb.setBounds(0,0,SIZEW,SIZEH);
		
		// --testing -> gameSpirits
		img = loadImage("Ex3\\PACMAN.png");
		GameSpirit pacman = new GameSpirit(115,5,100,100,img);
		pacman.j = this; // TEMP
		
		img = loadImage("Ex3\\fruit.png");
		GameSpirit fruit = new GameSpirit(333,25,77,77,img);
		fruit.j = this; // TEMP
		
		jb.add(pacman);
		jb.add(fruit);
		
		
		
		
		// Toolbar
		
		// pack it up.
		add(jb);
	}
	
	/**
	 * Loading an Image from file, see: https://stackoverflow.com/questions/18777893/jframe-background-image
	 * @param path to the file, Ex: "Ex3\\Pacman.png"
	 * @return Image Object
	 */
	public Image loadImage(String path) {
		BufferedImage i = null;
		try {
			i =  ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return i.getScaledInstance(i.getWidth(), i.getWidth(), Image.SCALE_SMOOTH);
	}
	
	public void componentResized(ComponentEvent ce) {
		if(jb == null)
			return;
		
		jb.setSize(this.size());
	
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {

		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	};







}
