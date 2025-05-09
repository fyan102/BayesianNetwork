package org.fyan102.bayesiannetwork.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import org.fyan102.bayesiannetwork.model.Node;

public class NodeView extends JPanel {
    private JLabel title;
    private ArrayList<JLabel> states;
    private ArrayList<JProgressBar> percents;
    private Node node;
    private Point dragStart;
    private Point location;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color TITLE_COLOR = new Color(70, 70, 70);
    private static final Color STATE_COLOR = new Color(100, 100, 100);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font STATE_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Color BUTTON_COLOR = new Color(65, 105, 225); // Royal Blue
    private static final Color BUTTON_HOVER_COLOR = new Color(105, 145, 255);

    public NodeView() {
        this(new Node());
    }

    public NodeView(Node node) {
        this.node = node;
        title = new JLabel();
        states = new ArrayList<>();
        percents = new ArrayList<>();
        
        setLayout(null);
        setSize(280, 80);
        setLocation(200, 150);
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Enable anti-aliasing
        setOpaque(true);
        
        init();
        setupMouseListeners();
    }

    private void init() {
        if (node != null) {
            title.setText(node.getName());
            title.setFont(TITLE_FONT);
            title.setForeground(TITLE_COLOR);
            title.setBounds(10, 5, 260, 25);
            add(title);

            int height = (node.getNumberOfStates() + 1) * 30 + 20;
            setSize(280, height);

            for (int i = 0; i < node.getNumberOfStates(); i++) {
                JLabel state = new JLabel(node.getState(i) + ": \t \t " + String.format("%.4f", node.getBelief(i)));
                state.setFont(STATE_FONT);
                state.setForeground(STATE_COLOR);
                state.setBounds(10, 35 + i * 30, 260, 20);
                states.add(state);
                add(state);

                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setValue((int)(node.getBelief(i) * 100));
                progressBar.setBounds(10, 55 + i * 30, 260, 8);
                progressBar.setForeground(new Color(65, 105, 225)); // Royal Blue
                progressBar.setBackground(new Color(230, 230, 230));
                progressBar.setBorderPainted(false);
                progressBar.setStringPainted(false);
                percents.add(progressBar);
                add(progressBar);
            }
        }
    }

    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
                location = getLocation();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point newLocation = new Point(
                    location.x + e.getX() - dragStart.x,
                    location.y + e.getY() - dragStart.y
                );
                setLocation(newLocation);
                
                // Notify parent to update links
                Container parent = getParent();
                if (parent instanceof NetworkView) {
                    ((NetworkView) parent).updateLinks();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showStateEditor();
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Right click
                    showContextMenu(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(100, 149, 237), 1, true), // Cornflower Blue
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void showContextMenu(int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        JMenuItem propertyItem = new JMenuItem("Properties");
        propertyItem.setFont(STATE_FONT);
        propertyItem.addActionListener(e -> showPropertyDialog());
        popup.add(propertyItem);
        
        // Add link creation menu items
        if (getParent() instanceof NetworkView) {
            NetworkView networkView = (NetworkView) getParent();
            JMenu addLinkMenu = new JMenu("Add Link From");
            addLinkMenu.setFont(STATE_FONT);
            
            for (NodeView otherNode : networkView.getNodes()) {
                if (otherNode != this && !node.getParents().contains(otherNode.getNode())) {
                    JMenuItem linkItem = new JMenuItem(otherNode.getNode().getName());
                    linkItem.setFont(STATE_FONT);
                    linkItem.addActionListener(e -> {
                        if (networkView.getNetwork().wouldCreateCycle(otherNode.getNode(), node)) {
                            JOptionPane.showMessageDialog(this,
                                "Cannot create link: would create a cycle in the network",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        } else {
                            node.addParent(otherNode.getNode());
                            networkView.updateLinks();
                            repaint();
                        }
                    });
                    addLinkMenu.add(linkItem);
                }
            }
            
            if (addLinkMenu.getItemCount() > 0) {
                popup.addSeparator();
                popup.add(addLinkMenu);
            }
        }
        
        popup.show(this, x, y);
    }

    private void showPropertyDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Node Properties", true);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);
        
        JLabel nameLabel = new JLabel("Name: " + node.getName());
        nameLabel.setFont(TITLE_FONT);
        nameLabel.setForeground(TITLE_COLOR);
        panel.add(nameLabel);
        
        panel.add(new JSeparator());
        
        for (int i = 0; i < node.getNumberOfStates(); i++) {
            JLabel stateLabel = new JLabel(String.format("%s: %.4f", node.getState(i), node.getBelief(i)));
            stateLabel.setFont(STATE_FONT);
            stateLabel.setForeground(STATE_COLOR);
            panel.add(stateLabel);
        }
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    @Override
    public void repaint() {
        if (node != null) {
            title.setText(node.getName());
            
            for (int i = 0; i < states.size(); i++) {
                states.get(i).setText(node.getState(i) + ": \t \t " + String.format("%.4f", node.getBelief(i)));
                percents.get(i).setValue((int)(node.getBelief(i) * 100));
            }
        }
        
        super.repaint();
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
        removeAll();
        states.clear();
        percents.clear();
        init();
        repaint();
    }

    private void showStateEditor() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Edit Node States", true);
        dialog.setLayout(new BorderLayout());
        
        // Create main panel with modern styling
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Node name field with modern styling
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(BACKGROUND_COLOR);
        JLabel nameLabel = new JLabel("Node Name:");
        nameLabel.setFont(TITLE_FONT);
        nameLabel.setForeground(TITLE_COLOR);
        JTextField nameField = new JTextField(node.getName(), 20);
        nameField.setFont(STATE_FONT);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        mainPanel.add(namePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // States panel with modern styling
        JPanel statesPanel = new JPanel();
        statesPanel.setLayout(new BoxLayout(statesPanel, BoxLayout.Y_AXIS));
        statesPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            "States",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            TITLE_FONT,
            TITLE_COLOR
        ));
        statesPanel.setBackground(BACKGROUND_COLOR);
        
        ArrayList<JPanel> statePanels = new ArrayList<>();
        for (int i = 0; i < node.getNumberOfStates(); i++) {
            JPanel statePanel = createStatePanel(i);
            statePanels.add(statePanel);
            statesPanel.add(statePanel);
            statesPanel.add(Box.createVerticalStrut(10));
        }
        
        // Add state button with modern styling
        JButton addStateButton = new JButton("Add State");
        addStateButton.setFont(BUTTON_FONT);
        addStateButton.setBackground(BUTTON_COLOR);
        addStateButton.setForeground(Color.WHITE);
        addStateButton.setFocusPainted(false);
        addStateButton.setBorderPainted(false);
        addStateButton.addActionListener(e -> {
            JPanel newStatePanel = createStatePanel(node.getNumberOfStates());
            statePanels.add(newStatePanel);
            statesPanel.add(newStatePanel);
            statesPanel.add(Box.createVerticalStrut(10));
            dialog.pack();
        });
        
        mainPanel.add(statesPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(addStateButton);
        
        // Conditional probabilities panel if node has parents
        if (!node.getParents().isEmpty()) {
            mainPanel.add(Box.createVerticalStrut(20));
            JPanel condProbsPanel = createConditionalProbabilitiesPanel();
            mainPanel.add(condProbsPanel);
        } else {
            // Only show probability fields for nodes without parents
            for (JPanel statePanel : statePanels) {
                Component[] components = statePanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof JTextField) {
                        JTextField textField = (JTextField) comp;
                        if (textField.getText().contains(":")) {
                            // This is the probability field
                            textField.setVisible(true);
                        }
                    }
                }
            }
        }
        
        // Buttons panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        // Style buttons
        for (JButton button : new JButton[]{saveButton, cancelButton}) {
            button.setFont(BUTTON_FONT);
            button.setBackground(BUTTON_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(100, 30));
        }
        
        saveButton.addActionListener(e -> {
            if (validateAndSaveStateChanges(statePanels)) {
                dialog.dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createStatePanel(int index) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);
        
        JTextField stateField = new JTextField(10);
        stateField.setFont(STATE_FONT);
        if (index < node.getNumberOfStates()) {
            stateField.setText(node.getState(index));
        }
        
        JTextField beliefField = new JTextField(5);
        beliefField.setFont(STATE_FONT);
        if (index < node.getNumberOfStates()) {
            beliefField.setText(String.format("%.2f", node.getBelief(index)));
        }
        
        // Hide probability field if node has parents
        beliefField.setVisible(node.getParents().isEmpty());
        
        JButton removeButton = new JButton("×");
        removeButton.setFont(new Font("Arial", Font.BOLD, 12));
        removeButton.setPreferredSize(new Dimension(20, 20));
        removeButton.setBackground(BUTTON_COLOR);
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.addActionListener(e -> {
            Container parent = panel.getParent();
            parent.remove(panel);
            parent.revalidate();
            parent.repaint();
        });
        
        JLabel stateLabel = new JLabel("State:");
        stateLabel.setFont(STATE_FONT);
        stateLabel.setForeground(STATE_COLOR);
        JLabel probLabel = new JLabel("Probability:");
        probLabel.setFont(STATE_FONT);
        probLabel.setForeground(STATE_COLOR);
        probLabel.setVisible(node.getParents().isEmpty());
        
        panel.add(stateLabel);
        panel.add(stateField);
        panel.add(probLabel);
        panel.add(beliefField);
        panel.add(removeButton);
        
        return panel;
    }

    private JPanel createConditionalProbabilitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            "Conditional Probabilities",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            TITLE_FONT,
            TITLE_COLOR
        ));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Get all possible parent state combinations
        ArrayList<ArrayList<String>> parentStateCombinations = getParentStateCombinations();
        
        // Create column names
        String[] columnNames = new String[node.getNumberOfStates() + 1];
        columnNames[0] = "Condition";
        for (int i = 0; i < node.getNumberOfStates(); i++) {
            columnNames[i + 1] = node.getState(i);
        }
        
        // Create table data
        Object[][] data = new Object[parentStateCombinations.size()][node.getNumberOfStates() + 1];
        for (int i = 0; i < parentStateCombinations.size(); i++) {
            ArrayList<String> combination = parentStateCombinations.get(i);
            
            // Create condition text
            StringBuilder conditionText = new StringBuilder("If ");
            for (int j = 0; j < node.getParents().size(); j++) {
                if (j > 0) conditionText.append(" AND ");
                conditionText.append(node.getParents().get(j).getName())
                           .append(" = ")
                           .append(combination.get(j));
            }
            data[i][0] = conditionText.toString();
            
            // Add probability values
            int rowIndex = getConditionalProbabilityRowIndex(combination);
            if (rowIndex >= 0 && rowIndex < node.getProbs().size()) {
                ArrayList<Double> probs = node.getProbs().get(rowIndex);
                for (int j = 0; j < node.getNumberOfStates(); j++) {
                    if (j < probs.size()) {
                        data[i][j + 1] = String.format("%.2f", probs.get(j));
                    } else {
                        data[i][j + 1] = "0.00";
                    }
                }
            }
        }
        
        // Create table with custom model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make only probability columns editable
                return column > 0;
            }
        };
        JTable table = new JTable(model);
        
        table.setFont(STATE_FONT);
        table.setRowHeight(25);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        
        // Style header
        table.getTableHeader().setFont(TITLE_FONT);
        table.getTableHeader().setBackground(BACKGROUND_COLOR);
        table.getTableHeader().setForeground(TITLE_COLOR);
        
        // Make first column wider
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add validation button
        JButton validateButton = new JButton("Validate Probabilities");
        validateButton.setFont(BUTTON_FONT);
        validateButton.setBackground(BUTTON_COLOR);
        validateButton.setForeground(Color.WHITE);
        validateButton.setFocusPainted(false);
        validateButton.setBorderPainted(false);
        validateButton.addActionListener(e -> validateProbabilities(table));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(validateButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void validateProbabilities(JTable table) {
        boolean valid = true;
        StringBuilder errorMessage = new StringBuilder();
        
        // Check each row
        for (int i = 0; i < table.getRowCount(); i++) {
            double sum = 0.0;
            for (int j = 1; j < table.getColumnCount(); j++) {
                try {
                    String value = table.getValueAt(i, j).toString();
                    double prob = Double.parseDouble(value);
                    if (prob < 0 || prob > 1) {
                        valid = false;
                        errorMessage.append(String.format("Row %d: Probability must be between 0 and 1\n", i + 1));
                    }
                    sum += prob;
                } catch (NumberFormatException ex) {
                    valid = false;
                    errorMessage.append(String.format("Row %d: Invalid probability value\n", i + 1));
                }
            }
            
            if (Math.abs(sum - 1.0) > 0.0001) {
                valid = false;
                errorMessage.append(String.format("Row %d: Probabilities must sum to 1 (current sum: %.2f)\n", 
                    i + 1, sum));
            }
        }
        
        if (!valid) {
            JOptionPane.showMessageDialog(this,
                errorMessage.toString(),
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "All probabilities are valid!",
                "Validation Successful",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private boolean validateAndSaveStateChanges(ArrayList<JPanel> statePanels) {
        // Validate and collect states
        ArrayList<String> newStates = new ArrayList<>();
        ArrayList<Double> newBeliefs = new ArrayList<>();
        
        for (JPanel statePanel : statePanels) {
            Component[] components = statePanel.getComponents();
            JTextField stateField = null;
            JTextField beliefField = null;
            
            for (Component comp : components) {
                if (comp instanceof JTextField) {
                    if (stateField == null) {
                        stateField = (JTextField) comp;
                    } else if (comp.isVisible()) {
                        beliefField = (JTextField) comp;
                    }
                }
            }
            
            if (stateField != null) {
                String stateName = stateField.getText().trim();
                if (!stateName.isEmpty()) {
                    newStates.add(stateName);
                    if (beliefField != null) {
                        try {
                            double belief = Double.parseDouble(beliefField.getText());
                            newBeliefs.add(belief);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this,
                                "Invalid probability value: " + beliefField.getText(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }
            }
        }
        
        // Validate that we have at least one state
        if (newStates.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "At least one state is required",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Only validate probabilities for nodes without parents
        if (node.getParents().isEmpty()) {
            // If no beliefs were provided, initialize with uniform distribution
            if (newBeliefs.isEmpty()) {
                double uniformProb = 1.0 / newStates.size();
                for (int i = 0; i < newStates.size(); i++) {
                    newBeliefs.add(uniformProb);
                }
            } else {
                double sum = newBeliefs.stream().mapToDouble(d -> d).sum();
                if (Math.abs(sum - 1.0) > 0.0001) {
                    JOptionPane.showMessageDialog(this,
                        "Probabilities must sum to 1. Current sum: " + String.format("%.2f", sum),
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }
        
        // Update node
        node.setStates(newStates);
        if (!node.getParents().isEmpty()) {
            // For nodes with parents, initialize beliefs with uniform distribution
            double[] beliefsArray = new double[newStates.size()];
            double uniformProb = 1.0 / newStates.size();
            for (int i = 0; i < beliefsArray.length; i++) {
                beliefsArray[i] = uniformProb;
            }
            node.setBeliefs(beliefsArray);
        } else {
            double[] beliefsArray = newBeliefs.stream().mapToDouble(d -> d).toArray();
            node.setBeliefs(beliefsArray);
        }
        
        // Update view
        removeAll();
        states.clear();
        percents.clear();
        init();
        repaint();
        
        return true;
    }

    private ArrayList<ArrayList<String>> getParentStateCombinations() {
        ArrayList<ArrayList<String>> combinations = new ArrayList<>();
        ArrayList<Node> parents = node.getParents();
        
        if (parents.isEmpty()) {
            return combinations;
        }
        
        // Initialize with first parent's states
        for (String state : parents.get(0).getStates()) {
            ArrayList<String> combination = new ArrayList<>();
            combination.add(state);
            combinations.add(combination);
        }
        
        // Add states from remaining parents
        for (int i = 1; i < parents.size(); i++) {
            ArrayList<ArrayList<String>> newCombinations = new ArrayList<>();
            for (ArrayList<String> combination : combinations) {
                for (String state : parents.get(i).getStates()) {
                    ArrayList<String> newCombination = new ArrayList<>(combination);
                    newCombination.add(state);
                    newCombinations.add(newCombination);
                }
            }
            combinations = newCombinations;
        }
        
        return combinations;
    }

    private int getConditionalProbabilityRowIndex(ArrayList<String> combination) {
        ArrayList<Node> parents = node.getParents();
        int rowIndex = 0;
        int multiplier = 1;
        
        for (int i = parents.size() - 1; i >= 0; i--) {
            Node parent = parents.get(i);
            int stateIndex = parent.getStates().indexOf(combination.get(i));
            rowIndex += stateIndex * multiplier;
            multiplier *= parent.getNumberOfStates();
        }
        
        return rowIndex;
    }
}
