package graph;/*
 * Decompiled with CFR 0_132.
 */

import graph.Point3D;
import java.util.ArrayList;

public class Node_Info {
    public static final int WHITE = 0;
    public static final int GRAY = 1;
    public static final int BLACK = 2;
    public static final int ERR = -1;
    public int _color;
    public double _dist;
    public ArrayList<String> _temp_path;
    public int _count_ni;

    public Node_Info() {
        this(null);
    }

    public Node_Info(Point3D p) {
        this.init();
    }

    public void init() {
        this._color = 0;
        this._dist = 0.0;
        this._temp_path = new ArrayList();
        this._count_ni = 0;
    }

    public String toString() {
        String ans = " dist," + this._dist + ", path:";
        int i = 0;
        while (i < this._temp_path.size()) {
            ans = String.valueOf(ans) + "," + this._temp_path.get(i);
            ++i;
        }
        return ans;
    }
}

