package FINAL_REDES;

import java.util.*;

public class Grafo {
    public List<Nodo> nodos = new ArrayList<>();
    public List<List<Edge>> adj = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();

    public void addNodo(Nodo n) {
        nodos.add(n);
        adj.add(new ArrayList<>());
    }

    public void addEdgeBidirectional(int u, int v) {
        int peso = (int)Math.round(Math.hypot(
                nodos.get(u).x - nodos.get(v).x,
                nodos.get(u).y - nodos.get(v).y
        ));
        Edge e1 = new Edge(u, v, peso);
        Edge e2 = new Edge(v, u, peso);
        adj.get(u).add(e1);
        adj.get(v).add(e2);
        edges.add(e1);
        edges.add(e2);
    }
}
