package org.conversor;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.*;
import java.util.Objects;


public class JanelaPrincipal extends JFrame {
    JanelaPrincipal() {
        this.setTitle("Conversor");

        ClassLoader classLoader = JanelaPrincipal.class.getClassLoader();
        ImageIcon icone = new ImageIcon(Objects.requireNonNull(classLoader.getResource("funil-Maan-icons.png")));
        this.setIconImage(icone.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

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