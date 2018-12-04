package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Game.Game;
import GameObjects.GameObject;


public class MyFrame extends JFrame implements ComponentListener{

	// JBackground component is an object that contains all GameSpirits elements and represent the "Game" object in UI.
	private JBackground jb;
	
	// pnl_toolbar contains all the management buttons such load,save and run.
	private JPanel pnl_toolbar;
	
	// pnl_toolbar's Buttons
	private JButton btn_load; // load game from file.
	private JButton btn_save; // save game to file.
	private JButton btn_run; // run current loaded game.
	
	// starting size of MyFrame.
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
		int toolbarH = 40; // Height of the toolbar component
		Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

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
		pnl_toolbar.setBorder(BorderFactory.createEtchedBorder());
		
		/// create buttons for toolbar
		btn_load = new JButton();
		btn_load.setText("Load existing game");
		btn_load.setCursor(handCursor);
		
		btn_save = new JButton();
		btn_save.setText("Save current game");
		btn_save.setCursor(handCursor);
		
		btn_run = new JButton();
		btn_run.setText("<- [RUN] ->");
		btn_run.setForeground(Color.BLUE);
		btn_run.setCursor(handCursor);
		
		// add buttons to toolbar panel
		pnl_toolbar.add(btn_load);
		pnl_toolbar.add(btn_save);
		pnl_toolbar.add(btn_run);
		

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
	
	/**
	 * reloading all game components and objects.
	 * TODO: might (?) be integrated inside JBackground
	 */
	public void refreshGameUI() {
		if(game == null || jb == null) return;
		
		for(GameObject obj : game.getObjects()) {
			jb.add(new GameSpirit(obj));
		}
	}
	
	/**
	 * Saving the current game to a KML file
	 * TODO: implement
	 */
	public void saveGame() {
		
	}
	
	/**
	 * running the current game.
	 * TODO: implement
	 */
	public void runGame() {
		
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentShown(ComponentEvent arg0) {}







}
