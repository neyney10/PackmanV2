package Maps;


import java.util.HashMap;

/**
 * Map Factory! you can generate/get maps with this class!
 * using the FlyWeight design pattern to store already created instances of maps..
 * please use MapFactory.MapType ENUM to request new maps with the "getMap(MapType)" function
 */
public class MapFactory {
	
	private static HashMap<MapType, Map> maps = new HashMap<>();
	/**
	 * Enums of different maps.
	 */
	public enum MapType {
		ArielUniversity,
		TelAviv
	}
	
	/**
	 * get a new Map from this factory, request a map using the MapFactory.MapType.type ENUM.
	 * @param MapType type ENUM
	 * @return Map object.
	 */
	public static Map getMap(MapType type) {
		Map map = maps.get(type);
		
		if(map == null) {
			switch(type) {
				case ArielUniversity:
					map = new ArielMap();
					break;
				case TelAviv:
					map = new TelAvivMap();
					break;
			}
			maps.put(type, map);
		}
		
		return map;
	}
	

	
	

	

}
