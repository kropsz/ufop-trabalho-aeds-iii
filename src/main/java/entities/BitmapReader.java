package entities;

import javax.swing.*;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class BitmapReader extends JFrame {
    private JTextField pathField;
    private JButton loadButton;
    private JButton showPathButton;
    private BufferedImage image;
    private JLabel pathLabel;


    public BitmapReader() {
        setTitle("Bitmap Reader");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        pathField = new JTextField(20);
        loadButton = new JButton("Carregar Imagem");
        showPathButton = new JButton("Mostrar Caminho");
        pathLabel = new JLabel();

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File(pathField.getText());
                    image = ImageIO.read(file);
                    if (image == null) {
                        JOptionPane.showMessageDialog(BitmapReader.this, "Erro ao carregar a imagem.");
                    } else {
                        JOptionPane.showMessageDialog(BitmapReader.this, "Imagem carregada com sucesso");
                        showPathButton.setEnabled(true);
                    }
                } catch (IOException ex) {  
                    JOptionPane.showMessageDialog(BitmapReader.this, "Erro ao carregar a imagem: " + ex.getMessage());
                }
            }
        });

        showPathButton.setEnabled(false);
        showPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                grafo.exibirSequenciaDePassos(caminho, pathLabel);
            }
        });

        panel.add(new JLabel("Caminho da Imagem:"));
        panel.add(pathField);
        panel.add(loadButton);
        panel.add(showPathButton);
        panel.add(pathLabel);

        add(panel);
    }

    public static void main(String[] args) {
        BitmapReader reader = new BitmapReader();
        reader.setVisible(true);
    }
}

