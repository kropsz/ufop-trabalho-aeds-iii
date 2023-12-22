package entities;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.*;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import javax.swing.JLabel;


public class GrafoDePixels {
    private DefaultUndirectedGraph<Point2D, DefaultEdge> grafo;
    private BufferedImage image;

    public GrafoDePixels(BufferedImage image) {
        this.grafo = new DefaultUndirectedGraph<>(DefaultEdge.class);
        this.image = image;
    }

    public void adicionarPixel(int x, int y) {
        Point2D coordenadaPixel = new Point2D.Double(x, y);
        grafo.addVertex(coordenadaPixel);
    }

    public void adicionarConexao(int x1, int y1, int x2, int y2) {
        Point2D coordenadaPixel1 = new Point2D.Double(x1, y1);
        Point2D coordenadaPixel2 = new Point2D.Double(x2, y2);
        grafo.addEdge(coordenadaPixel1, coordenadaPixel2);
    }

    public void adicionarArestasVizinhas() {
        int largura = image.getWidth();
        int altura = image.getHeight();
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                if (x < largura - 1 && !isPixelPreto(x, y) && !isPixelPreto(x + 1, y)) {
                    adicionarConexao(x, y, x + 1, y);
                }
                if (y < altura - 1 && !isPixelPreto(x, y) && !isPixelPreto(x, y + 1)) {
                    adicionarConexao(x, y, x, y + 1);
                }
            }
        }
    }
    
    public Point2D buscarNoPorCor(Color cor) {
        Set<Point2D> nosCorrespondentes = new HashSet<>();
        Set<Point2D> vertices = grafo.vertexSet();

        for (Point2D ponto : vertices) {
            int x = (int) ponto.getX();
            int y = (int) ponto.getY();
            int rgb = image.getRGB(x, y);
            Color pixelColor = new Color(rgb);

            if (pixelColor.equals(cor)) {
                nosCorrespondentes.add(ponto);
            }
        }

        if (!nosCorrespondentes.isEmpty()) {
            return nosCorrespondentes.iterator().next();
        } else {
            return null;
        }
    }

    public List<Point2D> buscaEmLargura(Point2D pontoInicial, Point2D pontoDestino) {
        List<Point2D> caminho = new ArrayList<>();
        Queue<Point2D> fila = new LinkedList<>();
        Map<Point2D, Point2D> predecessores = new HashMap<>();
        Set<Point2D> visitados = new HashSet<>();

        fila.add(pontoInicial);
        visitados.add(pontoInicial);

        while (!fila.isEmpty()) {
            Point2D atual = fila.poll();
            if (atual.equals(pontoDestino)) {
                Point2D ponto = pontoDestino;
                while (ponto != null) {
                    caminho.add(ponto);
                    ponto = predecessores.get(ponto);
                }
                Collections.reverse(caminho);
                return caminho;
            }

            Set<Point2D> vizinhos = obterVizinhos(atual);
            for (Point2D vizinho : vizinhos) {
                if (!visitados.contains(vizinho)) {
                    fila.add(vizinho);
                    visitados.add(vizinho);
                    predecessores.put(vizinho, atual);
                }
            }
        }

        return caminho;
    }

    private Set<Point2D> obterVizinhos(Point2D ponto) {
        Set<Point2D> vizinhos = new HashSet<>();
        int x = (int) ponto.getX();
        int y = (int) ponto.getY();
        int largura = image.getWidth();
        int altura = image.getHeight();

        if (x > 0) {
            Point2D vizinhoEsquerda = new Point2D.Double(x - 1, y);
            if (!isPixelPreto((int)vizinhoEsquerda.getX(), (int)vizinhoEsquerda.getY())) {
                vizinhos.add(vizinhoEsquerda);
            }
        }
        if (x < largura - 1) {
            Point2D vizinhoDireita = new Point2D.Double(x + 1, y);
            if (!isPixelPreto((int)vizinhoDireita.getX(), (int)vizinhoDireita.getY())) {
                vizinhos.add(vizinhoDireita);
            }
        }
        if (y > 0) {
            Point2D vizinhoCima = new Point2D.Double(x, y - 1);
            if (!isPixelPreto((int)vizinhoCima.getX(), (int)vizinhoCima.getY())) {
                vizinhos.add(vizinhoCima);
            }
        }
        if (y < altura - 1) {
            Point2D vizinhoBaixo = new Point2D.Double(x, y + 1);
            if (!isPixelPreto((int)vizinhoBaixo.getX(), (int)vizinhoBaixo.getY())) {
                vizinhos.add(vizinhoBaixo);
            }
        }

        return vizinhos;
    }

    private boolean isPixelPreto(int x, int y) {
        int rgb = image.getRGB(x, y);
        Color cor = new Color(rgb);
        return cor.equals(Color.BLACK);
    }

    public void exibirSequenciaDePassos(List<Point2D> caminho, JLabel label) {
        StringBuilder sequencia = new StringBuilder();
        for (int i = 0; i < caminho.size() - 1; i++) {
            Point2D atual = caminho.get(i);
            Point2D proximo = caminho.get(i + 1);
            if (proximo.getX() > atual.getX()) {
                sequencia.append("→ "); // Setas para a direita
            } else if (proximo.getX() < atual.getX()) {
                sequencia.append("← "); // Setas para a esquerda
            } else if (proximo.getY() > atual.getY()) {
                sequencia.append("↓ "); // Setas para baixo
            } else if (proximo.getY() < atual.getY()) {
                sequencia.append("↑ "); // Setas para cima
            }
        }
        label.setText(sequencia.toString());
        label.setFont(new Font("Arial", Font.PLAIN, 20));
    }
    
    
    
}
