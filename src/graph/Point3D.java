package graph;/*
 * Decompiled with CFR 0_132.
 */

public class Point3D {
    private double _x;
    private double _y;
    private double _z;
    public static final double _epsilon = 0.009999999776482582;
    public double OldWeight;
    public static final int ONSEGMENT = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int INFRONTOFA = 3;
    public static final int BEHINDB = 4;
    public static final int ERROR = 5;
    public static final int DOWN = 6;
    public static final int UP = 7;

    public Point3D(double x1, double y1) {
        this._x = x1;
        this._y = y1;
        this._z = 0.0;
    }

    public Point3D(double x1, double y1, double z1) {
        this._x = x1;
        this._y = y1;
        this._z = z1;
    }

    public Point3D(Point3D p) {
        this._x = p._x;
        this._y = p._y;
        this._z = p._z;
    }

    public double x() {
        return this._x;
    }

    public double y() {
        return this._y;
    }

    public double z() {
        return this._z;
    }

    public int ix() {
        return (int)this._x;
    }

    public int iy() {
        return (int)this._y;
    }

    public int iz() {
        return (int)this._z;
    }

    public void set(Point3D p) {
        this._x = p._x;
        this._y = p._y;
        this._z = p._z;
    }

    public void offset(Point3D p) {
        this.offset(p._x, p._y, p._z);
    }

    public void set(double x, double y, double z) {
        this._x = x;
        this._y = y;
        this._z = z;
    }

    public void offset(double dx, double dy, double dz) {
        this._x += dx;
        this._y += dy;
        this._z += dz;
    }

    public void setX(double x) {
        this._x = x;
    }

    public void setY(double y) {
        this._y = y;
    }

    public void setZ(double z) {
        this._z = z;
    }

    public boolean equals(Point3D p) {
        if (p._x == this._x && p._y == this._y && p._z == this._z) {
            return true;
        }
        return false;
    }

    public boolean equalsXY(Point3D p) {
        if (p._x == this._x && p._y == this._y) {
            return true;
        }
        return false;
    }

    public boolean equalsIntXY(Point3D p) {
        if (p.ix() == (int)this._x && p.iy() == (int)this._y) {
            return true;
        }
        return false;
    }

    public boolean smallerXY(Point3D p) {
        if (this._x < p._x || this._x == p._x && this._y < p._y) {
            return true;
        }
        return false;
    }

    public boolean close2equalsXY(Point3D p) {
        if (Math.abs(p._x - this._x) < 0.009999999776482582 && Math.abs(p._y - this._y) < 0.009999999776482582) {
            return true;
        }
        return false;
    }

    public String key() {
        return String.valueOf(this._x) + " " + this._y + " " + this._z;
    }

    public String keyXY() {
        return String.valueOf(this._x) + " " + this._y;
    }

    public void move(double dx, double dy, double dz) {
        this._x += dx;
        this._y += dy;
        this._z += dz;
    }

    public Point3D translate(Point3D p) {
        if (p == null) {
            return null;
        }
        return new Point3D(this._x + p._x, this._y + p._y, this._z + p._z);
    }

    public double distance(Point3D p) {
        return this.distance3D(p);
    }

    public double dist2(Point3D p) {
        double dx = this._x - p._x;
        double dy = this._y - p._y;
        double dz = this._z - p._z;
        return dx * dx + dy * dy + dz * dz;
    }

    public Point3D make_Roi_point(double resolution) {
        double x = this.x() + resolution;
        double y = this.y() + resolution;
        double z = this.z();
        Point3D p2 = new Point3D(x, y, z);
        return p2;
    }

    public double distance3D(Point3D p) {
        double temp = Math.pow(p._x - this._x, 2.0) + Math.pow(p._y - this._y, 2.0) + Math.pow(p._z - this._z, 2.0);
        return Math.sqrt(temp);
    }

    public double distance2D(Point3D p) {
        double temp = Math.pow(p._x - this._x, 2.0) + Math.pow(p._y - this._y, 2.0);
        return Math.sqrt(temp);
    }

    public String toString() {
        return "[" + (int)this._x + "," + (int)this._y + "," + (int)this._z + "]";
    }

    public String toString(boolean all) {
        if (all) {
            return "[" + this._x + "," + this._y + "," + this._z + "]";
        }
        return "[" + (int)this._x + "," + (int)this._y + "," + (int)this._z + "]";
    }

    public String toFile() {
        return String.valueOf(this._x) + " " + this._y + " " + this._z + " ";
    }

    public String toFile1() {
        return "Point3D " + this._x + " " + this._y + " " + this._z;
    }

    public int pointLineTest2(Point3D a, Point3D b) {
        int flag = this.pointLineTest(a, b);
        if (a._x < b._x) {
            if (a._x <= this._x && b._x > this._x) {
                if (flag == 1) {
                    return 6;
                }
                if (flag == 2) {
                    return 7;
                }
            }
        } else if (a._x > b._x && b._x <= this._x && a._x > this._x) {
            if (flag == 2) {
                return 6;
            }
            if (flag == 1) {
                return 7;
            }
        }
        return flag;
    }

    public int pointLineTest(Point3D a, Point3D b) {
        if (a == null || b == null || a.equalsXY(b)) {
            return 5;
        }
        double dy = b._y - a._y;
        double dx = b._x - a._x;
        double res = dy * (this._x - a._x) - dx * (this._y - a._y);
        if (res < 0.0) {
            return 1;
        }
        if (res > 0.0) {
            return 2;
        }
        if (dx > 0.0) {
            if (this._x < a._x) {
                return 3;
            }
            if (b._x < this._x) {
                return 4;
            }
            return 0;
        }
        if (dx < 0.0) {
            if (this._x > a._x) {
                return 3;
            }
            if (b._x > this._x) {
                return 4;
            }
            return 0;
        }
        if (dy > 0.0) {
            if (this._y < a._y) {
                return 3;
            }
            if (b._y < this._y) {
                return 4;
            }
            return 0;
        }
        if (dy < 0.0) {
            if (this._y > a._y) {
                return 3;
            }
            if (b._y > this._y) {
                return 4;
            }
            return 0;
        }
        return 5;
    }

    public void rescale(Point3D center, Point3D vec) {
        if (center != null && vec != null) {
            this.rescale(center, vec.x(), vec.y(), vec.z());
        }
    }

    public void rescale(Point3D center, double size) {
        if (center != null && size > 0.0) {
            this.rescale(center, size, size, size);
        }
    }

    private void rescale(Point3D center, double sizeX, double sizeY, double sizeZ) {
        this._x = center._x + (this._x - center._x) * sizeX;
        this._y = center._y + (this._y - center._y) * sizeY;
        this._z = center._z + (this._z - center._z) * sizeZ;
    }

    public void rotate2D(Point3D center, double angle) {
        this._x -= center.x();
        this._y -= center.y();
        double a = Math.atan2(this._y, this._x);
        double radius = Math.sqrt(this._x * this._x + this._y * this._y);
        this._x = center.x() + radius * Math.cos(a + angle);
        this._y = center.y() + radius * Math.sin(a + angle);
    }

    public double angleXY(Point3D p) {
        if (p == null) {
            throw new RuntimeException("** Error: Point3D angle got null **");
        }
        return Math.atan2(p._y - this._y, p._x - this._x);
    }

    public double angleXY_2PI(Point3D p) {
        if (p == null) {
            throw new RuntimeException("** Error: Point3D angle got null **");
        }
        double ans = Math.atan2(p._y - this._y, p._x - this._x);
        if (ans < 0.0) {
            ans += 6.283185307179586;
        }
        return ans;
    }

    public double angleZ(Point3D p) {
        if (p == null) {
            throw new RuntimeException("** Error: Point3D angleZ got null **");
        }
        return Math.atan2(p._z - this._z, this.distance2D(p));
    }

    public double north_angle(Point3D p) {
        double ans = 0.0;
        double a_rad = Math.atan2(p._y - this._y, p._x - this._x);
        double a_deg = Math.toDegrees(a_rad);
        ans = a_deg <= 90.0 ? 90.0 - a_deg : 450.0 - a_deg;
        return ans;
    }

    public double up_angle(Point3D p) {
        double ans = 0.0;
        ans = Math.atan2(p._z - this._z, this.distance2D(p));
        return Math.toDegrees(ans);
    }

    public double up_angle(Point3D p, double h) {
        double ans = 0.0;
        ans = Math.atan2(p._z + h - this._z, this.distance2D(p));
        return Math.toDegrees(ans);
    }

    public double r2d_old(double a) {
        return a * 180.0 / 3.141592653589793;
    }

    public static double r2d(double a) {
        return Math.toDegrees(a);
    }

    public static double d2r(double a) {
        return Math.toRadians(a);
    }
}

