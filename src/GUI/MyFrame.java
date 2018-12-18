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
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import Algorithms.ShortestPathAlgo;
import Files_format.Path2Kml;
import GUI.Animation.SimulatePath;
import Game.Game;
import GameObjects.Fruit;
import GameObjects.GameObject;
import GameObjects.Packman;
import Maps.Map;
import Maps.MapFactory;
import Maps.MapFactory.MapType;
import Path.Solutions;

/**
 * Singleton
 * the main Window of the game.
 * have default window size of 1433x642
 * @author neyne
 */
public final class MyFrame extends JFrame implements ComponentListener {

	private static MyFrame instance;

	final public static boolean DEBUG = true;

	// JBackground component is an object that contains all GameSpirits elements and
	// represent the "Game" object in UI.
	private JBackground jb;

	// images and media, see "init" function for initialization.
	ImageIcon saveIcon;
	ImageIcon loadIcon;
	ImageIcon runIcon;
	ImageIcon exitIcon;
	ImageIcon pacmanIcon;
	ImageIcon fruitIcon;
	ImageIcon clearIcon;
	ImageIcon computeIcon;

	// menubar's Buttons
	private JButton btn_run; // run current loaded game.
	private JButton btn_exitDropMode; // stop the abillity to drop game objects
	private JButton btn_stopSimulation;
	private JButton btn_compute; // compute shortest path algo

	// path algorithm
	private ShortestPathAlgo pathFindingAlgorithm;
	// Simulation
	private SimulatePath simulation;
	private boolean simulating = false;

	// starting size of MyFrame.
	public final int SIZEW = 1433;
	public final int SIZEH = 642;

	/**
	 * Serialization version UIDl
	 */
	private static final long serialVersionUID = 121312L;

	/**
	 * Get this Singleton's instance.
	 * @return instance of MyFrame object.
	 */
	public static MyFrame getInstance() {
		if (instance == null)
			instance = new MyFrame();

		return instance;
	}

	/**
	 * Constructor of MyFrame, initialization and showing the frame.
	 */
	private MyFrame() {
		super();
		init();
		this.setTitle("Packman!!!");
	}

	/**
	 * Initialize JComponents, toolbar, background image and such.
	 */
	private void init() {
		/////////////////////////
		// Set global settings //
		/////////////////////////
		setSize(SIZEW, SIZEH);
		setMinimumSize(new Dimension(600, 300));
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(this); // for resizing the component

		//// Set Component's settings
		Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
		// icons
		saveIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\save_icon.png"));
		loadIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\load_icon.png"));
		runIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\run_icon.png"));
		exitIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\exit_icon.png"));
		pacmanIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\pacman_icon.png"));
		fruitIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\fruit_icon.png"));
		clearIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\del_icon.png"));
		computeIcon = new ImageIcon(ImageFactory.getImageFromDisk("GameData\\compute_icon.png"));

		////////////////
		// Background //
		////////////////
		jb = new JBackground();
		jb.setBounds(0, 0, SIZEW, SIZEH);

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
		
		// would be inside gameobjects menu
		JMenu menuMap = new JMenu("Maps");
		menuMap.setFont(menuFont);
		menuMap.setCursor(handCursor);
		menuMap.setBackground(Color.WHITE);
		menuMap.setBorder(BorderFactory.createSoftBevelBorder(0));

		/// create buttons for menubar's menus
		JMenuItem i1 = new JMenuItem("Load game");
		i1.setFont(itemFont);
		i1.setCursor(handCursor);
		i1.setBackground(Color.cyan);
		i1.setIcon(loadIcon);
		i1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadGame();
			}
		});

		JMenuItem i2 = new JMenuItem("Save game -> CSV");
		i2.setFont(itemFont);
		i2.setCursor(handCursor);
		i2.setBackground(Color.cyan);
		i2.setIcon(saveIcon);
		i2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveGame();
			}
		});
		
		JMenuItem export = new JMenuItem("Export game -> KML");
		export.setFont(itemFont);
		export.setCursor(handCursor);
		export.setBackground(Color.cyan);
		export.setIcon(saveIcon);
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportGame();
			}
		});

		JMenuItem clear = new JMenuItem("Clear current game");
		clear.setFont(itemFont);
		clear.setCursor(handCursor);
		clear.setBackground(Color.cyan);
		clear.setIcon(clearIcon);
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});

		JMenuItem gmobjAddPackman = new JMenuItem("Pokemon +");
		gmobjAddPackman.setFont(itemFont);
		gmobjAddPackman.setCursor(handCursor);
		gmobjAddPackman.setBackground(Color.ORANGE);
		gmobjAddPackman.setIcon(pacmanIcon);
		gmobjAddPackman.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPackman();
			}
		});

		JMenuItem gmobjAddFruit = new JMenuItem("Yummy +");
		gmobjAddFruit.setFont(itemFont);
		gmobjAddFruit.setCursor(handCursor);
		gmobjAddFruit.setBackground(Color.ORANGE);
		gmobjAddFruit.setIcon(fruitIcon);
		gmobjAddFruit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFruit();
			}
		});
		
		JMenuItem mapAriel = new JMenuItem("Ariel University");
		mapAriel.setFont(itemFont);
		mapAriel.setCursor(handCursor);
		mapAriel.setBackground(Color.GRAY);
		mapAriel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jb == null) return;
				jb.setMap(MapFactory.getMap(MapType.ArielUniversity));
				jb.repaint();
			}
		});
		
		JMenuItem mapTelAviv = new JMenuItem("Tel-Aviv");
		mapTelAviv.setFont(itemFont);
		mapTelAviv.setCursor(handCursor);
		mapTelAviv.setBackground(Color.GRAY);
		mapTelAviv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					if(jb == null) return;
					jb.setMap(MapFactory.getMap(MapType.TelAviv));
					jb.repaint();
			}
		});

		btn_run = new JButton();
		btn_run.setText("RUN");
		btn_run.setForeground(Color.getHSBColor(0.35f, 1.0f, 0.5f));
		btn_run.setCursor(handCursor);
		btn_run.setIcon(runIcon);
		btn_run.setHorizontalTextPosition(SwingConstants.LEFT); // TO SLIDE ICON TO THE RIGHT
		btn_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyFrame.this.runGame();
			}
		});

		btn_compute = new JButton();
		btn_compute.setText("Compute");
		btn_compute.setForeground(Color.getHSBColor(0.35f, 1.0f, 0.5f));
		btn_compute.setCursor(handCursor);
		btn_compute.setIcon(computeIcon);
		btn_compute.setHorizontalTextPosition(SwingConstants.LEFT); // TO SLIDE ICON TO THE RIGHT
		btn_compute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyFrame.this.computePath();
			}
		});

		btn_exitDropMode = new JButton();
		btn_exitDropMode.setText("stop dropMode");
		btn_exitDropMode.setVisible(false);
		btn_exitDropMode.setIcon(exitIcon);
		btn_exitDropMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitDropMode();
			}
		});

		btn_stopSimulation = new JButton();
		btn_stopSimulation.setText("Stop simulation");
		btn_stopSimulation.setVisible(false);
		btn_stopSimulation.setIcon(exitIcon);
		btn_stopSimulation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});

		// pack menus
		menuMap.add(mapAriel);
		menuMap.add(mapTelAviv);
		menu.add(i1);
		menu.add(i2);
		menu.add(export);
		menu.add(clear);
		menuObjects.add(gmobjAddPackman);
		menuObjects.add(gmobjAddFruit);
		menuObjects.add(menuMap); // adding a submenu
		menubar.add(menu);
		menubar.add(menuObjects);
		menubar.add(btn_exitDropMode);
		menubar.add(btn_run);
		menubar.add(btn_stopSimulation);
		menubar.add(btn_compute);

		/*
		 * pack it up. from last to first generated components to create the 'Z' height
		 * layer property and stack components
		 */
		setJMenuBar(menubar);
		add(jb);

	}

	/**
	 * @see "https://stackoverflow.com/questions/8639567/java-rotating-images"
	 * @param img
	 * @param angle
	 * @return Image - rotated Image
	 */
	public static Image rotateImage(Image img, float angle) {
		BufferedImage image = (BufferedImage) img;

		// Rotation information

		double rotationRequired = Math.toRadians(angle);
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		return op.filter(image, null);
	}

	/**
	 * called when the user resizing the Window. updating the images and game objects position 
	 * scaling all width , heights, and x y positions.
	 */
	public void componentResized(ComponentEvent ce) {
		rescaleGameUI();
	}

	/**
	 * Rescaling the GameUI components to match screen size, width and height <br>
	 * adjusting position and image size.
	 */
	public void rescaleGameUI() {
		// update Background
		if (jb != null) {
			jb.setSize(this.size());

			if (jb.getMap() == null)
				return;
			
			jb.updateMapWithNewScreenSize(this.getWidth(), this.getHeight());
			
			if (jb.getGame() == null)
				return;

			Map map = jb.getMap();

			//map.updateScreenRange(this.getWidth(), this.getHeight());

			for (Component c : jb.getComponents()) {
				if (c instanceof GameSpirit) {
					GameSpirit gameComponent = (GameSpirit) c;
					map.updateLocationOnScreen(gameComponent);
				} // if component instanceof GameSpirit
			} // for over components
		} // if jb != null
	}

	/**
	 * Clearing all game objects from the game.
	 */
	public void clear() {
		setGame(new Game());
	}

	/**
	 * Set a new Game object associated with this MyFrame.
	 * 
	 * @param game - the game to set.
	 */
	public void setGame(Game game) {
		jb.setGame(game);
		rescaleGameUI();
	}

	/**
	 * Load game from CSV file.
	 */
	public void loadGame() {
		FileDialog fd = new FileDialog(new Frame(), "Load Game File (CSV)", FileDialog.LOAD);
		fd.setDirectory("/");
		fd.setFile("*.csv");
		fd.setVisible(true);

		if (fd.getFiles().length == 0)
			return;

		stop(); // stopping simulation if any.
		setGame(new Game(fd.getFiles()[0].getAbsolutePath()));
		

		// TEMP
		// TODO: remove.
		pathFindingAlgorithm = new ShortestPathAlgo(jb.getGame());
		pathFindingAlgorithm.calcPath();

	}

	/**
	 * Saving the current game to a CSV file
	 */
	public void saveGame() {
		if (jb == null)
			return;

		FileDialog fd = new FileDialog(new Frame(), "Save Game File (CSV)", FileDialog.SAVE);
		fd.setDirectory("/");
		fd.setFile("*.csv");
		fd.setVisible(true);
		if (fd.getFiles().length != 0)
			jb.getGame().toCsv(fd.getDirectory() + fd.getFile());
	}
	
	/**
	 * Exporting the  current game into a KML file.
	 */
	public void exportGame() {
		if(jb == null || jb.getGame() == null || jb.getGame().isEmpty())
			return;
		
			if (jb == null)
			return;

		FileDialog fd = new FileDialog(new Frame(), "Export Game File (KML)", FileDialog.SAVE);
		fd.setDirectory("/");
		fd.setFile("*.kml");
		fd.setVisible(true);
		
		// if didnt choose a name
		if (fd.getFiles().length == 0)
			return;
		
		Solutions solution = ShortestPathAlgo.convertIntoPathSolutions(jb.getGame());
		Path2Kml.create(solution, (fd.getDirectory() + fd.getFile()));
	}

	/**
	 * running the current game.
	 */
	public void runGame() {
		if (jb == null || jb.getGame() == null || jb.getGame().isEmpty() || simulating)
			return;

		btn_stopSimulation.setVisible(true);
		btn_run.setVisible(false);

		simulation = new SimulatePath(jb);
		simulation.start();

		simulating = true;
	}

	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on
	 * map from type Packman.
	 */
	public void addPackman() {
		if (jb == null)
			return;

		jb.dropItem = new Packman(0);
		enterDropMode();
	}

	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on
	 * map from type Fruit.
	 */
	public void addFruit() {
		if (jb == null)
			return;

		jb.dropItem = new Fruit(0);
		enterDropMode();
	}

	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on
	 * map.
	 */
	public void enterDropMode() {
		if (jb == null)
			return;

		jb.dropMode = true;
		btn_exitDropMode.setVisible(true);
	}

	/**
	 * Handles the click of exiting drop Mode
	 */
	public void exitDropMode() {
		if (jb == null)
			return;

		jb.dropMode = false;
		btn_exitDropMode.setVisible(false);
	}

	/**
	 * Stop running, stopping simulation
	 */
	public void stop() {
		if(!simulating)
			return;

		simulation.interrupt();

		try {
			simulation.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		if (jb == null || jb.getGame() == null)
			return;

		Map map = jb.getGame().getMap();

		for (Component co : jb.getComponents()) {
			if (co instanceof GameSpirit) {
				GameSpirit gs = (GameSpirit) co;
				if (gs.getGameObj() instanceof Fruit)
					co.setVisible(true);
				else if(gs.getGameObj() instanceof Packman)  {
					map.updateLocationOnScreen(gs,map.transformByScale(map.getLocationOnScreen(gs.getGameObj())));
				}
					
			}
		}

		btn_run.setVisible(true);
		btn_stopSimulation.setVisible(false);
		simulating = false;
	}

	/**
	 * Calculate path's of all pacmans in game by using the ShortestPathAlgo class to compute.
	 * the class set the path on each pacman after calculation.
	 */
	public void computePath() {
		if(jb == null || jb.getGame() == null)
			return;

		// compute paths
		pathFindingAlgorithm = new ShortestPathAlgo(jb.getGame());
		pathFindingAlgorithm.calcPath();

		// repaint
		repaint();
	}


	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

}
