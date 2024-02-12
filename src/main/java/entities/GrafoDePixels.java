package entities;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import javax.swing.JLabel;

public class GrafoDePixels {
    private SimpleWeightedGraph<Point3D, DefaultWeightedEdge> grafo;
    private BufferedImage[] imagens;

    public GrafoDePixels(BufferedImage[] imagens) {
        this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.imagens = imagens;
    }

    public void adicionarPixel(int x, int y, int z) {
        Point3D coordenadaPixel = new Point3D(x, y, z);
        grafo.addVertex(coordenadaPixel);
    }

    public void adicionarConexao(Point3D ponto1, Point3D ponto2, double peso) {
        DefaultWeightedEdge edge = grafo.addEdge(ponto1, ponto2);
        grafo.setEdgeWeight(edge, peso);
    }

    public void adicionarArestasVizinhas() {
        int largura = imagens[0].getWidth();
        int altura = imagens[0].getHeight();
        int profundidade = imagens.length;
        for (int z = 0; z < profundidade; z++) {
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    Point3D pontoAtual = new Point3D(x, y, z);
                    if (x < largura - 1) {
                        Point3D pontoVizinho = new Point3D(x + 1, y, z);
                        if (!isPixelObstaculo(pontoAtual) && !isPixelObstaculo(pontoVizinho)) {
                            adicionarConexao(pontoAtual, pontoVizinho, calcularPeso(pontoAtual, pontoVizinho));
                        }
                    }
                    if (y < altura - 1) {
                        Point3D pontoVizinho = new Point3D(x, y + 1, z);
                        if (!isPixelObstaculo(pontoAtual) && !isPixelObstaculo(pontoVizinho)) {
                            adicionarConexao(pontoAtual, pontoVizinho, calcularPeso(pontoAtual, pontoVizinho));
                        }
                    }
                    if (z < profundidade - 1) {
                        Point3D pontoVizinho = new Point3D(x, y, z + 1);
                        if (!isPixelObstaculo(pontoAtual) && !isPixelObstaculo(pontoVizinho)) {
                            adicionarConexao(pontoAtual, pontoVizinho, calcularPeso(pontoAtual, pontoVizinho));
                        }
                    }
                }
            }
        }
    }

    private double calcularPeso(Point3D ponto1, Point3D ponto2) {
        int largura = imagens[0].getWidth();
        int altura = imagens[0].getHeight();
        int profundidade = imagens.length;
        double peso = 1.0;

        for (int k = Math.max(0, (int) ponto1.getZ() - 1); k <= Math.min(profundidade - 1,
                (int) ponto1.getZ() + 1); k++) {
            for (int i = Math.max(0, (int) ponto1.getX() - 1); i <= Math.min(largura - 1,
                    (int) ponto1.getX() + 1); i++) {
                for (int j = Math.max(0, (int) ponto1.getY() - 1); j <= Math.min(altura - 1,
                        (int) ponto1.getY() + 1); j++) {
                    Point3D ponto = new Point3D(i, j, k);
                    if (isPixelPreto(ponto)) {
                        peso += 1000.0;
                    } else if (isPixelCinzaEscuro(ponto)) {
                        peso += 500.0;
                    } else if (isPixelCinzaClaro(ponto)) {
                        peso += 200.0;
                    }
                }
            }
        }

        for (int k = Math.max(0, (int) ponto2.getZ() - 1); k <= Math.min(profundidade - 1,
                (int) ponto2.getZ() + 1); k++) {
            for (int i = Math.max(0, (int) ponto2.getX() - 1); i <= Math.min(largura - 1,
                    (int) ponto2.getX() + 1); i++) {
                for (int j = Math.max(0, (int) ponto2.getY() - 1); j <= Math.min(altura - 1,
                        (int) ponto2.getY() + 1); j++) {
                    Point3D ponto = new Point3D(i, j, k);
                    if (isPixelPreto(ponto)) {
                        peso += 1000.0;
                    } else if (isPixelCinzaEscuro(ponto)) {
                        peso += 500.0;
                    } else if (isPixelCinzaClaro(ponto)) {
                        peso += 200.0;
                    }
                }
            }
        }

        return peso;
    }

    public List<Point3D> buscarNosPorCor(Color cor) {
        List<Point3D> nosCorrespondentes = new ArrayList<>();
        Set<Point3D> vertices = grafo.vertexSet();

        for (Point3D ponto : vertices) {
            int x = (int) ponto.getX();
            int y = (int) ponto.getY();
            int z = (int) ponto.getZ();
            int rgb = imagens[z].getRGB(x, y);
            Color pixelColor = new Color(rgb);

            if (pixelColor.equals(cor)) {
                nosCorrespondentes.add(ponto);
            }
        }

        return nosCorrespondentes;
    }

    public Point3D buscarNoPorCor(Color cor) {
        Set<Point3D> vertices = grafo.vertexSet();

        for (Point3D ponto : vertices) {
            int x = (int) ponto.getX();
            int y = (int) ponto.getY();
            int z = (int) ponto.getZ();
            int rgb = imagens[z].getRGB(x, y);
            Color pixelColor = new Color(rgb);

            if (pixelColor.equals(cor)) {
                return ponto;
            }
        }

        return null;
    }

    public List<Point3D> dijkstra(Point3D pontoInicial, Point3D pontoDestino) {
        Map<Point3D, Double> distancias = new HashMap<>();
        Map<Point3D, Point3D> predecessores = new HashMap<>();
        PriorityQueue<Point3D> fila = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        for (Point3D ponto : todosOsPontos()) {
            distancias.put(ponto, Double.POSITIVE_INFINITY);
        }
        distancias.put(pontoInicial, 0.0);

        fila.add(pontoInicial);

        while (!fila.isEmpty()) {
            Point3D atual = fila.poll();

            if (atual.equals(pontoDestino)) {
                List<Point3D> caminho = new ArrayList<>();
                Point3D ponto = pontoDestino;
                while (ponto != null) {
                    caminho.add(ponto);
                    ponto = predecessores.get(ponto);
                }
                Collections.reverse(caminho);
                return caminho;
            }

            Set<Point3D> vizinhos = obterVizinhos(atual);
            for (Point3D vizinho : vizinhos) {
                double peso = calcularPeso(atual, vizinho);
                double distanciaAlternativa = distancias.get(atual) + peso;
                if (distanciaAlternativa < distancias.get(vizinho)) {
                    fila.remove(vizinho);
                    distancias.put(vizinho, distanciaAlternativa);
                    predecessores.put(vizinho, atual);
                    fila.add(vizinho);
                }
            }
        }

        return null;
    }

    private Set<Point3D> todosOsPontos() {
        Set<Point3D> pontos = new HashSet<>();
        int largura = imagens[0].getWidth();
        int altura = imagens[0].getHeight();
        int profundidade = imagens.length;

        for (int z = 0; z < profundidade; z++) {
            for (int x = 0; x < largura; x++) {
                for (int y = 0; y < altura; y++) {
                    pontos.add(new Point3D(x, y, z));
                }
            }
        }

        return pontos;
    }

    private Set<Point3D> obterVizinhos(Point3D ponto) {
        Set<Point3D> vizinhos = new HashSet<>();
        int x = (int) ponto.getX();
        int y = (int) ponto.getY();
        int z = (int) ponto.getZ();
        int largura = imagens[0].getWidth();
        int altura = imagens[0].getHeight();
        int profundidade = imagens.length;

        if (x > 0) {
            Point3D vizinhoEsquerda = new Point3D(x - 1, y, z);
            if (!isPixelObstaculo(vizinhoEsquerda)) {
                vizinhos.add(vizinhoEsquerda);
            }
        }
        if (x < largura - 1) {
            Point3D vizinhoDireita = new Point3D(x + 1, y, z);
            if (!isPixelObstaculo(vizinhoDireita)) {
                vizinhos.add(vizinhoDireita);
            }
        }
        if (y > 0) {
            Point3D vizinhoCima = new Point3D(x, y - 1, z);
            if (!isPixelObstaculo(vizinhoCima)) {
                vizinhos.add(vizinhoCima);
            }
        }
        if (y < altura - 1) {
            Point3D vizinhoBaixo = new Point3D(x, y + 1, z);
            if (!isPixelObstaculo(vizinhoBaixo)) {
                vizinhos.add(vizinhoBaixo);
            }
        }
        if (z > 0) {
            Point3D vizinhoFrente = new Point3D(x, y, z - 1);
            if (!isPixelObstaculo(vizinhoFrente)) {
                vizinhos.add(vizinhoFrente);
            }
        }
        if (z < profundidade - 1) {
            Point3D vizinhoAtras = new Point3D(x, y, z + 1);
            if (!isPixelObstaculo(vizinhoAtras)) {
                vizinhos.add(vizinhoAtras);
            }
        }

        return vizinhos;
    }

    private boolean isPixelObstaculo(Point3D ponto) {
        int x = (int) ponto.getX();
        int y = (int) ponto.getY();
        int z = (int) ponto.getZ();
        int rgb = imagens[z].getRGB(x, y);
        Color cor = new Color(rgb);
        return cor.equals(Color.BLACK) || cor.equals(Color.DARK_GRAY) || cor.equals(Color.GRAY);
    }

    private boolean isPixelPreto(Point3D ponto) {
        int x = (int) ponto.getX();
        int y = (int) ponto.getY();
        int z = (int) ponto.getZ();
        int rgb = imagens[z].getRGB(x, y);
        Color cor = new Color(rgb);
        return cor.equals(Color.BLACK);
    }

    private boolean isPixelCinzaEscuro(Point3D ponto) {
        int x = (int) ponto.getX();
        int y = (int) ponto.getY();
        int z = (int) ponto.getZ();
        int rgb = imagens[z].getRGB(x, y);
        Color cor = new Color(rgb);
        return cor.equals(Color.DARK_GRAY);
    }

    private boolean isPixelCinzaClaro(Point3D ponto) {
        int x = (int) ponto.getX();
        int y = (int) ponto.getY();
        int z = (int) ponto.getZ();
        int rgb = imagens[z].getRGB(x, y);
        Color cor = new Color(rgb);
        return cor.equals(Color.GRAY);
    }

    public void exibirSequenciaDePassos(List<Point3D> caminho, JLabel label) {
        StringBuilder sequencia = new StringBuilder();
        for (int i = 0; i < caminho.size() - 1; i++) {
            Point3D atual = caminho.get(i);
            Point3D proximo = caminho.get(i + 1);
            if (proximo.getX() > atual.getX()) {
                sequencia.append("→ ");
            } else if (proximo.getX() < atual.getX()) {
                sequencia.append("← "); 
            } else if (proximo.getY() > atual.getY()) {
                sequencia.append("↓ "); 
            } else if (proximo.getY() < atual.getY()) {
                sequencia.append("↑ "); 
            } else if (proximo.getZ() > atual.getZ()) {
                sequencia.append("⇡ "); 
            } else if (proximo.getZ() < atual.getZ()) {
                sequencia.append("⇣ "); 
            }
        }
        label.setText(sequencia.toString());
        label.setFont(new Font("Arial", Font.PLAIN, 20));
    }

}
