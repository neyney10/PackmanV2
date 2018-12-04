package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Game.Game;
import GameObjects.GameObject;


public class MyFrame extends JFrame implements ComponentListener{

	private JBackground jb;
	private JPanel pnl_toolbar;
	public int SIZEW = 1000;
	public int SIZEH = 600;
	
	private Game game;


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
		System.out.println("Making MyFrame visible...");
	}

	/**
	 * Initialize JComponents, toolbar, background image and such.
	 */
	private void init() {
		/////////////////////////
		// Set global settings //
		/////////////////////////
		setSize(SIZEW,SIZEH);
		setMinimumSize(new Dimension(600,300));
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(this); // for resizing the component

		// Set Component's settings
		int toolbarH = 35; // Height of the toolbar component

		////////////////
		// Background //
		////////////////
		Image img;
		img = loadImage("Ex3\\Ariel1.png");
		jb = new JBackground(img);
		jb.setBounds(0,toolbarH,SIZEW,SIZEH);

		// --testing -> gameSpirits 
		img = loadImage("Ex3\\PACMAN.png");
		GameSpirit pacman = new GameSpirit(115,5,100,100,img);

		img = loadImage("Ex3\\fruit.png");
		GameSpirit fruit = new GameSpirit(333,25,77,77,img);

		// add gameSpirits to JBackground
		jb.add(pacman);
		jb.add(fruit);

		/////////////
		// Toolbar //
		/////////////
		pnl_toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnl_toolbar.setBackground(Color.lightGray);
		pnl_toolbar.setBounds(0, 0, SIZEW, toolbarH);
		
		/// create buttons for toolbar
		JButton btn_load = new JButton();
		btn_load.setText("Load existing game");
		
		JButton btn_save = new JButton();
		btn_save.setText("Save current game");
		
		// add buttons to toolbar panel
		pnl_toolbar.add(btn_load);
		pnl_toolbar.add(btn_save);
		

		/* pack it up. from last to first generated components to create the 'Z' height layer property and 
		 stack components */
		add(pnl_toolbar);
		add(jb);

	}

	/**
	 * Loading an Image from file, see: https://stackoverflow.com/questions/18777893/jframe-background-image
	 * @param path to the file, Ex: "Ex3\\Pacman.png" for Windows system.
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
		// update Background
		if(jb != null) {
			jb.setSize(this.size());

			double scaleFactorX, scaleFactorY;
			scaleFactorX = ((double)getWidth())/SIZEW;
			scaleFactorY = ((double)getHeight())/SIZEH;

			for(Component c : jb.getComponents()) {
				if(c instanceof GameSpirit) {
					GameSpirit gameComponent = (GameSpirit) c; 
					gameComponent.setBounds(
							(int)(gameComponent.startX*scaleFactorX),
							(int)(gameComponent.startY*scaleFactorY),
							(int)(gameComponent.startWidth*scaleFactorX),
							(int)(gameComponent.startHeight*scaleFactorY)
							);
				} // if component instanceof GameSpirit
			} // for over components
		} // if jb != null 

		// update Toolbar panel
		if(pnl_toolbar != null) {
			pnl_toolbar.setSize(this.getWidth(), pnl_toolbar.getHeight());
		}


	}
	
	/**
	 * Set a new Game object associated with this MyFrame.
	 * @param game - the game to set.
	 */
	public void setGame(Game game) {
		this.game = game;
	}
	
	/**
	 * Load game from CSV file.
	 * @param path - to the file
	 */
	public void loadGame(String path) {
		setGame(new Game(path));
	}
	
	public void refreshGameUI() {
		if(game == null || jb == null) return;
		
		for(GameObject obj : game.getObjects()) {
			jb.add(new GameSpirit(obj));
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentShown(ComponentEvent arg0) {}







}
