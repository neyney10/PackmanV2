package graph;/*
 * Decompiled with CFR 0_132.
 */

import graph.Edge;
import graph.Graph;
import graph.Node;
import graph.Node_Info;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

public class Graph_Algo {
    public static double diameter(Graph g) {
        double ans = 0.0;
        int i = 0;
        while (i < g.size()) {
            double dist = Graph_Algo.dijkstra(g, i);
            if (ans < dist) {
                ans = dist;
                System.out.println(String.valueOf(i) + ") " + ans);
            }
            ++i;
        }
        return ans;
    }

    public static double radius(Graph g) {
        double ans = Double.MAX_VALUE;
        int i = 0;
        while (i < g.size()) {
            double dist = Graph_Algo.dijkstra(g, i);
            if (ans > dist) {
                ans = dist;
                System.out.println(String.valueOf(i) + ") " + ans);
            }
            ++i;
        }
        return ans;
    }

    public static double dijkstra(Graph g, String source) {
        double ans = -1.0;
        int ind = g.getNodeIndexByName(source);
        if (ind != -1) {
            ans = Graph_Algo.dijkstra1(g, ind);
        }
        return ans;
    }

    private static double dijkstra(Graph g, int source) {
        Graph_Algo.clearGraphData(g);
        return Graph_Algo.dijkstra1(g, source);
    }

    private static void dijkstra_with_BL(Graph g, int source, ArrayList<Integer> bl) {
        Graph_Algo.clearGraphData(g);
        int i = 0;
        while (i < bl.size()) {
            int ind_bl = bl.get(i);
            Node c = g.getNodeByIndex(ind_bl);
            c.get_info()._color = 2;
            ++i;
        }
        Graph_Algo.dijkstra1(g, source);
    }

    private static double dijkstra1(Graph g, int source) {
        Node src = g.getNodeByIndex(source);
        src.get_info()._color = 1;
        ArrayList<Node> grays = new ArrayList<Node>();
        grays.add(src);
        int non_white = 0;
        int i = 0;
        while (i < g.size()) {
            if (g.getNodeByIndex((int)i).get_info()._color != 0) {
                ++non_white;
            }
            ++i;
        }
        double ans = -1.0;
        while (!grays.isEmpty() && non_white < g.size()) {
            double min = Double.MAX_VALUE;
            Node min_node = null;
            Node source_node = null;
            int min_ind = 0;
            int i2 = 0;
            while (i2 < grays.size()) {
                Node cr = (Node)grays.get(i2);
                double dist_cr = cr.get_info()._dist;
                ArrayList<Edge> ni = cr.get_ni();
                int a = 0;
                while (a < ni.size()) {
                    double d1;
                    Edge e_cr = ni.get(a);
                    Node wi = g.getNodeByIndex(e_cr.getInd());
                    if (wi.get_info()._color == 0 && (d1 = dist_cr + e_cr.getW()) < min) {
                        min_node = wi;
                        min = d1;
                        source_node = cr;
                        min_ind = i2;
                    }
                    ++a;
                }
                ++i2;
            }
            ++non_white;
            min_node.get_info()._color = 1;
            min_node.get_info()._temp_path.addAll(source_node.get_info()._temp_path);
            min_node.get_info()._temp_path.add(source_node.get_name());
            min_node.get_info()._dist = min;
            ans = min;
            grays.add(min_node);
            ++source_node.get_info()._count_ni;
            if (source_node.get_info()._count_ni != source_node.degree()) continue;
            source_node.get_info()._color = 2;
            grays.remove(min_ind);
        }
        return ans;
    }

    public static void clearGraphData(Graph g) {
        g.clear_meta_data();
    }
}

