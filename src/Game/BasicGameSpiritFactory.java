package Game;

import GUI.GameSpirit;
import GameObjects.BasicGameSpirit;
import GameObjects.GameObject;

public interface BasicGameSpiritFactory {

	/**
	 * Create game spirit from BasicGameSpirit Point3D geodetic coordinates
	 * @param obj
	 * @return GameSpirit
	 */
	public GameSpirit createGameSpirit(BasicGameSpirit obj);
	
	/**
	 * Crate game spirit from BasicGameSpirit from already given (x,y) pixel position.
	 * @param obj
	 * @return GameSpirit
	 */
	public GameSpirit createGameSpiritXY(BasicGameSpirit obj,int x, int y);
}
