package Maps;


import java.util.HashMap;

public class MapFactory {
	
	private static HashMap<MapType, Map> maps = new HashMap<>();
	public enum MapType {
		ArielUniversity
	}
	
	public static Map getMap(MapType type) {
		Map map = maps.get(type);
		
		if(map == null) {
			switch(type) {
				case ArielUniversity:
					map = new ArielMap();
			}
		}
		
		return map;
	}
	
	

	

}
