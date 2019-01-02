package graph;/*
 * Decompiled with CFR 0_132.
 */

import graph.Edge;
import graph.Node;
import graph.Node_Info;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Graph {
    public static final double EPS = 1.0E-6;
    private ArrayList<Node> _nodes = new ArrayList();
    private Random _rand;
    private int _edge_count = 0;

    public boolean add(Node d) {
        boolean ans = false;
        if (!this.exist(d)) {
            this._nodes.add(d);
            ans = true;
        }
        return ans;
    }

    public int size() {
        return this._nodes.size();
    }

    public String toString() {
        String ans = "";
        ans = String.valueOf(ans) + this.size() + "\n" + this._edge_count;
        int i = 0;
        while (i < this.size()) {
            Node cr = this._nodes.get(i);
            ans = String.valueOf(ans) + cr + "\n";
            ++i;
        }
        return ans;
    }

    public void toFile() {
        long t = new Date().getTime();
        String name = "Graph_" + this.size() + "_" + this._edge_count + "_" + t;
        this.toFile(name);
    }

    public void toFile(String f) {
        try {
            Node cr;
            FileWriter fw = new FileWriter(f);
            PrintWriter os = new PrintWriter(fw);
            os.print(String.valueOf(this.size()) + "\n");
            int i = 0;
            while (i < this.size()) {
                cr = this._nodes.get(i);
                os.print(String.valueOf(i) + " " + cr.get_info() + "\n");
                ++i;
            }
            os.print(this._edge_count);
            i = 0;
            while (i < this.size()) {
                cr = this._nodes.get(i);
                ArrayList<Edge> ni = cr.get_ni();
                int a = 0;
                while (a < ni.size()) {
                    int index = ni.get(a).getInd();
                    double w = ni.get(a).getW();
                    if (index > i) {
                        Node ot = this._nodes.get(index);
                        os.print("\n" + i + " " + index + " " + w);
                    }
                    ++a;
                }
                ++i;
            }
            os.close();
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Err: unable to write Graph file named: " + f);
        }
    }

    public void clear_meta_data() {
        int i = 0;
        while (i < this.size()) {
            this._nodes.get(i).get_info().init();
            ++i;
        }
    }

    public Node getNodeByIndex(int i) {
        Node ans = null;
        if (i >= 0 && i < this.size()) {
            ans = this._nodes.get(i);
        }
        return ans;
    }

    public boolean exist(Node d) {
        if (this.getNodeByName(d.get_name()) != null) {
            return true;
        }
        return false;
    }

    public int getNodeIndexByName(String s) {
        int ans = -1;
        int i = 0;
        while (ans == -1 && i < this.size()) {
            Node cr = this._nodes.get(i);
            String name = cr.get_name();
            if (s.equals(name)) {
                ans = i;
            }
            ++i;
        }
        return ans;
    }

    public Node getNodeByName(String s) {
        Node ans = null;
        int ind = this.getNodeIndexByName(s);
        if (ind != -1) {
            ans = this._nodes.get(ind);
        }
        return ans;
    }

    public void addEdge(String a, String b, double w) {
        Node va = this.getNodeByName(a);
        Node vb = this.getNodeByName(b);
        if (va != null && vb != null) {
            va.add(vb, w);
            vb.add(va, w);
        }
    }
}

