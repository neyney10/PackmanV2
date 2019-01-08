package AI;
import Algorithms.RobotAlgorithm;
import Algorithms.SonicAlgorithmV3;
import Game.Game;
import GameObjects.Player;
import Geom.Point3D;
import Robot.Play;

/**
 * An automatic robot which plays the game with the "Play" offline server.
 * Can be "loaded" with different algorithms, and provides the exact interface required by the "Play" offline server.
 * @author Ofek Bader
 *
 */
public class AutomaticRobot{
	
	// The strategy algorithm.
	RobotAlgorithm algorithm;
	
	/**
	 * [Constructor] <br>
	 * Create a new robot with the specified algorithm strategy.
	 * @param algorithm (RobotAlgorithm) - strategy.
	 */
	public AutomaticRobot(RobotAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * get the next move of this robot for the next step of the game.
	 * @return orientation to move towards.
	 */
	public double getOrientation() {
		return algorithm.getPlayerOrientation();
	}
	
	/**
	 * Get the first starting point/position of the robot for the player initialization of "Play".
	 * @return Point3D starting position of the player.
	 */
	public Point3D getStartingPlayerPosition() {
		return algorithm.getPlayerStartPosition();
	}
	
	/**
	 * Update/refresh the game status - notify the robot about game changes.
	 * @param game
	 */
	public void setNewGameStatus(Game game) {
		algorithm.refreshGameStatus(game);
		algorithm.calculate();
	}
	
	/**
	 * Clone this robot and its algorithm.
	 */
	public AutomaticRobot clone() {
		return new AutomaticRobot(algorithm.clone());
	}

}

