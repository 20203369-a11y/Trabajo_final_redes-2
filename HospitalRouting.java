package FINAL_REDES;
import java.util.*;

public class HospitalRouting {

    
    static class Nodo {
        String nombre;
        int x, y;
        Nodo(String nombre, int x, int y) { this.nombre = nombre; this.x = x; this.y = y; }
    }

    
    static class Edge {
        int u, v;
        int peso;
        Edge(int u, int v, int peso) { this.u = u; this.v = v; this.peso = peso; }
    }

    
    static class Grafo {
        List<Nodo> nodos;
        List<List<Edge>> adj;
        List<Edge> edges; 

        Grafo() {
            nodos = new ArrayList<>();
            adj = new ArrayList<>();
            edges = new ArrayList<>();
        }

        void addNodo(Nodo n) {
            nodos.add(n);
            adj.add(new ArrayList<>());
        }

        
        void addEdgeBidirectional(int u, int v) {
            int peso = (int) Math.round(distanciaEuclidiana(nodos.get(u).x, nodos.get(u).y,
                                                            nodos.get(v).x, nodos.get(v).y));
            Edge e1 = new Edge(u, v, peso);
            Edge e2 = new Edge(v, u, peso);
            adj.get(u).add(e1);
            adj.get(v).add(e2);
            edges.add(e1);
            edges.add(e2);
        }
    }

    
    static double distanciaEuclidiana(double x1, double y1, double x2, double y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }

    
    static int[] latLonToXY(double lat, double lon) {
        double latRef = -12.08554;
        double lonRef = -76.97097;
        double factor = 100000.0;
        int x = (int) Math.round((lon - lonRef) * factor);
        int y = (int) Math.round((lat - latRef) * factor);
        return new int[]{x, y};
    }

    
    static class DijkstraResult {
        int[] dist;
        int[] pre;
        DijkstraResult(int[] dist, int[] pre) { this.dist = dist; this.pre = pre; }
    }

    static DijkstraResult dijkstra(Grafo g, int origen) {
        int n = g.nodos.size();
        int[] dist = new int[n];
        int[] pre = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(pre, -1);
        dist[origen] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{origen, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            int dcur = cur[1];
            if (dcur != dist[u]) continue;
            for (Edge e : g.adj.get(u)) {
                if (dist[u] + e.peso < dist[e.v]) {
                    dist[e.v] = dist[u] + e.peso;
                    pre[e.v] = u;
                    pq.add(new int[]{e.v, dist[e.v]});
                }
            }
        }
        return new DijkstraResult(dist, pre);
    }

    
    static class BFResult {
        int[] dist;
        int[] pre;
        boolean hasNegativeCycle;
        BFResult(int[] dist, int[] pre, boolean cycle) { this.dist = dist; this.pre = pre; this.hasNegativeCycle = cycle; }
    }

    static BFResult bellmanFord(Grafo g, int origen) {
        int n = g.nodos.size();
        int[] dist = new int[n];
        int[] pre = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(pre, -1);
        dist[origen] = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean updated = false;
            for (Edge e : g.edges) {
                if (dist[e.u] != Integer.MAX_VALUE && dist[e.u] + e.peso < dist[e.v]) {
                    dist[e.v] = dist[e.u] + e.peso;
                    pre[e.v] = e.u;
                    updated = true;
                }
            }
            if (!updated) break;
        }

        boolean negativeCycle = false;
        for (Edge e : g.edges) {
            if (dist[e.u] != Integer.MAX_VALUE && dist[e.u] + e.peso < dist[e.v]) {
                negativeCycle = true;
                break;
            }
        }
        return new BFResult(dist, pre, negativeCycle);
    }

    static List<Integer> reconstruirRuta(int[] pre, int destino) {
        List<Integer> ruta = new ArrayList<>();
        int cur = destino;
        while (cur != -1) {
            ruta.add(cur);
            cur = pre[cur];
        }
        Collections.reverse(ruta);
        return ruta;
    }

    static int mapaAlNodoMasCercano(Grafo g, int ux, int uy) {
        int idx = -1;
        double mejor = Double.MAX_VALUE;
        for (int i = 0; i < g.nodos.size(); i++) {
            Nodo n = g.nodos.get(i);
            double d = distanciaEuclidiana(ux, uy, n.x, n.y);
            if (d < mejor) { mejor = d; idx = i; }
        }
        return idx;
    }

    static List<Integer> kHospitalesMasCercanos(Grafo g, int ux, int uy, Set<Integer> hospitales, int k) {
        List<int[]> arr = new ArrayList<>();
        for (int h : hospitales) {
            Nodo n = g.nodos.get(h);
            double d = distanciaEuclidiana(ux, uy, n.x, n.y);
            arr.add(new int[]{h, (int)Math.round(d)});
        }
        arr.sort(Comparator.comparingInt(a -> a[1]));
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < Math.min(k, arr.size()); i++) out.add(arr.get(i)[0]);
        return out;
    }

    
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Grafo g = new Grafo();
        g.addNodo(new Nodo("Universidad de Lima (ULima)", 0, 0));                
        g.addNodo(new Nodo("Nodo B (Intersección B)", 1000, -200));             
        g.addNodo(new Nodo("Nodo C (Intersección C)", 2000, -500));             
        g.addNodo(new Nodo("Clínica Santa Lucía (Hospital)", -3737, -590));     
        g.addNodo(new Nodo("Instituto Nacional del Niño (Hospital)", -2832, -282));
        g.addNodo(new Nodo("Clínica Internacional (Hospital)", -2795, -1724));  

        g.addEdgeBidirectional(0, 1);
        g.addEdgeBidirectional(0, 2);
        g.addEdgeBidirectional(1, 2);
        g.addEdgeBidirectional(0, 3);
        g.addEdgeBidirectional(1, 4);
        g.addEdgeBidirectional(2, 5);
        g.addEdgeBidirectional(3, 4);
        g.addEdgeBidirectional(4, 5);

        Set<Integer> hospitales = new HashSet<>(Arrays.asList(3, 4, 5));

        System.out.println("====== Sistema de rutas (ODS 3) ======");
        System.out.println("Elija cómo ingresar su ubicación:");
        System.out.println("1 - Ingresar X,Y");
        System.out.println("2 - Ingresar lat/lon");
        System.out.print("Opción: ");
        int modo = sc.nextInt();

        int ux = 0, uy = 0;
        if (modo == 1) {
            System.out.print("Ingrese X: ");
            ux = sc.nextInt();
            System.out.print("Ingrese Y: ");
            uy = sc.nextInt();
        } else {
            System.out.print("Ingrese latitud: ");
            double lat = sc.nextDouble();
            System.out.print("Ingrese longitud: ");
            double lon = sc.nextDouble();
            int[] xy = latLonToXY(lat, lon);
            ux = xy[0]; uy = xy[1];
        }

        List<Integer> top3 = kHospitalesMasCercanos(g, ux, uy, hospitales, 3);
        System.out.println("\nLos 3 hospitales más cercanos:");
        for (int i = 0; i < top3.size(); i++) {
            Nodo n = g.nodos.get(top3.get(i));
            double d = distanciaEuclidiana(ux, uy, n.x, n.y);
            System.out.printf("%d) %s (≈ %.2f)\n", i+1, n.nombre, d);
        }

        System.out.print("\nElige hospital (1-3): ");
        int elegido = sc.nextInt();
        int idxHospital = top3.get(elegido-1);

        int origen = mapaAlNodoMasCercano(g, ux, uy);
        System.out.println("Tu nodo origen será: " + g.nodos.get(origen).nombre);

        System.out.println("\nAlgoritmo:");
        System.out.println("1 - Dijkstra");
        System.out.println("2 - Bellman-Ford");
        System.out.print("Opción: ");
        int alg = sc.nextInt();

        if (alg == 1) {
            DijkstraResult res = dijkstra(g, origen);
            List<Integer> ruta = reconstruirRuta(res.pre, idxHospital);
            int dist = res.dist[idxHospital];

            System.out.println("\n→ Ruta calculada con DIJKSTRA:");
            for (int id : ruta)
                System.out.print(" -> " + g.nodos.get(id).nombre);
            System.out.println("\nDistancia total: " + dist);

            // <<< AQUI SE ABRE LA VENTANA GRÁFICA >>>
            new GraphVisualizer(g, ruta).setVisible(true);

        } else {
            BFResult res = bellmanFord(g, origen);
            List<Integer> ruta = reconstruirRuta(res.pre, idxHospital);
            int dist = res.dist[idxHospital];

            System.out.println("\n→ Ruta calculada con BELLMAN-FORD:");
            for (int id : ruta)
                System.out.print(" -> " + g.nodos.get(id).nombre);
            System.out.println("\nDistancia total: " + dist);

            
            new GraphVisualizer(g, ruta).setVisible(true);
        }

        sc.close();
    }
}

