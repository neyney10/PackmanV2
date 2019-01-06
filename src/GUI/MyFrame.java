package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import AI.AutomaticRobot;
import AI.RobotSimulator;
import Algorithms.*;
import Files_format.Path2Kml;
import GUI.Animation.AutoRun;
import GUI.Animation.SimulatePath;
import Game.Game;
import GameObjects.Box;
import GameObjects.Fruit;
import GameObjects.Ghost;
import GameObjects.Packman;
import GameObjects.Player;
import Geom.Point3D;
import Maps.Map;
import Maps.MapFactory;
import Maps.MapFactory.MapType;
import Path.Path;
import Path.Solution;
import Robot.Play;

/**
 * Singleton the main Window of the game. have default window size of 1433x642
 * 
 * @author neyne
 */
public final class MyFrame extends JFrame implements ComponentListener {

	private static MyFrame instance;

	final public static boolean DEBUG = true;

	// JBackground component is an object that contains all GameSpirits elements and
	// represent the "Game" object in UI.
	private JBackground jb;
	// TEMP:
	AutomaticRobot robot;

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
	private JButton btn_run; // run current loaded game with a robot.
	private JButton btn_run_manual; // run current loaded game manually.
	private JButton btn_exitDropMode; // stop the abillity to drop game objects
	private JButton btn_stopSimulation;
	private JButton btn_compute; // compute shortest path algo

	// fonts
	Font menuFont, itemFont;
	int menuFontSize, itemFontSize;

	// cursors
	Cursor handCursor;

	// path algorithm
	private ShortestPathAlgo pathFindingAlgorithm;
	// Simulation
	private SimulatePath simulation;
	private AutoRun autoRun;
	private boolean simulating = false;

	// starting size of MyFrame.
	public final int SIZEW = 1433;
	public final int SIZEH = 642;

	/**
	 * Serialization version UIDl
	 */
	private static final long serialVersionUID = 121312L;

	// temp
	public Path path;
	private String scenario;

	/**
	 * Get this Singleton's instance.
	 * 
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
		handCursor = new Cursor(Cursor.HAND_CURSOR);
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

		menuFontSize = 18;
		itemFontSize = 22;

		menuFont = new Font("Arial", Font.PLAIN, menuFontSize);
		itemFont = new Font("Arial", Font.PLAIN, itemFontSize);


		/*
		 * pack it up. from last to first generated components to create the 'Z' height
		 * layer property and stack components
		 */
		setJMenuBar(initMenuBar());
		add(jb);

	}

	/**
	 * Initialize menu bar for this JFrame.
	 * @return JMenuBar the created menu bar
	 */
	private JMenuBar initMenuBar() {
		JMenuBar menubar = new JMenuBar();

		menubar.add(initFileMenu());
		menubar.add(initGameObjectMenu());
		//menubar.add(initRobotMenu());

		btn_run = new JButton();
		btn_run.setText("RUN [A]");
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

		btn_run_manual = new JButton();
		btn_run_manual.setText("RUN [M]");
		btn_run_manual.setForeground(Color.getHSBColor(0.35f, 1.0f, 0.5f));
		btn_run_manual.setCursor(handCursor);
		btn_run_manual.setIcon(runIcon);
		btn_run_manual.setHorizontalTextPosition(SwingConstants.LEFT); // TO SLIDE ICON TO THE RIGHT
		btn_run_manual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyFrame.this.runGameManual();
			}
		});

		btn_compute = new JButton();
		btn_compute.setText("Create Robot");
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

		menubar.add(btn_exitDropMode);
		menubar.add(btn_run);
		menubar.add(btn_run_manual);
		menubar.add(btn_stopSimulation);
		menubar.add(btn_compute);

		return menubar;
	}

	/**
	 * Initialize menu for files
	 * @return JMenu file menu
	 */
	private JMenu initFileMenu() {
		// creating menus
		JMenu menu = new JMenu("File");
		menu.setFont(menuFont);
		menu.setCursor(handCursor);
		menu.setBorder(BorderFactory.createSoftBevelBorder(0));

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

		menu.add(i1);
		menu.add(i2);
		menu.add(export);
		menu.add(clear);

		return menu;
	}

	/**
	 * Initialize menu for game objects
	 * @return JMenu game objects menu
	 */
	private JMenu initGameObjectMenu() {
		JMenu menuObjects = new JMenu("Game Objects");
		menuObjects.setFont(menuFont);
		menuObjects.setCursor(handCursor);
		menuObjects.setBorder(BorderFactory.createSoftBevelBorder(0));

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

		JMenuItem gmobjAddGhost = new JMenuItem("Boo +");
		gmobjAddGhost.setFont(itemFont);
		gmobjAddGhost.setCursor(handCursor);
		gmobjAddGhost.setBackground(Color.ORANGE);
		gmobjAddGhost.setIcon(fruitIcon);
		gmobjAddGhost.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addGhost();
			}
		});

		JMenuItem gmobjAddBox = new JMenuItem("Blacky +");
		gmobjAddBox.setFont(itemFont);
		gmobjAddBox.setCursor(handCursor);
		gmobjAddBox.setBackground(Color.ORANGE);
		gmobjAddBox.setIcon(fruitIcon);
		gmobjAddBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addBox();
			}
		});

		JMenuItem gmobjAddPlayer = new JMenuItem("The Chosen one +");
		gmobjAddPlayer.setFont(itemFont);
		gmobjAddPlayer.setCursor(handCursor);
		gmobjAddPlayer.setBackground(Color.ORANGE);
		gmobjAddPlayer.setIcon(fruitIcon);
		gmobjAddPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPlayer();
			}
		});

		//TEMP
		JMenuItem gmobjShowStatistics = new JMenuItem("Show statistics");
		gmobjShowStatistics.setFont(itemFont);
		gmobjShowStatistics.setCursor(handCursor);
		gmobjShowStatistics.setBackground(Color.ORANGE);
		gmobjShowStatistics.setIcon(fruitIcon);
		gmobjShowStatistics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jb == null)
					return;

				jb.setShowStatistics(!jb.isShowStatistics());
				jb.repaint();
			}
		});

		menuObjects.add(gmobjAddPackman);
		menuObjects.add(gmobjAddFruit);
		menuObjects.add(gmobjAddGhost);
		menuObjects.add(gmobjAddBox);
		menuObjects.add(gmobjAddPlayer);
		menuObjects.add(gmobjShowStatistics);
		menuObjects.add(initMapMenu()); // adding a submenu

		return menuObjects;
	}

	/**
	 * Initialize menu for robot
	 * @return JMenu robot's menu
	 */
	private JMenu initRobotMenu() {
		JMenu menuRobot = new JMenu("Robot");
		menuRobot.setFont(menuFont);
		menuRobot.setCursor(handCursor);
		menuRobot.setBorder(BorderFactory.createSoftBevelBorder(0));

		JMenuItem strategyAlgorithm1 = new JMenuItem("Strategy: Sonic-Ver-1.0");
		strategyAlgorithm1.setFont(itemFont);
		strategyAlgorithm1.setCursor(handCursor);
		strategyAlgorithm1.setBackground(Color.ORANGE);
		strategyAlgorithm1.setIcon(pacmanIcon);
		strategyAlgorithm1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { robot = new AutomaticRobot(new SonicAlgorithmV1()); } });

		JMenuItem strategyAlgorithm2 = new JMenuItem("Strategy: Sonic-Ver-2.0");
		strategyAlgorithm2.setFont(itemFont);
		strategyAlgorithm2.setCursor(handCursor);
		strategyAlgorithm2.setBackground(Color.ORANGE);
		strategyAlgorithm2.setIcon(pacmanIcon);
		strategyAlgorithm2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { robot = new AutomaticRobot(new SonicAlgorithmV2(new DijkstraAlgorithm(jb.getGame()))); } });

		JMenuItem strategyAlgorithm3 = new JMenuItem("Strategy: Sonic-Ver-3.0");
		strategyAlgorithm3.setFont(itemFont);
		strategyAlgorithm3.setCursor(handCursor);
		strategyAlgorithm3.setBackground(Color.ORANGE);
		strategyAlgorithm3.setIcon(pacmanIcon);
		strategyAlgorithm3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { robot = new AutomaticRobot(new SonicAlgorithmV3(new DijkstraAlgorithm(jb.getGame()))); } });


		menuRobot.add(strategyAlgorithm1);
		menuRobot.add(strategyAlgorithm2);
		menuRobot.add(strategyAlgorithm3);

		return menuRobot;
	}

	private JMenu initMapMenu() {
		// would be inside gameobjects menu
		JMenu menuMap = new JMenu("Maps");
		menuMap.setFont(menuFont);
		menuMap.setCursor(handCursor);
		menuMap.setBackground(Color.WHITE);
		menuMap.setBorder(BorderFactory.createSoftBevelBorder(0));


		JMenuItem mapAriel = new JMenuItem("Ariel University");
		mapAriel.setFont(itemFont);
		mapAriel.setCursor(handCursor);
		mapAriel.setBackground(Color.GRAY);
		mapAriel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jb == null)
					return;
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
				if (jb == null)
					return;
				jb.setMap(MapFactory.getMap(MapType.TelAviv));
				jb.repaint();
			}
		});
		// pack menus
		menuMap.add(mapAriel);
		menuMap.add(mapTelAviv);

		return menuMap;
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
	 * called when the user resizing the Window. updating the images and game
	 * objects position scaling all width , heights, and x y positions.
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

			// TEMP -40
			jb.updateMapWithNewScreenSize(this.getWidth()-40, this.getHeight()-40);

			if (jb.getGame() == null)
				return;

			Map map = jb.getMap();

			// map.updateScreenRange(this.getWidth(), this.getHeight());

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
		if(jb == null)
			return;

		FileDialog fd = new FileDialog(new Frame(), "Load Game File (CSV)", FileDialog.LOAD);
		fd.setDirectory("/");
		fd.setFile("*.csv");
		fd.setVisible(true);

		if(fd.getFile() == null)
			return;

		scenario = fd.getFiles()[0].getAbsolutePath();
		Play play = new Play(fd.getFiles()[0].getAbsolutePath());
		play.setIDs(999999888);

		jb.setPlay(play);
		jb.setGame(new Game(play.getBoard()));

		stop(); // stopping simulation if any.

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
	 * Exporting the current game into a KML file.
	 */
	public void exportGame() {
		if (jb == null || jb.getGame() == null || jb.getGame().isEmpty())
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

		Solution solution = ShortestPathAlgo.convertIntoPathSolutions(jb.getGame());
		Path2Kml.create(jb.getGame(), solution, (fd.getDirectory() + fd.getFile()));
	}

	/**
	 * running the current game.
	 */
	public void runGame() {
		if (jb == null || jb.getGame() == null || jb.getGame().isEmpty() || jb.getMap() == null) // || simulating
			return;

		if(robot == null) {
			// Configure PathFinding algorithm for the robot
			RobotPathFindingAlgorithm pathFinding = new DijkstraAlgorithm(jb.getGame());
			// Configure Robot tactic algorithm, load it with the path algorithm.
			RobotAlgorithm algorithm = new SonicAlgorithmV3(pathFinding);
			// Configure the robot itself with the tactic algorithm and update it status.
			AutomaticRobot robot = new AutomaticRobot(algorithm);
		}
		robot.setNewGameStatus(jb.getGame());



		//get Robot's chosen starting position.
		Point3D playerPos3D = robot.getStartingPlayerPosition();

		Play play = jb.getPlay();
		play.setInitLocation(playerPos3D.y(), playerPos3D.x());

		// start game.
		play.start();
		jb.repaint();

		/* Create a thread handler for handling the game's frame-rate/refresh mechanism.
		 * give it the robot to handle at each frame.
		 */
		autoRun = new AutoRun(jb, 22);
		autoRun.addAutomaticRobot(robot);
		autoRun.start();

		if(jb.getPlay().isRuning()) {
			btn_stopSimulation.setVisible(true);
			btn_run.setVisible(false);
			btn_run_manual.setVisible(false);
		}

	}

	public void runGameManual() {
		if (jb == null || jb.getGame() == null || jb.getGame().isEmpty() || jb.getMap() == null) // || simulating
			return;

		Point3D playerPos3D = jb.getGame().getPlayer().getPoint();
		Play play = jb.getPlay();
		//play.setIDs(999999888);

		play.setInitLocation(playerPos3D.y(), playerPos3D.x());

		// start game.
		play.start();
		jb.repaint();

		autoRun = new AutoRun(jb, 33);
		autoRun.start();

		if(jb.getPlay().isRuning()) {
			btn_stopSimulation.setVisible(true);
			btn_run.setVisible(false);
			btn_run_manual.setVisible(false);
		}
	}

	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on
	 * map from type Packman.
	 */
	public void addPackman() {
		if (jb == null)
			return;

		jb.setDropItem(new Packman(0));
		enterDropMode();
	}

	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on
	 * map from type Fruit.
	 */
	public void addFruit() {
		if (jb == null)
			return;

		jb.setDropItem(new Fruit(0));
		enterDropMode();
	}

	private void addBox() {
		if (jb == null)
			return;

		jb.setDropItem(new Box(0));
		enterDropMode();
	}

	private void addPlayer() {
		if (jb == null)
			return;

		jb.setDropItem(new Player(0));
		enterDropMode();
	}

	private void addGhost() {
		if (jb == null)
			return;

		jb.setDropItem(new Ghost(0));
		enterDropMode();
	}

	/**
	 * setting the dropMode of this.JButton to true for allowing dropping items on
	 * map.
	 */
	public void enterDropMode() {
		if (jb == null)
			return;

		jb.setDropMode(true);
		btn_exitDropMode.setVisible(true);
	}

	/**
	 * Handles the click of exiting drop Mode
	 */
	public void exitDropMode() {
		if (jb == null)

			return;

		jb.setDropMode(false);
		btn_exitDropMode.setVisible(false);
	}

	/**
	 * Stop running, stopping simulation
	 */
	public void stop() {
		//if (!simulating)
		if(jb == null || autoRun == null)
			return;

		//simulation.interrupt();
		jb.getPlay().stop();
		autoRun.interrupt();

		try {
			//simulation.join();
			autoRun.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		if (jb == null || jb.getGame() == null)
			return;

		//		Map map = jb.getGame().getMap();
		//
		//		for (Component co : jb.getComponents()) {
		//			if (co instanceof GameSpirit) {
		//				GameSpirit gs = (GameSpirit) co;
		//				if (gs.getGameObj() instanceof Fruit)
		//					co.setVisible(true);
		//				else if (gs.getGameObj() instanceof Packman) {
		//					map.updateLocationOnScreen(gs, map.transformByScale(map.getLocationOnScreen(gs.getGameObj())));
		//				}
		//
		//			}
		//		}

		btn_run.setVisible(true);
		btn_run_manual.setVisible(true);
		btn_stopSimulation.setVisible(false);

		simulating = false;
	}

	/**
	 * Calculate path's of all pacmans in game by using the ShortestPathAlgo class
	 * to compute. the class set the path on each pacman after calculation.
	 */
	public void computePath() {
		JDialog dialog = createRobotDialog();
		dialog.setModal(true);
		dialog.setVisible(true);
		//		if (jb == null || jb.getGame() == null)
		//			return;
		//
		//		// compute paths
		////		pathFindingAlgorithm = new ShortestPathAlgo(jb.getGame());
		////		pathFindingAlgorithm.calcPath();

		// repaint
		repaint();
	}

	public JDialog createRobotDialog() {
		JDialog dialog = new JDialog(this, "Robot factory! Create your own robot!");
		dialog.setSize(755, 133);
		dialog.setLayout(new FlowLayout(1,25,15));

		// Path's label
		JLabel lbl_paths = new JLabel("Path Finding Algorithm: ");

		// Path's cmb row descriptions
		String[] paths = { "Dijkstra" }; 

		// combo box of path finding algo
		JComboBox<String> cmb_Path = new JComboBox<>(paths);
		cmb_Path.addActionListener((e)-> {

		});

		// Strategy algo label
		JLabel lbl_algos = new JLabel("Strategy Algorithm: ");

		// Combo box description of algorithm.
		RobotAlgorithm[] algos = {
				new SonicAlgorithmV1(),
				new SonicAlgorithmV2(null),
				new SonicAlgorithmV3(null)
		};

		// combo box of algorithms.
		JComboBox<RobotAlgorithm> cmb_Algo = new JComboBox<>(algos);
		cmb_Algo.addActionListener((e)-> {

		});

		JButton btn_choose = new JButton("Choose Best");
		btn_choose.addActionListener((e) -> {
			if(MyFrame.this.scenario == null)
				return;
			
			RobotAlgorithm algorithm;
			
			JDialog notification = new JDialog();
			notification.setTitle("This task might take some time... Please wait.");
			notification.add(new JLabel("Please wait for the robot's simulation to end.."));
			notification.setSize(300, 100);
			notification.setVisible(true);
			
			RobotSimulator simulator = new RobotSimulator();
			simulator.addAlgorithm(new SonicAlgorithmV1());
			simulator.addAlgorithm(new SonicAlgorithmV2(new DijkstraAlgorithm(jb.getGame())));
			simulator.addAlgorithm(new SonicAlgorithmV3(new DijkstraAlgorithm(jb.getGame())));
			simulator.simulate(scenario);
			algorithm = simulator.getBest();
			robot = new AutomaticRobot(algorithm);
			robot.setNewGameStatus(jb.getGame());

			System.out.println("[Robot Factory] Created the robot we can make!");

			
			notification.setVisible(false);
			notification.dispose();
			dialog.setVisible(false);
			dialog.dispose();
		});

		JButton btn_submit = new JButton("Create robot");
		btn_submit.addActionListener((e) -> {
			int ind = cmb_Algo.getSelectedIndex();

			Game game = MyFrame.this.jb.getGame();
			RobotPathFindingAlgorithm pathAlgorithm = new DijkstraAlgorithm(game);
			RobotAlgorithm strategyAlgorithm = null;
			switch(ind) {
			case 0:
				strategyAlgorithm = new SonicAlgorithmV1();
				break;
			case 1:
				strategyAlgorithm = new SonicAlgorithmV2(pathAlgorithm);
				break;
			case 2:
				strategyAlgorithm = new SonicAlgorithmV3(pathAlgorithm);
				break;
			}


			MyFrame.this.robot = new AutomaticRobot(strategyAlgorithm); 

			System.out.println("[Robot Factory] Created a new Robot!");

			dialog.setVisible(false);
			dialog.dispose();
		});

		dialog.add(lbl_paths);
		dialog.add(cmb_Path);
		dialog.add(lbl_algos);
		dialog.add(cmb_Algo);
		dialog.add(btn_choose);
		dialog.add(btn_submit);

		return dialog;


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
