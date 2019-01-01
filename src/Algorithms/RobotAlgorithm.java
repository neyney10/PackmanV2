package Algorithms;

import Game.Game;
import Geom.Point3D;

public interface RobotAlgorithm {

	public void calculate();
	
	public double getPlayerOrientation();
	
	public Point3D getPlayerStartPosition();
	
	public void refreshGameStatus(Game game);
}
