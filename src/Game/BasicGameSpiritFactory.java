package Game;

import GUI.GameSpirit;
import GameObjects.GameObject;

public interface BasicGameSpiritFactory {

	/**
	 * Create game spirit from GameObject Point3D geodetic coordinates
	 * @param obj
	 * @return GameSpirit
	 */
	public GameSpirit createGameSpirit(GameObject obj);
	
	/**
	 * Crate game spirit from GameObject from already given (x,y) pixel position.
	 * @param obj
	 * @return GameSpirit
	 */
	public GameSpirit createGameSpiritXY(GameObject obj,int x, int y);
}
