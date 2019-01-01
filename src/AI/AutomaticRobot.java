package AI;
import Algorithms.RobotAlgorithm;
import Game.Game;
import GameObjects.Player;
import Geom.Point3D;
import Robot.Play;

public class AutomaticRobot {
	
	RobotAlgorithm algorithm;
	Player player;
	
	public AutomaticRobot(RobotAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public double getOrientation() {
		return algorithm.getPlayerOrientation();
	}
	
	public Point3D getStartingPlayerPosition() {
		return algorithm.getPlayerStartPosition();
	}
	
	public void setNewGameStatus(Game game) {
		algorithm.refreshGameStatus(game);
		algorithm.calculate();
	}
}

