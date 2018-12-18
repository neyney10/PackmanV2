
package Maps;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import GameObjects.Packman;
import Geom.Point3D;
import Maps.MapFactory.MapType;

/**
 * a JUNIT4 class for testing functionality of Map class. <br>
 * Executing multiple tests such as, Coordinate conversion, ScaleFactor manipulation, and distance calculation
 * both in pixels and meters.
 * @author Neyney
 */
public class testMap {


	/**
	 * Because of the Flyweight MAP Pool ("Cache")
	 * the same map is shared between tests, so we need to reset it.
	 * @param Mp the map to rest
	 */
	private void restMap(Map map) { 
		map.scaleFactorY = 1;
		map.scaleFactorX = 1;
		map.screenRange.width = map.getOriginalScreenWidth();
		map.screenRange.height = map.getOriginalScreenHeight();
	}
	
	/**
	 * Testing the conversion of points from GSP-Geodetic type coordinates to
	 * pixel-type coordinates used on screen/frame location/position and vice versa.
	 */
	@Test
	public void testCoordinatesConversionFromGPStoPixel() {
		Map map = MapFactory.getMap(MapType.ArielUniversity);
		restMap(map);
		double errorMargin = 0.00001;

		// TEST 1
		Packman p1 = new Packman(35.211222,32.104496,30,0, 1, 1);
		Point3D p3d1 = p1.getPoint();
		Point pixelPoint = map.getLocationOnScreen(p3d1);
		Point3D gpsPoint = map.getLocationFromScreen(pixelPoint);
		
		assertEquals(p3d1.x(), gpsPoint.x(), errorMargin);
		assertEquals(p3d1.y(), gpsPoint.y(), errorMargin);
		
		// TEST 2
		p1 = new Packman(35.207462,32.102482,30,1, 1, 1);
		p3d1 = p1.getPoint();
		pixelPoint = map.getLocationOnScreen(p3d1);
		gpsPoint = map.getLocationFromScreen(pixelPoint);
		
		assertEquals(p3d1.x(), gpsPoint.x(), errorMargin);
		assertEquals(p3d1.y(), gpsPoint.y(), errorMargin);

		// random test
		double lat,lon;
		int tests = 10;
		for(int i = 0 ; i<tests;i++) {
			lat = 30+Math.random()*10;
			lon = 30+Math.random()*10;
			p3d1 = new Point3D(lat,lon,0);

			pixelPoint = map.getLocationOnScreen(p3d1);
			gpsPoint = map.getLocationFromScreen(pixelPoint);

			assertEquals(p3d1.x(), gpsPoint.x(), errorMargin);
			assertEquals(p3d1.y(), gpsPoint.y(), errorMargin);
		}
	}
	
	/**
	 * Test the distance between two points on screen, the distance in Meters
	 * it first converts the Pixel-points from screen to geodetic points using the inner Map's formulas
	 * and coordinates conversion/manipulation and then using the 3rd-party "MyCoords" class to calculate the distances in meters
	 * between two geodetic points.
	 */
	@Test
	public void testDistanceMeters() {
		Map map = MapFactory.getMap(MapType.ArielUniversity);
		restMap(map);
		double distance, distanceErrorMarginInMeters = 0.75;
		
		// TEST 1
		distance = map.getDistance(new Point(551,331), new Point(555,555));
		assertEquals(117.47, distance, distanceErrorMarginInMeters);
	}
	
	/**
	 * Test the length- distance in pixels between two points on screen/frame, the distance in pixels.
	 */
	@Test
	public void testDistancePixels() {
		Map map = MapFactory.getMap(MapType.ArielUniversity);

		double distance, distanceErrorMargin = 0.1;
		
		// TEST 1
		distance = map.getDistanceByPixelRaw(new Point(551,331), new Point(555,555));
		assertEquals(224, distance, distanceErrorMargin);
		
		// TEST 2
		distance = map.getDistanceByPixelRaw(new Point(map.getScreenWidth(),0), new Point(map.getScreenWidth(),map.getScreenHeight()));
		assertEquals(map.getScreenHeight(), distance, distanceErrorMargin);

		// TEST 3
		distance = map.getDistanceByPixelRaw(new Point(0,map.getScreenHeight()), new Point(map.getScreenWidth(),map.getScreenHeight()));
		assertEquals(map.getScreenWidth(), distance, distanceErrorMargin);
		
		// TEST 4
		distance = map.getDistanceByPixelRaw(new Point(map.getScreenWidth(),map.getScreenHeight()), new Point(map.getScreenWidth(),map.getScreenHeight()));
		assertEquals(0, distance, distanceErrorMargin);
		
		// TEST 5
		distance = map.getDistanceByPixelRaw(new Point(map.getScreenWidth()/2,map.getScreenHeight()/2), new Point(map.getScreenWidth(),map.getScreenHeight()));
		assertEquals(785.5, distance, distanceErrorMargin);
		
	}
	
	/**
	 * Testing the angle in degrees between two points on screen in pixels.
	 */
	@Test
	public void testAngle() {
		Map map = MapFactory.getMap(MapType.ArielUniversity);
		restMap(map);
		double angle, angleErrorMariginInDegrees = 0.3;
		
		// TEST 1 (from down to up)
		angle = map.getAngle(new Point(551,331), new Point(551,555));
		assertEquals(0, angle, angleErrorMariginInDegrees);
		
		// TEST 2 (from left to right)
		angle = map.getAngle(new Point(5,331), new Point(125,331));
		assertEquals(90, angle, angleErrorMariginInDegrees);
		
		// TEST 3 (from right to left)
		angle = map.getAngle(new Point(125,331), new Point(3,331));
		assertEquals(270, angle, angleErrorMariginInDegrees);
		
		// TEST 4 (two edge points on screen)
		angle = map.getAngle(new Point(0,0), new Point(map.getScreenWidth(),map.getScreenHeight()));
		assertEquals(65.85, angle, angleErrorMariginInDegrees);
	}
	
	/**
	 * Testing the Scaling Factors of the map.
	 */
	@Test
	public void testScaleFactors() {
		Map map = MapFactory.getMap(MapType.ArielUniversity);
		restMap(map);
		double scaleFactorX, scaleFactorY, scaleFactorErrorMargin = 0.1;
		
		// TEST 1
		scaleFactorX = map.getScreenWidth()/map.getOriginalScreenWidth();
		scaleFactorY = map.getScreenHeight()/map.getOriginalScreenHeight();
		
		assertEquals(scaleFactorX, map.getScaleFactorX(), scaleFactorErrorMargin);
		assertEquals(scaleFactorY, map.getScaleFactorY(), scaleFactorErrorMargin);
		
		// TEST 2
		map.updateScreenRange(map.getOriginalScreenWidth()*2, map.getOriginalScreenHeight()*2);
		
		scaleFactorX *= 2;
		scaleFactorY *= 2;
		
		assertEquals(scaleFactorX, map.getScaleFactorX(), scaleFactorErrorMargin);
		assertEquals(scaleFactorY, map.getScaleFactorY(), scaleFactorErrorMargin);
		
		// TEST 3
		map.updateScreenRange((int)(map.getScreenWidth()*1.5), (int)(map.getScreenHeight()*1.2));
		
		scaleFactorX *= 1.5;
		scaleFactorY *= 1.2;

		assertEquals(scaleFactorX, map.getScaleFactorX(), scaleFactorErrorMargin);
		assertEquals(scaleFactorY, map.getScaleFactorY(), scaleFactorErrorMargin);

	}

	/**
	 * Testing the TransformByScale function, which is returning RAW X,Y points from scaled ones.
	 */
	@Test
	public void testTransform() {
		Map map = MapFactory.getMap(MapType.ArielUniversity);
		restMap(map);
		double transformErrorMargin = 0.000001;
		Point p1, p2, result;

		// test 1
		p1 = new Point(100,100);
		p2 = new Point(232,333);

		result = map.transformByScale(p1.x, p1.y);

		assertEquals(p1.x, result.x, transformErrorMargin);
		assertEquals(p1.y, result.y, transformErrorMargin);

		result = map.transformByScale(p2.x, p2.y);
		
		assertEquals(p2.x, result.x, transformErrorMargin);
		assertEquals(p2.y, result.y, transformErrorMargin);

		assertNotEquals(p1.x, result.x);
		
		// test random
		int x,y;
		int tests = 10;
		for(int i = 0 ; i<tests;i++) {
			x = (int) (Math.random()*1000);
			y = (int) (Math.random()*1000);
			p1 = new Point(x,y);

			result = map.transformByScale(x, y);

			assertEquals(p1.x, result.x, transformErrorMargin);
			assertEquals(p1.y, result.y, transformErrorMargin);
		}

		// change scales
		map.updateScreenRange((int)(map.getScreenWidth()*1.4), (int)(map.getScreenHeight()*1.4));

		// test random
		for(int i = 0 ; i<tests;i++) {
			x = (int) (Math.random()*1000);
			y = (int) (Math.random()*1000);
			p1 = new Point(x,y);
			p2 = new Point((int)(x/map.getScaleFactorX()), (int)(y/map.getScaleFactorY()));

			result = map.transformByScale(x, y);

			assertEquals(p2.x, result.x, transformErrorMargin);
			assertEquals(p2.y, result.y, transformErrorMargin);
		}
		
	}
	

}
