package GUI.Animation;

import javax.swing.JPanel;

import AI.AutomaticRobot;
import GUI.JBackground;
import Game.Game;
import Robot.Play;

public class AutoRun extends Thread {

	private JBackground GUI;
	private int refreshDelayRate;
	private Play play;
	private AutomaticRobot robot;

	public AutoRun(JBackground GUI, int refreshDelayRate) {
		this.GUI = GUI;
		this.refreshDelayRate = refreshDelayRate;
		this.play = GUI.getPlay();
		setDaemon(true);
	}

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