package GUI.Animation;

import javax.swing.JPanel;

import AI.AutomaticRobot;
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
		GUI.repaint();
		
	}


}