package org.conversor;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FlatOneDarkIJTheme.setup();
            new MainFrame();
        });
    }
}