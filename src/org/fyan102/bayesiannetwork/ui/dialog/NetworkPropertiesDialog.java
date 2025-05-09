package org.fyan102.bayesiannetwork.ui.dialog;

import org.fyan102.bayesiannetwork.model.Network;
import org.fyan102.bayesiannetwork.ui.config.UIConfig;

import javax.swing.*;
import java.awt.*;

public class NetworkPropertiesDialog {
    private final JDialog dialog;
    private final JTextField nameField;
    private final JTextArea descriptionArea;
    private Network network;

    public NetworkPropertiesDialog(JFrame parent) {
        dialog = DialogFactory.createDialog(parent, "Network Properties", true);
        
        // Create components
        nameField = new JTextField(20);
        nameField.setFont(UIConfig.MENU_FONT);
        
        descriptionArea = DialogFactory.createHelpText("");
        descriptionArea.setRows(5);
        
        // Create panels
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(UIConfig.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add components to input panel
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(DialogFactory.createScrollPane(descriptionArea), gbc);
        
        // Create buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = DialogFactory.createCloseButton(dialog);
        
        // Style buttons
        saveButton.setFont(UIConfig.BUTTON_FONT);
        saveButton.setBackground(UIConfig.BUTTON_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        
        // Add button actions
        saveButton.addActionListener(e -> {
            if (network != null) {
                network.setName(nameField.getText());
                network.setDescription(descriptionArea.getText());
                dialog.dispose();
            }
        });
        
        // Create button panel
        JPanel buttonPanel = DialogFactory.createButtonPanel(saveButton, cancelButton);
        
        // Add panels to dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void show(Network network) {
        this.network = network;
        nameField.setText(network.getName());
        descriptionArea.setText(network.getDescription());
        DialogFactory.showDialog(dialog, 400, 300);
    }
} 