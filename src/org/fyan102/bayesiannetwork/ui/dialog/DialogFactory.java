package org.fyan102.bayesiannetwork.ui.dialog;

import org.fyan102.bayesiannetwork.ui.config.UIConfig;

import javax.swing.*;
import java.awt.*;

public final class DialogFactory {
    private DialogFactory() {
        // Prevent instantiation
    }

    public static JDialog createDialog(JFrame parent, String title, boolean modal) {
        JDialog dialog = new JDialog(parent, title, modal);
        dialog.getContentPane().setBackground(UIConfig.BACKGROUND_COLOR);
        dialog.setLayout(new BorderLayout());
        return dialog;
    }

    public static JButton createCloseButton(JDialog dialog) {
        JButton closeButton = new JButton("Close");
        closeButton.setFont(UIConfig.BUTTON_FONT);
        closeButton.setBackground(UIConfig.BUTTON_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());
        return closeButton;
    }

    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(UIConfig.BACKGROUND_COLOR);
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        return buttonPanel;
    }

    public static JTextArea createHelpText(String text) {
        JTextArea helpText = new JTextArea(text);
        helpText.setEditable(false);
        helpText.setBackground(UIConfig.BACKGROUND_COLOR);
        helpText.setFont(UIConfig.MENU_FONT);
        helpText.setWrapStyleWord(true);
        helpText.setLineWrap(true);
        return helpText;
    }

    public static JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    public static void showDialog(JDialog dialog, int width, int height) {
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(dialog.getParent());
        dialog.setVisible(true);
    }
} 