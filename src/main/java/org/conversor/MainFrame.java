package org.conversor;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.util.Objects;


public class MainFrame extends JFrame {
    MainFrame() {
        this.setTitle("Conversor");

        ClassLoader classLoader = MainFrame.class.getClassLoader();
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("funil-Maan-icons.png")));
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(null);
        this.setSize(320, 493); //sets x-dimension and y-dimension
        this.setResizable(false);

        AbasConversor abas = new AbasConversor();

        abas.addTab("Moeda", new ConversorDeMoeda());
        abas.addTab("Temperatura", new ConversorDeTemperatura());
        abas.addTab("Distancia", new ConversorDeDistancia());

        this.add(abas);
        this.setVisible(true);
    }
}