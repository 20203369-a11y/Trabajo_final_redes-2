package FINAL_REDES;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphVisualizer extends JFrame {

    private HospitalRouting.Grafo grafo;
    private List<Integer> ruta;

    public GraphVisualizer(HospitalRouting.Grafo grafo, List<Integer> ruta) {
        this.grafo = grafo;
        this.ruta = ruta;

        setTitle("Visualización del Grafo y Ruta");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new GraphPanel());
    }

    class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));

            // Dibujar aristas
            for (int i = 0; i < grafo.nodos.size(); i++) {
                for (HospitalRouting.Edge e : grafo.adj.get(i)) {

                    int x1 = grafo.nodos.get(e.u).x / 5 + 400;
                    int y1 = grafo.nodos.get(e.u).y / 5 + 300;
                    int x2 = grafo.nodos.get(e.v).x / 5 + 400;
                    int y2 = grafo.nodos.get(e.v).y / 5 + 300;

                    g2.setColor(Color.BLACK);
                    g2.drawLine(x1, y1, x2, y2);
                }
            }

          
            if (ruta != null && ruta.size() > 1) {
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(4));

                for (int i = 0; i < ruta.size() - 1; i++) {
                    int u = ruta.get(i);
                    int v = ruta.get(i + 1);

                    int x1 = grafo.nodos.get(u).x / 5 + 400;
                    int y1 = grafo.nodos.get(u).y / 5 + 300;
                    int x2 = grafo.nodos.get(v).x / 5 + 400;
                    int y2 = grafo.nodos.get(v).y / 5 + 300;

                    g2.drawLine(x1, y1, x2, y2);
                }
            }

            
            for (int i = 0; i < grafo.nodos.size(); i++) {
                HospitalRouting.Nodo n = grafo.nodos.get(i);
                int x = n.x / 5 + 400;
                int y = n.y / 5 + 300;

                if (i == 3 || i == 4 || i == 5)
                    g2.setColor(Color.ORANGE); 
                else
                    g2.setColor(Color.CYAN);

                g2.fillOval(x - 10, y - 10, 20, 20);

                g2.setColor(Color.BLACK);
                g2.drawString(n.nombre, x + 12, y - 12);
            }
        }
    }
}
