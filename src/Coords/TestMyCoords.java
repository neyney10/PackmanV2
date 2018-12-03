package Coords;

import Geom.Point3D;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Testing class using JUNIT4 for testing "MyCoords" class
 * @author Ofek Bader
 *
 */
public class TestMyCoords {

	static MyCoords m;
	static double approximity;
	static double pointApprox;
	
	@BeforeClass
    public static void oneTimeSetUp() {
        m = new MyCoords(); 
        approximity = 0.1; //meters
        pointApprox = 0.00001; //polar
    }

	
	/**
	 * Testing the Distance value returned by given points, also make use of the"VectorNormal2D" and "VectorNormal" functions.
	 */
	@Test
	public void testDistance() {
		System.out.println("-------- Distance3D tests ----------");
		
		Point3D b9 = new Point3D(32.103315 ,35.209039,670);
		Point3D humus = new Point3D(32.106352,35.205225,650);

		double dist = m.distance3d(b9, humus);
		double distByVector = m.vectorNormal(m.vector3D(b9, humus));
		double distByVectorBoaz = m.vectorNormal2D(m.vector3D(b9, humus));
		
		assertTrue("distance should be around 493.6",dist > (493.6-approximity) && dist < (493.6+approximity));
		System.out.println("Distance3D: "+ dist + " | distByVector: " + distByVector + " | distByVectorBoaz: " + distByVectorBoaz);
	}
	
	/**
	 * Tests the Vector3D function, make use of "add" function/
	 */
	@Test
	public void testVector3D() {
		System.out.println("-------- Vector tests ----------");
		
		Point3D b9 = new Point3D(32.103315 ,35.209039,670);
		Point3D humus = new Point3D(32.106352,35.205225,650);
		
		Point3D v3d = m.vector3D(b9, humus);
		System.out.println("vector3D Boaz: "+v3d);
		b9 = m.add(b9,v3d);
		System.out.println("humus: "+humus);
		System.out.println("should be humus: "+b9);
		
		assertTrue("Should be same as Humus point",( humus.x() > (b9.x()-pointApprox) && humus.x() < (b9.x()+pointApprox) ) &&
				( humus.y() > (b9.y()-pointApprox) && humus.y() < (b9.y()+pointApprox) ) &&
				( humus.z() > (b9.z()-pointApprox) && humus.x() < (b9.z()+pointApprox) ));
		
	}
	
	/**
	 * testing azimuth elevation and distance function. the distance function is already tested in another case.
	 */
	@Test
	public void testAzimuth() {
		System.out.println("-------- Azimuth tests ----------");
		
		Point3D b9 = new Point3D(32.103315 ,35.209039,670);
		Point3D humus = new Point3D(32.106352,35.205225,650);

		double[] azi = m.azimuth_elevation_dist(b9, humus);
		System.out.println(azi[0]+" | " +azi[1] + " | "+azi[2]);
		
		assertTrue("Should return correct elevation values" ,(azi[0] > 313.23-approximity && azi[0] < 313.23+approximity ));
		assertTrue("Should return correct elevation values" ,(azi[1] > -2.32-approximity && azi[1] < -2.32+approximity ));

		
	}

}
