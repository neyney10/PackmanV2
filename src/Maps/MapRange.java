package Maps;

/**
 * Stores the polar/geodetic map range coordinates,
 * have different mathods of Width and Height.
 */
public class MapRange {
	double x1,y1,x2,y2;
	
	/**
	 * [Constructor] <br>
	 * create a new MapRange.
	 * @param x1 - left top latitude.
	 * @param y1 - left top lontitude.
	 * @param x2 - right bottom latitude.
	 * @param y2 - right bottm lontitude.
	 */
	public MapRange(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/**
	 * [Constructor] <br>
	 * create a new MapRange.
	 * @param point2d left top point (polar -> (lat,lon,alt))
	 * @param point2d2 right bottom point (polar -> (lat,lon,alt))
	 */
	public MapRange(javafx.geometry.Point2D point2d, javafx.geometry.Point2D point2d2) {
		this(point2d.getX(), point2d.getY(), point2d2.getX(), point2d2.getY());
	}

	/**
	 * get the left top point latitude.
	 * @return latitude in double precision.
	 */
	public double getX1() {
		return x1;
	}

	/**
	 * set new latitude for the top left point corner.
	 * @param x1 polar
	 */
	public void setX1(double x1) {
		this.x1 = x1;
	}

	/**
	 *  get the left top point longitude.
	 * @return longitude in double precision.
	 */
	public double getY1() {
		return y1;
	}

	/**
	 * set new longitude in the top left point corner.
	 * @param y1 polar
	 */
	public void setY1(double y1) {
		this.y1 = y1;
	}

	/**
	 * get the bottom right point latitude.
	 * @return latitude in double precision.
	 */
	public double getX2() {
		return x2;
	}

	/**
	 * set new latitude in the bottom right point corner.
	 * @param x2 polar
	 */
	public void setX2(double x2) {
		this.x2 = x2;
	}

		/**
	 * get the bottom right point longitude.
	 * @return longitude in double precision.
	 */
	public double getY2() {
		return y2;
	}

		/**
	 * set new longitude in the bottom right point corner.
	 * @param y2 polar
	 */
	public void setY2(double y2) {
		this.y2 = y2;
	}
	
	/**
	 * get Delta latitude absolute value
	 * @return delta latitude.
	 */
	public double getWidth() {
		return Math.abs(x2-x1);
	}
	
	/**
	 * get Delta longitude absolute value.
	 * @return delta longitude.
	 */
	public double getHeight() {
		return Math.abs(y2-y1);
	}


}
