package AI;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import Algorithms.RobotAlgorithm;
import Game.Game;
import Geom.Point3D;
import Maps.MapFactory;
import Maps.MapFactory.MapType;
import Robot.Play;

/**
 * A simulator class for robot to train/test his different algorithms on a specific scenario.
 * Eventually the class shall return the algoritim with the best score.
 * @author Ofek Bader
 *
 */
public class RobotSimulator {
	private ArrayList<RobotAlgorithm> algorithms;
	private RobotAlgorithm bestAlgorithm;
	private double bestScore;

	/**
	 * [Constructor] <br>
	 * Create a new Robot simulator (Initializing the Algorithms list ).
	 */
	public RobotSimulator() {
		this.algorithms = new ArrayList<RobotAlgorithm>();
	}

	/**
	 * add another algorithm to the list.
	 * @param algorithm
	 */
	public void addAlgorithm(RobotAlgorithm algorithm) {
		this.algorithms.add(algorithm);
	}

	/**
	 * Simulate gameplay, test all algorithms on given scenario and eventually
	 * sets the best algorithm in the class which can be retrieved by "getBest" function.
	 * @param scenario
	 */
	public void simulate(String scenario) {
		bestScore = -10000;

		var iter = algorithms.iterator();

		// CREATE a threadpool of size 3 to speed up things with simulation.
		// with threadpool we will have the ability to test different algorithms
		// in parrallel at the same time.
		ExecutorService executor = Executors.newFixedThreadPool(3);
		Future<Double>[] results = new Future[algorithms.size()];

		// start each algorithms in a thread, store results in
		// "results" array.
		for(int i = 0 ; i<algorithms.size() && iter.hasNext() ; i++) {
			RobotAlgorithm algorithm = iter.next();
			AutomaticRobot robot = new AutomaticRobot(algorithm);
			Play play = new Play(scenario);
			results[i] = executor.submit(new testRun(play, robot));
		}


		// iterate over the results and check who has the best score.
		for(int i = 0 ; i<results.length;i++) {
			double score = -10000;
			try {
				score = results[i].get();

				if(score > bestScore) {
					bestScore = score;
					bestAlgorithm = algorithms.get(i);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.out.println("ERROR IN: "+i);
				e.printStackTrace();
			}
		}
		
		 executor.shutdownNow();
	}


	/**
	 * Returns the best algorithm simulated.
	 * @return
	 */
	public RobotAlgorithm getBest() {
		return bestAlgorithm;
	}


	//////////////// PRIVATE /////////////

	/**
	 * TestRun class which tests a robot (with an algorithm) in a play scenario.
	 * Because the tests takes time to complete, its highly recommended to use a new Thread for
	 * each simulation. best is using a threadpool if have multiple simulations and test scenarios.
	 * @author Ofek Bader
	 *
	 */
	class testRun implements Callable<Double>{
		Play play;
		AutomaticRobot robot;

		public testRun(Play play, AutomaticRobot robot)
		{
			this.play = play;
			this.robot = robot;
		}

		@Override
		public Double call() throws Exception {
			Game game = new Game(play.getBoard());
			game.setMap(MapFactory.getMap(MapType.ArielUniversity));
			robot.setNewGameStatus(game);
			Point3D playerStartPos = robot.getStartingPlayerPosition();

			play.setInitLocation(playerStartPos.y(), playerStartPos.x());

			game.refreshGameStatus(play.getBoard());
			
			play.start();
	
			while(play.isRuning()) {
				double orientation = robot.getOrientation();

				play.rotate(orientation);

				game.refreshGameStatus(play.getBoard());
				robot.setNewGameStatus(game);
				
			}
			play.stop();

			String[] lines = play.getStatistics().split(",");
			double score = Double.parseDouble(lines[2].substring(6, lines[2].length()));
			//TEMP
			System.out.println("*SCORE: "+score);
			return score;
		}


	}



}
