package Algorithms;

import Game.Game;
import GameObjects.Player;
import Geom.Point3D;
import Path.Path;

public interface RobotPathFindingAlgorithm {
	
	public Path calculate(Point3D source, Point3D destination);
	
	public void refreshGameStatus(Game game);
	
}
