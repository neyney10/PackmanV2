package Algorithms;

import Game.Game;
import Geom.Point3D;

/**
 * An interface for Robot's game strategy.
 * The strategy algorithm shall make choices in game depending on current game situation and
 * its inner-algorithms such as Path-Finding algorithm, the algorithm shall also decide the 
 * starting position the robot's think is best according to the strategy.
 * @author Ofek Bader
 */
public interface RobotAlgorithm {
	
	/**
	 * Calculate strategy, recommended if not must to call this function before making
	 * any game-decision based choice such as Movement orientation and starting position.
	 */
	public void calculate();
	
	/**
	 * Get player's next move according to inner algorithm.
	 * @return Orientation in degrees [0-360] in double precision.
	 */
	public double getPlayerOrientation();
	
	/**
	 * Get the player starting position chosen by the robot. <br>
	 * Note: some algorithm would need to be calculated first using the "calculate()" function
	 * to generate the starting position.
	 * @return Point3D starting position in game (in Polar)
	 */
	public Point3D getPlayerStartPosition();
	
	/**
	 * Refresh/Update game status, so the algorithm will have the ability to calculate 
	 * correct and effective result relative to current game's status.
	 * @param game
	 */
	public void refreshGameStatus(Game game);
}
