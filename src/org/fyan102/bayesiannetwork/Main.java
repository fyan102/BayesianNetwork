package org.fyan102.bayesiannetwork;

import javax.swing.SwingUtilities;
import org.fyan102.bayesiannetwork.ui.Window;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            window.setVisible(true);
        });
    }
}
