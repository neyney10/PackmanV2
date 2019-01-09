package GUI.Animation;

import javax.swing.JPanel;

import AI.AutomaticRobot;
import DB.DataBase;
import DB.GameScores;
import GUI.JBackground;
import Game.Game;
import Robot.Play;

/**
 * AutoRun class is a thread which simply calls "panel.repaint" and "play.rotate" on every step.
 * between each step the thread sleeps for "RefreshDelayRate" milliseconds.
 * @author neyne
 *
 */
public class AutoRun extends Thread {

	private JBackground GUI;
	private int refreshDelayRate;
	private Play play;
	private AutomaticRobot robot;

	/**
	 * [Constructor] <br>
	 * Create a new Auto Run thread for auto running the game frame rate.
	 * @param GUI
	 * @param refreshDelayRate milliseconds delay between each step/frame 
	 */
	public AutoRun(JBackground GUI, int refreshDelayRate) {
		this.GUI = GUI;
		this.refreshDelayRate = refreshDelayRate;
		this.play = GUI.getPlay();
		setDaemon(true);
	}

	/**
	 * Add an automatic robot as a player to this auto run.
	 * @param robot - the automatic robot to assign.
	 */
	public void addAutomaticRobot(AutomaticRobot robot) {
		this.robot = robot;
	}

	@Override
	public void run() {
		double orientation;
		try {
			while(this.play.isRuning()) {

				// refresh - repaint panel
				GUI.repaint();

				Thread.sleep(refreshDelayRate); 

				if(robot != null) {
					synchronized (GUI.getGame()) {
						robot.setNewGameStatus(GUI.getGame());
						orientation = robot.getOrientation();
					}

				} else orientation = GUI.getGame().getPlayer().getOrientation();

				// advance game by a single step
				play.rotate(orientation);

			}
		} catch (InterruptedException e) {}

		System.out.println("***** Game Statistics *******");
		System.out.println(play.getStatistics());
		
		double[] scores = getScoreDataFromDB();
		GUI.setAvgAll(scores[0]);
		GUI.setAvgScenario(scores[1]);
		GUI.setAvgPlayer(scores[2]);
		
		GUI.repaint();
		
	}
	
	/**
	 * returns a 3 score result, [0] is the average score of all games and players, [1] is the average score
	 * of all games in this scenario. [2] is the average of all games of this player in the scenario.
	 * @return an array of score results from DB
	 */
	private double[] getScoreDataFromDB() {
		DataBase db = new DataBase("jdbc:mysql://ariel-oop.xyz:3306/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false",
				"student", 
				"student");
		
		GameScores gameScores = new GameScores(db);
		double avgAll = gameScores.averageScoreForAllScenarios();
		double avgMap = gameScores.averageScoreForScenario(GUI.getScenarioHashCode());
		double avgPlayer;
		if(robot == null)
			avgPlayer = gameScores.averageScoreForScenario(GUI.getScenarioHashCode(), Long.parseLong(GUI.getPlayerID()));
		else avgPlayer = 0;

		double[] scores = {avgAll, avgMap, avgPlayer};
		
		return scores;
	}


}