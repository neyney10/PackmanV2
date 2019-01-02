package graph;/*
 * Decompiled with CFR 0_132.
 */

public class Edge {
    private int _index;
    private double _weight;

    public Edge(int ind, double w) {
        this._index = ind;
        this._weight = w;
    }

    public String toString() {
        return "" + this._index + ":" + this._weight;
    }

    public double getW() {
        return this._weight;
    }

    public int getInd() {
        return this._index;
    }
}

