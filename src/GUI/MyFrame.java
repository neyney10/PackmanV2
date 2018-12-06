package GUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Maps.Map;


public class MyFrame extends JFrame implements ComponentListener{

	// JBackground component is an object that contains all GameSpirits elements and represent the "Game" object in UI.
	private JBackground jb;
	
	// images and media, see "init" function for initialization.
	ImageIcon saveIcon;
	ImageIcon loadIcon;
	ImageIcon runIcon;
	ImageIcon exitIcon;
	ImageIcon pacmanIcon;
	ImageIcon fruitIcon;
	Image packmanImage, fruitImage, mapImage;
	
	// menubar's Buttons
	private JButton btn_run; // run current loaded game.
	private JButton btn_exitDropMode;
	
	// starting size of MyFrame.
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
		super();
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


		//// Set Component's settings
		Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
		 // icons
		saveIcon = new ImageIcon(loadImage("GameData\\save_icon.png"));
		loadIcon = new ImageIcon(loadImage("GameData\\load_icon.png"));
		runIcon = new ImageIcon(loadImage("GameData\\run_icon.png"));
		exitIcon = new ImageIcon(loadImage("GameData\\exit_icon.png"));
		pacmanIcon = new ImageIcon(loadImage("GameData\\pacman_icon.png"));
		fruitIcon = new ImageIcon(loadImage("GameData\\fruit_icon.png"));
		packmanImage = loadImage("GameData\\PACMAN.png");
		fruitImage = loadImage("GameData\\fruit.png");
		mapImage = loadImage("GameData\\Ariel1.png"); // Should come from MAP

		////////////////
		// Background //
		////////////////
		jb = new JBackground(mapImage);
		jb.setBounds(0,0,SIZEW,SIZEH);



		/////////////
		// Toolbar //
		/////////////

		int menuFontSize, itemFontSize;
		menuFontSize = 18;
		itemFontSize = 22;
		
		Font menuFont = new Font("Arial", Font.PLAIN, menuFontSize);
		Font itemFont = new Font("Arial", Font.PLAIN, itemFontSize);
		
		JMenuBar menubar = new JMenuBar();
		
		// creating menus
		JMenu menu = new JMenu("File");
		menu.setFont(menuFont);
		menu.setCursor(handCursor);
		menu.setBorder(BorderFactory.createSoftBevelBorder(0));
		
		JMenu menuObjects = new JMenu("Game Objects");
		menuObjects.setFont(menuFont);
		menuObjects.setCursor(handCursor);
		menuObjects.setBorder(BorderFactory.createSoftBevelBorder(0));

		
		/// create buttons for menubar's menus
		JMenuItem i1 = new JMenuItem("Load game");
		i1.setFont(itemFont);
		i1.setCursor(handCursor);
		i1.setBackground(Color.cyan);
		i1.setIcon(loadIcon);
		i1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {loadGame();}});
		
		JMenuItem i2 = new JMenuItem("Save game");
		i2.setFont(itemFont);
		i2.setCursor(handCursor);
		i2.setBackground(Color.cyan);
		i2.setIcon(saveIcon);
		i2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {saveGame();}});
		
		JMenuItem gmobjAddPackman = new JMenuItem("Pokemon +");
		gmobjAddPackman.setFont(itemFont);
		gmobjAddPackman.setCursor(handCursor);
		gmobjAddPackman.setBackground(Color.ORANGE); 
		gmobjAddPackman.setIcon(pacmanIcon);
		gmobjAddPackman.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {addPackman();}});
		
		JMenuItem gmobjAddFruit = new JMenuItem("Yummy +");
		gmobjAddFruit.setFont(itemFont);
		gmobjAddFruit.setCursor(handCursor);
		gmobjAddFruit.setBackground(Color.ORANGE); 
		gmobjAddFruit.setIcon(fruitIcon);
		gmobjAddFruit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {addFruit(); }});
		
		btn_run = new JButton();
		btn_run.setText("RUN");
		btn_run.setForeground(Color.getHSBColor(0.35f, 1.0f, 0.5f));
		btn_run.setCursor(handCursor);
		btn_run.setIcon(runIcon);
		btn_run.setHorizontalTextPosition(SwingConstants.LEFT); // TO SLIDE ICON TO THE RIGHT
		btn_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {MyFrame.this.Test();}});
		
		btn_exitDropMode = new JButton();
		btn_exitDropMode.setText("stop dropMode");
		btn_exitDropMode.setVisible(false);
		btn_exitDropMode.setIcon(exitIcon);
		btn_exitDropMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {exitDropMode();}});
		
		// pack menus
		menu.add(i1);
		menu.add(i2);
		menuObjects.add(gmobjAddPackman);
		menuObjects.add(gmobjAddFruit);
		menubar.add(menu);
		menubar.add(menuObjects);
		menubar.add(btn_exitDropMode);
		menubar.add(btn_run);
		
		/* pack it up. from last to first generated components to create the 'Z' height layer property and 
		 stack components */
		setJMenuBar(menubar);
		add(jb);

	}

	/**
	 * Loading an Image from file.
	 * @see: https://stackoverflow.com/questions/18777893/jframe-background-image
	 * @param path to the file, Ex: "GameData\\Pacman.png" for Windows system.
	 * @return Image Object (can be casted into BufferedImage)
	 */
	public static Image loadImage(String path) {
		BufferedImage i = null;
		try {
			i =  ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return i;//i.getScaledInstance(i.getWidth(), i.getWidth(), Image.SCALE_SMOOTH);
	}
	
	/**
	 * @see "https://stackoverflow.com/questions/8639567/java-rotating-images"
	 * @param img
	 * @param angle
	 * @return
	 */
	public static Image rotateImage(Image img, float angle) {
		BufferedImage image = (BufferedImage) img;

		// Rotation information

		double rotationRequired = Math.toRadians (angle);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		return op.filter(image, null);
	}

	public void componentResized(ComponentEvent ce) {
		// update Background
		if(jb != null) {
			jb.setSize(this.size());
			
			if(jb.getGame() == null)
				return;
			
			Map map = jb.getGame().getMap();
			
			map.updateScreenRange(this.getWidth(), this.getHeight());

			for(Component c : jb.getComponents()) {
				if(c instanceof GameSpirit) {
					GameSpirit gameComponent = (GameSpirit) c; 
					map.updateLocationOnScreen(gameComponent);
				} // if component instanceof GameSpirit
			} // for over components
		} // if jb != null 

	}
	
	/**
	 * Set a new Game object associated with this MyFrame.
	 * @param game - the game to set.
	 */
	public void setGame(Game game) {
		jb.setGame(game);
	}
	
	/**
	 * Load game from CSV file.
	 * @param *path - to the file
	 */
	public void loadGame() {
		FileDialog fd = new FileDialog(new Frame(), "Load Game File (CSV)",FileDialog.LOAD);
		fd.setDirectory("/");
		fd.setFile("*.csv");
		fd.setVisible(true);

		if(fd.getFiles().length != 0)
			setGame(new Game(fd.getFiles()[0].getAbsolutePath()));
	}
	
	
	
	/**
	 * Saving the current game to a CSV file
	 * TODO: implement
	 */
	public void saveGame() {
		if(jb == null) return;
		
		FileDialog fd = new FileDialog(new Frame(), "Save Game File (CSV)",FileDialog.SAVE);
		fd.setDirectory("/");
		fd.setFile("*.csv");
		fd.setVisible(true);
		if (fd.getFiles().length != 0)
			jb.getGame().toCsv(fd.getDirectory()+fd.getFile());
	}
	
	/**
	 * running the current game.
	 * TODO: implement
	 */
	public void runGame() {
		
	}
	
	
	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on map from type Packman.
	 */
	public void addPackman() {
		if(jb == null) return;
		
		jb.dropItem = packmanImage;
		enterDropMode();
	}
	
	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on map from type Fruit.
	 */
	public void addFruit() {
		if(jb == null) return;
		
		jb.dropItem = fruitImage;
		enterDropMode();
	}
	
	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on map.
	 */
	public void enterDropMode() {
		if(jb == null) return;
		
		jb.dropMode = true;
		btn_exitDropMode.setVisible(true);
	}
	
	/**
	 * Handles the click of exiting drop Mode
	 */
	public void exitDropMode() {
		if(jb == null) return;
		
		jb.dropMode = false;
		btn_exitDropMode.setVisible(false);
	}
	
	/**
	 * [Developer Note] Only for debug and testing purposes
	 */
	public void Test() {
		if(jb == null) return;
		
		Map map = jb.getGame().getMap();
		
		for(Component c : jb.getComponents()) {
			if(c instanceof GameSpirit) {
				GameSpirit gameComponent = (GameSpirit) c;
				map.moveLocationByPixels(gameComponent, 10, 0);
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentShown(ComponentEvent arg0) {}


}
