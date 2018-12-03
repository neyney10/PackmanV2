package Coords;

import Geom.Point3D;

/**
 * class that manipulates GPS (geodetic/polar) points, can move a point by a vector (direction and length),
 * calculate direction (azimuth) and elevation, distance and more.
 * @author Ofek Bader
 */
public class MyCoords implements coords_converter {

	private final static double earth_radius = 6378137;//ESTIMATE: 6371000;
	private final static double earth_lon_norm = 0.84709;


	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		Point3D newp = m2p(local_vector_in_meter);
		newp.add(gps);
		return newp;
	}

	@Override
	public double distance3d(Point3D gps0, Point3D gps1) {
		
		// BOAZ'S ALGORITHM
		
//		double d = earth_radius*Math.sin(Point3D.d2r(gps1.x()-gps0.x()));
//		double d2 = earth_radius*earth_lon_norm*Math.sin(Point3D.d2r(gps1.y()-gps0.y()));
//		double d3 = gps1.z() - gps0.z();
//		double distance = Math.sqrt((d*d+d2*d2+d3));

		// more accurate algorithm calculation.
		
		double deltaPhi,distance,deltaLamda,phi1,phi2,alphaRad,alphaDeg;
		deltaLamda = Math.toRadians(gps1.y()) - Math.toRadians(gps0.y());
		phi1 = Math.toRadians(gps0.x());
		phi2 = Math.toRadians(gps1.x());
		deltaPhi = phi2 - phi1;
		double a = Math.pow(Math.sin(deltaPhi/2),2)+Math.cos(phi1)*Math.cos(phi2)*Math.pow(Math.sin(deltaLamda/2),2);
		double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
		distance =  earth_radius * c;
		


		return distance;
	}

	

	/**
	 * Get the vector normal 3D value.
	 * @param vector to get the normal from
	 * @return double as the normal
	 */
	public double vectorNormal(Point3D vector) {
		return Math.sqrt(vector.x()*vector.x()+vector.y()*vector.y()+vector.z()*vector.z());
	}
	
	/**
	 * Get the vector normal 2D value.
	 * @param vector to get the normal from
	 * @return double as the normal
	 */
	public double vectorNormal2D(Point3D vector) {
		return Math.sqrt(vector.x()*vector.x()+vector.y()*vector.y());
	}


	@Override
	public Point3D vector3D(Point3D gps0, Point3D gps1) {
		double lon_norm = Math.cos(Math.toRadians(gps0.x()));
		double x0 = earth_radius*Math.sin(Point3D.d2r(gps1.x()-gps0.x()));
		double y0 = earth_radius*lon_norm*Math.sin(Point3D.d2r(gps1.y()-gps0.y()));
		double z0 = gps1.z()-gps0.z();
		
		return new Point3D(x0,y0,z0);
	}



	

	@Override
	public double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1){
		double[] arr = new double[3];

		/*
		 * deltaLamba =  longitude(gps1) - longitude(gps0) in radians
		 * phi1 = latitude(gps0) in radians
		 * phi2 = latitude(gps1) in radians
		 * alphaRad = the Azimuth angle in radians
		 * alphaDeg = the Azimuth angle in degrees
		 */
		double deltaLamda,phi1,phi2,alphaRad,alphaDeg;
		deltaLamda = Math.toRadians(gps1.y()) - Math.toRadians(gps0.y());
		phi1 = Math.toRadians(gps0.x());
		phi2 = Math.toRadians(gps1.x());

		double x = Math.sin(deltaLamda)*Math.cos(phi2);//Split for checking
		double y = (Math.cos(phi1)*Math.sin(phi2))-(Math.sin(phi1)*Math.cos(phi2)*Math.cos(deltaLamda));//split for checking
		alphaRad = Math.atan2(x,y);
		alphaDeg = Math.toDegrees(alphaRad);
		if (alphaDeg<0)//just so it will give positive degree
			alphaDeg = 360+alphaDeg;

		arr[0] = alphaDeg;
		double A = (gps1.z()-gps0.z());
		double C = distance3d(gps0,gps1);
		double B = Math.sqrt(C*C-A*A);
		arr[1] = Math.toDegrees(Math.atan(A/B));
		arr[2] = distance3d(gps0, gps1);
		System.out.println("Tan-1: "+ Math.toDegrees(Math.atan(A/B))+" | Sin-1: " + Math.toDegrees(Math.asin(A/C)));
		System.out.println("angle:" +Math.toDegrees(p2m(gps0).angleZ(p2m(gps1))));




		return arr;
	}

	
	/**
	 * returns TRUE if the given point is a valid GPS point by the following criteria: <Br>
	 * Lat: (-90, 90), Lon: (-180.180), Alt:(-450,inf)
	 * @param p
	 * @return true iff the point is a valid GPS point.
	 */
	public boolean isValid_GPS_Point_Rotated(Point3D p) {

		// if lon is out of range of (-180, 180) than it's an invalid GPS point
		if(p.y() < -180 && p.y() > 180)
			return false;

		// if lat is out of range of (-90, 90) than it's an invalid GPS point
		if(p.x() < -90 && p.x() > 90)
			return false;

		// if alt is out of range of (-450, +inf) than it's an invalid GPS point
		if(p.z() < -450)
			return false;

		return true;
	}
	
	@Override
	public boolean isValid_GPS_Point(Point3D p) {

		// if lat is out of range of (-180, 180) than it's an invalid GPS point
		if(p.x() < -180 && p.x() > 180)
			return false;

		// if lon is out of range of (-90, 90) than it's an invalid GPS point
		if(p.y() < -90 && p.y() > 90)
			return false;

		// if alt is out of range of (-450, +inf) than it's an invalid GPS point
		if(p.z() < -450)
			return false;

		return true;
	}


	/**
	 * Converts a point from polar coordinates to meters
	 * @param gps0 the point to convert
	 * @return new Point3D in meters
	 */
	public Point3D p2m(Point3D gps0) {
		double x = earth_radius*Math.sin(Point3D.d2r(gps0.x()));
		double y = earth_radius*earth_lon_norm*Math.sin(Point3D.d2r(gps0.y()));
		
		return new Point3D(x,y,gps0.z());
	}
	
	/**
	 * Converts a point from meter coordinates to polar coordinates
	 * @param v3d the point to convert in meters
	 * @return new Point3D in polar
	 */
	public Point3D m2p(Point3D v3d) {
		double x = Math.toDegrees(Math.asin(v3d.x()/earth_radius));
		double y = Math.toDegrees(Math.asin(v3d.y()/(earth_radius*earth_lon_norm)));
		
		return new Point3D(x,y,v3d.z());
	}

}
