package org.conversor;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;


public class AbasConversor extends JTabbedPane {

    AbasConversor() {
        this.setBounds(0, 0, 320, 456);
        this.setTabPlacement(JTabbedPane.BOTTOM);
        //this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.putClientProperty("JTabbedPane.showTabSeparators", false);
        this.putClientProperty("JTabbedPane.tabHeight", 62);
        this.putClientProperty("JTabbedPane.tabIconPlacement", SwingConstants.TOP);
        this.setVisible(true);
    }
}