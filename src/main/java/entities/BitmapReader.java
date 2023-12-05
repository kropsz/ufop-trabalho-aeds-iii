package entities;

import java.awt.Color;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class BitmapReader {
    public static void main(String[] args) {

        try {
            File file = new File("src/main/resources/assets/toy_map.bmp");
            BufferedImage image = ImageIO.read(file);

            if (image == null) {
                System.out.println("Erro ao carregar a imagem.");
            } else {
                System.out.println("Imagem carregada com sucesso. Largura: " + image.getWidth() + ", Altura: "
                        + image.getHeight());

                GrafoDePixels grafo = new GrafoDePixels(image);

                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        grafo.adicionarPixel(x, y);
                    }
                }
                grafo.adicionarArestasVizinhas();

                Point2D noVermelho = grafo.buscarNoPorCor(Color.RED);
                Point2D noVerde = grafo.buscarNoPorCor(Color.GREEN);
                List<Point2D> caminho = grafo.buscaEmLargura(noVermelho, noVerde);
                grafo.exibirSequenciaDePassos(caminho);

            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }
}
