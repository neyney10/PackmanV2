package Algorithms;

import Game.Game;
import GameObjects.Player;
import Geom.Point3D;
import Path.Path;

/**
 * An interface for path-finding algorithm meant to be used by a "robot".
 * the interface only describe ways of reaching certain locations in game, it does not
 * decide where the player should go, unless the Robot's strategy algorithm takes into account
 * data received from this interface in its calculations.
 * @author Ofek Bader
 */
public interface RobotPathFindingAlgorithm {
	
	public Path calculate(Point3D source, Point3D destination);
	
	/**
	 * Refresh/Update game status, so the algorithm will have the ability to calculate 
	 * correct and effective result relative to current game's status.
	 * @param game
	 */
	public void refreshGameStatus(Game game);
	
}
