package org.fyan102.bayesiannetwork.ui.dialog;

import org.fyan102.bayesiannetwork.model.Node;
import org.fyan102.bayesiannetwork.ui.config.UIConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NodePropertiesDialog {
    private final JDialog dialog;
    private final JTextField nameField;
    private final JTextArea descriptionArea;
    private final JList<String> statesList;
    private final DefaultListModel<String> statesModel;
    private Node node;

    public NodePropertiesDialog(JFrame parent) {
        dialog = DialogFactory.createDialog(parent, "Node Properties", true);
        
        // Create components
        nameField = new JTextField(20);
        nameField.setFont(UIConfig.MENU_FONT);
        
        descriptionArea = DialogFactory.createHelpText("");
        descriptionArea.setRows(3);
        
        statesModel = new DefaultListModel<>();
        statesList = new JList<>(statesModel);
        statesList.setFont(UIConfig.MENU_FONT);
        statesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
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
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("States:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(DialogFactory.createScrollPane(statesList), gbc);
        
        // Create buttons
        JButton addStateButton = new JButton("Add State");
        JButton removeStateButton = new JButton("Remove State");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = DialogFactory.createCloseButton(dialog);
        
        // Style buttons
        for (JButton button : new JButton[]{addStateButton, removeStateButton, saveButton}) {
            button.setFont(UIConfig.BUTTON_FONT);
            button.setBackground(UIConfig.BUTTON_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
        }
        
        // Add button actions
        addStateButton.addActionListener(e -> {
            String state = JOptionPane.showInputDialog(dialog, "Enter state name:");
            if (state != null && !state.trim().isEmpty()) {
                statesModel.addElement(state.trim());
            }
        });
        
        removeStateButton.addActionListener(e -> {
            int selectedIndex = statesList.getSelectedIndex();
            if (selectedIndex != -1) {
                statesModel.remove(selectedIndex);
            }
        });
        
        saveButton.addActionListener(e -> {
            if (node != null) {
                node.setName(nameField.getText());
                node.setDescription(descriptionArea.getText());
                List<String> states = new ArrayList<>();
                for (int i = 0; i < statesModel.size(); i++) {
                    states.add(statesModel.getElementAt(i));
                }
                node.setStates(states);
                dialog.dispose();
            }
        });
        
        // Create button panel
        JPanel buttonPanel = DialogFactory.createButtonPanel(
            addStateButton, removeStateButton, saveButton, cancelButton
        );
        
        // Add panels to dialog
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void show(Node node) {
        this.node = node;
        nameField.setText(node.getName());
        descriptionArea.setText(node.getDescription());
        statesModel.clear();
        for (String state : node.getStates()) {
            statesModel.addElement(state);
        }
        DialogFactory.showDialog(dialog, 400, 500);
    }
} 