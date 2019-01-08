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
import java.awt.Toolkit;
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


	// Simulation
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
	 * 
	 * @return JMenuBar the created menu bar
	 */
	private JMenuBar initMenuBar() {
		JMenuBar menubar = new JMenuBar();

		menubar.add(initFileMenu());
		menubar.add(initGameObjectMenu());
	

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
				MyFrame.this.createRobot();
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
	 * 
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
		menu.add(clear);

		return menu;
	}

	/**
	 * Initialize menu for game objects
	 * 
	 * @return JMenu game objects menu
	 */
	private JMenu initGameObjectMenu() {
		JMenu menuObjects = new JMenu("Game Settings");
		menuObjects.setFont(menuFont);
		menuObjects.setCursor(handCursor);
		menuObjects.setBorder(BorderFactory.createSoftBevelBorder(0));

	

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

		// TEMP
		JMenuItem gmobjShowStatistics = new JMenuItem("Show statistics");
		gmobjShowStatistics.setFont(itemFont);
		gmobjShowStatistics.setCursor(handCursor);
		gmobjShowStatistics.setBackground(Color.ORANGE);
		gmobjShowStatistics.setIcon(fruitIcon);
		gmobjShowStatistics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jb == null)
					return;

				jb.setShowStatistics(!jb.isShowStatistics());
				jb.repaint();
			}
		});

		menuObjects.add(gmobjAddPlayer);
		menuObjects.add(gmobjShowStatistics);


		return menuObjects;
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
			jb.updateMapWithNewScreenSize(this.getWidth() - 40, this.getHeight() - 40);

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
		stop();
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
		if (jb == null)
			return;

		FileDialog fd = new FileDialog(new Frame(), "Load Game File (CSV)", FileDialog.LOAD);
		fd.setDirectory("/");
		fd.setFile("*.csv");
		fd.setVisible(true);

		if (fd.getFile() == null)
			return;

		scenario = fd.getFiles()[0].getAbsolutePath();
		Play play = new Play(fd.getFiles()[0].getAbsolutePath());
		play.setIDs(999999888);

		jb.setPlay(play);
		jb.setGame(new Game(play.getBoard()));

		stop(); // stopping simulation if any.

	}

	/**
	 * running the current game on automatic mode, a robot plays as the player.
	 */
	public void runGame() {
		if (jb == null || jb.getGame() == null || jb.getGame().isEmpty() || jb.getMap() == null || scenario == null) // ||
																														// simulating
			return;

		jb.setPlay(new Play(scenario));
		Play play = jb.getPlay();

		if (robot == null) {
			// Configure PathFinding algorithm for the robot
			RobotPathFindingAlgorithm pathFinding = new DijkstraAlgorithm(jb.getGame());
			// Configure Robot tactic algorithm, load it with the path algorithm.
			RobotAlgorithm algorithm = new SonicAlgorithmV3(pathFinding);
			// Configure the robot itself with the tactic algorithm and update it status.
			robot = new AutomaticRobot(algorithm);
		} else robot = robot.clone();
		robot.setNewGameStatus(jb.getGame());

		// get Robot's chosen starting position.
		Point3D playerPos3D = robot.getStartingPlayerPosition();

		play.setInitLocation(playerPos3D.y(), playerPos3D.x());

		// start game.
		play.start();
		jb.repaint();

		/*
		 * Create a thread handler for handling the game's frame-rate/refresh mechanism.
		 * give it the robot to handle at each frame.
		 */
		autoRun = new AutoRun(jb, 22);
		autoRun.addAutomaticRobot(robot);
		autoRun.start();

		if (jb.getPlay().isRuning()) {
			btn_stopSimulation.setVisible(true);
			btn_run.setVisible(false);
			btn_run_manual.setVisible(false);
		}

	}

	/**
	 * Run current game manually as the user plays the game and controls the player.
	 */
	public void runGameManual() {
		if (jb == null || jb.getGame() == null || jb.getGame().isEmpty() || jb.getMap() == null) // || simulating
			return;

		Point3D playerPos3D = jb.getGame().getPlayer().getPoint();
		if(playerPos3D == null || playerPos3D.equals(new Point3D(0,0,0))) //abort
			return;
		
		System.out.println(playerPos3D);
		jb.setPlay(new Play(scenario));
		Play play = jb.getPlay();

		play.setInitLocation(playerPos3D.y(), playerPos3D.x());

	
		// get ids
		String[] ids = showEnterIDDialog();
		if(ids == null || ids.length == 0)
			return;
		else if(ids.length == 1)
			play.setIDs(Long.parseLong(ids[0]));
		else if(ids.length == 2)
			play.setIDs(Long.parseLong(ids[0]),Long.parseLong(ids[1]));
		else if(ids.length == 3)
			play.setIDs(Long.parseLong(ids[0]),Long.parseLong(ids[1]),Long.parseLong(ids[2]));
		
		// start game.
		play.start();
		jb.repaint();

		autoRun = new AutoRun(jb, 33);
		autoRun.start();

		if (jb.getPlay().isRuning()) {
			btn_stopSimulation.setVisible(true);
			btn_run.setVisible(false);
			btn_run_manual.setVisible(false);
		}
	}
	
	/**
	 * Creates and shows on screen a dialog for entering ID's
	 * @returns ID's in strings format.
	 */
	public String[] showEnterIDDialog() {
		DialogID dialog  = new DialogID(this, "Please insert your ID!", true);
		return dialog.showDialog();
	}

	
	/**
	 * Add or re-locate player in-game
	 */
	private void addPlayer() {
		if (jb == null)
			return;

		jb.setDropItem(new Player(0));
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
		// if (!simulating)
		if (jb == null || autoRun == null)
			return;

		// simulation.interrupt();
		jb.getPlay().stop();
		autoRun.interrupt();

		try {
			// simulation.join();
			autoRun.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		if (jb == null || jb.getGame() == null)
			return;



		btn_run.setVisible(true);
		btn_run_manual.setVisible(true);
		btn_stopSimulation.setVisible(false);

		simulating = false;
	}

	/**
	 * Calculate path's of all pacmans in game by using the ShortestPathAlgo class
	 * to compute. the class set the path on each pacman after calculation.
	 */
	public void createRobot() {
		JDialog dialog = createRobotDialog();
		dialog.setModal(true);
		dialog.setVisible(true);

		// repaint
		repaint();
	}

	public JDialog createRobotDialog() {
		JDialog dialog = new JDialog(this, "Robot factory! Create your own robot!");
		dialog.setSize(755, 133);
		dialog.setLayout(new FlowLayout(1, 25, 15));

		// Path's label
		JLabel lbl_paths = new JLabel("Path Finding Algorithm: ");

		// Path's cmb row descriptions
		String[] paths = { "Dijkstra" };

		// combo box of path finding algo
		JComboBox<String> cmb_Path = new JComboBox<>(paths);
		cmb_Path.addActionListener((e) -> {

		});

		// Strategy algo label
		JLabel lbl_algos = new JLabel("Strategy Algorithm: ");

		// Combo box description of algorithm.
		RobotAlgorithm[] algos = { new SonicAlgorithmV1(), new SonicAlgorithmV2(null), new SonicAlgorithmV3(null) };

		// combo box of algorithms.
		JComboBox<RobotAlgorithm> cmb_Algo = new JComboBox<>(algos);
		cmb_Algo.addActionListener((e) -> {

		});

		JButton btn_choose = new JButton("Choose Best");
		btn_choose.addActionListener((e) -> {
			if (MyFrame.this.scenario == null)
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
			switch (ind) {
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
	
	
	
	
	
	
	
	///////// OTHER ////////
	
	class DialogID extends JDialog {
		String[] ids;
		
		/**
		 * Create new JDialog of subtype DialogID
		 * @param myFrame - frame
		 * @param string - title
		 * @param b - is modal (set to true)
		 */
		public DialogID(MyFrame myFrame, String string, boolean b) {
			super(myFrame, string, b);
		}

		/**
		 * Use this when wanting to show the dialog, returns the user CSV input of ids.
		 * @return ids
		 */
		public String[] showDialog() { 
			center();
			
			// set layout
			this.setLayout(new FlowLayout());
			
			// create text field to enter ID's. seperated by comma
			JTextField inp_id = new JTextField("999999888");
			inp_id.setColumns(33);
			
			// create "continue"/"submit" button
			JButton btn_submit = new JButton("Continue");
			btn_submit.addActionListener((e) -> {
				String[] csv = inp_id.getText().split(",");
				ids = csv;
				this.setVisible(false);
				this.dispose();
			});
			
			this.add(inp_id);
			this.add(btn_submit);
			
			this.pack();
			
			this.setVisible(true);
			
			return ids;
		}
		
		/**
		 * Center the dialog on screen
		 */
		private void center() {
			final int width = this.getWidth();
		    final int height = this.getHeight();
		    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    int x = (screenSize.width / 2) - (width / 2);
		    int y = (screenSize.height / 2) - (height / 2);

		    this.setLocation(x, y);
		}
		
	}

}
