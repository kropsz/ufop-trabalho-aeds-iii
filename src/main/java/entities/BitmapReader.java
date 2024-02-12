package entities;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class BitmapReader extends JFrame {
    private JTextField pathField;
    private JButton loadButton;
    private JButton showPathButton;
    private BufferedImage[] imagens;
    private JLabel pathLabel;

    public BitmapReader() {
        setTitle("Bitmap Reader");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        pathField = new JTextField(20);
        loadButton = new JButton("Carregar Pasta com Imagens");
        showPathButton = new JButton("Mostrar Caminho nas Imagens");
        pathLabel = new JLabel();

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File dir = new File(pathField.getText());
                    File[] files = dir.listFiles((d, name) -> name.endsWith(".bmp"));
                    if (files == null || files.length == 0) {
                        JOptionPane.showMessageDialog(BitmapReader.this, "Erro ao carregar as imagens.");
                    } else {
                        Arrays.sort(files);
                        imagens = new BufferedImage[files.length];
                        for (int i = 0; i < files.length; i++) {
                            imagens[i] = ImageIO.read(files[i]);
                        }
                        JOptionPane.showMessageDialog(BitmapReader.this, "Imagens carregadas com sucesso");
                        showPathButton.setEnabled(true);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(BitmapReader.this, "Erro ao carregar as imagens: " + ex.getMessage());
                }
            }
        });

        showPathButton.setEnabled(false);
        showPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GrafoDePixels grafo = new GrafoDePixels(imagens);

                for (int z = 0; z < imagens.length; z++) {
                    for (int y = 0; y < imagens[z].getHeight(); y++) {
                        for (int x = 0; x < imagens[z].getWidth(); x++) {
                            grafo.adicionarPixel(x, y, z);
                        }
                    }
                }
                grafo.adicionarArestasVizinhas();

                Point3D noVermelho = grafo.buscarNoPorCor(Color.RED);
                List<Point3D> nosVerdes = grafo.buscarNosPorCor(Color.GREEN);

                List<Point3D> caminhoMaisCurto = null;
                for (Point3D noVerde : nosVerdes) {
                    List<Point3D> caminho = grafo.dijkstra(noVermelho, noVerde);
                    grafo.exibirSequenciaDePassos(caminhoMaisCurto, pathLabel);
                    if (caminhoMaisCurto == null || caminho.size() < caminhoMaisCurto.size()) {
                        caminhoMaisCurto = caminho;
                    }
                }

                if (caminhoMaisCurto != null) {
                    BufferedImage[] imagensComCaminho = new BufferedImage[imagens.length];
                    for (int i = 0; i < imagens.length; i++) {
                        imagensComCaminho[i] = new BufferedImage(imagens[i].getWidth(), imagens[i].getHeight(),
                                imagens[i].getType());
                        Graphics g = imagensComCaminho[i].getGraphics();
                        g.drawImage(imagens[i], 0, 0, null);
                        g.dispose();
                    }
                    Collections.sort(caminhoMaisCurto, Comparator.comparingDouble(Point3D::getZ));

                    desenharCaminhoNaImagem(caminhoMaisCurto, imagensComCaminho);
                    exibirImagens(imagensComCaminho);
                }
            }
        });
        panel.add(new JLabel("Caminho da Imagem:"));
        panel.add(pathField);
        panel.add(loadButton);
        panel.add(showPathButton);
        panel.add(pathLabel);

        add(panel);
    }

    private void desenharCaminhoNaImagem(List<Point3D> caminho, BufferedImage[] imagens) {
        Map<Double, Integer> mapeamento = criarMapeamentoZParaIndice(caminho);
        Color corDoCaminho = Color.YELLOW;
        for (Point3D ponto : caminho) {
            int indice = mapeamento.get(ponto.getZ());
            imagens[indice].setRGB((int) ponto.getX(), (int) ponto.getY(), corDoCaminho.getRGB());
        }
    }

    private void exibirImagens(BufferedImage[] imagens) {
        int deslocamento = 0;
        for (int i = 0; i < imagens.length; i++) {
            BufferedImage imagemRedimensionada = redimensionarImagem(imagens[i], 800, 600); 
            ImageIcon icon = new ImageIcon(imagemRedimensionada);
            JLabel label = new JLabel(icon);
            JFrame frame = new JFrame("Andar " + i);
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.pack();
            frame.setLocation(deslocamento, deslocamento);
            frame.setVisible(true);
            deslocamento += 50; 
        }
    }

    private BufferedImage redimensionarImagem(BufferedImage imagemOriginal, int novaLargura, int novaAltura) {
        Image imagemTemporaria = imagemOriginal.getScaledInstance(novaLargura, novaAltura, Image.SCALE_DEFAULT);
        BufferedImage imagemRedimensionada = new BufferedImage(novaLargura, novaAltura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagemRedimensionada.createGraphics();
        g2d.drawImage(imagemTemporaria, 0, 0, null);
        g2d.dispose();
        return imagemRedimensionada;
    }

    private Map<Double, Integer> criarMapeamentoZParaIndice(List<Point3D> caminho) {
        Map<Double, Integer> mapeamento = new HashMap<>();
        double zMin = caminho.get(0).getZ();
        double zMax = caminho.get(0).getZ();
        for (Point3D ponto : caminho) {
            if (ponto.getZ() < zMin) {
                zMin = ponto.getZ();
            }
            if (ponto.getZ() > zMax) {
                zMax = ponto.getZ();
            }
        }
        double intervalo = zMax - zMin;
        for (int i = 0; i < caminho.size(); i++) {
            double zNormalizado = (caminho.get(i).getZ() - zMin) / intervalo;
            mapeamento.put(caminho.get(i).getZ(), (int) (zNormalizado * (imagens.length - 1)));
        }
        return mapeamento;
    }

}
