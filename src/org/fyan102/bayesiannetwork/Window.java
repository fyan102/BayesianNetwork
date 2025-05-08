package org.fyan102.bayesiannetwork;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import org.fyan102.bayesiannetwork.util.NetworkFileHandler;

public class Window extends JFrame {
    // The view for network
    private NetworkView network;
    // The view for menu
    private MenuView menu;
    // Main panel
    private JPanel mainPanel;
    
    // Modern UI constants
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color TOOLBAR_COLOR = new Color(240, 240, 240);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Color BUTTON_COLOR = new Color(65, 105, 225); // Royal Blue
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237); // Cornflower Blue

    public Window() {
        initializeUI();
        setupNetwork();
    }

    private void initializeUI() {
        setTitle("Bayesian Network");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Create main panel with modern look
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Setup menu bar
        setupMenuBar();
        
        // Setup toolbar
        setupToolBar();

        // Setup network view
        network = new NetworkView();
        network.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(network, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(TOOLBAR_COLOR);
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        // File menu
        JMenu fileMenu = createMenu("File");
        JMenuItem openItem = createMenuItem("Open", e -> openFile());
        JMenuItem saveItem = createMenuItem("Save", e -> saveFile());
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        
        // Network menu
        JMenu networkMenu = createMenu("Network");
        JMenuItem runItem = createMenuItem("Run", e -> network.calculate());
        JMenuItem addNodeItem = createMenuItem("Add Chance Node", e -> addNode());
        networkMenu.add(runItem);
        networkMenu.add(addNodeItem);

        menuBar.add(fileMenu);
        menuBar.add(networkMenu);
        setJMenuBar(menuBar);
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setFont(MENU_FONT);
        return menu;
    }

    private JMenuItem createMenuItem(String title, ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(MENU_FONT);
        item.addActionListener(listener);
        return item;
    }

    private void setupToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(TOOLBAR_COLOR);
        toolBar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton newButton = createToolbarButton("New");
        JButton openButton = createToolbarButton("Open");
        openButton.addActionListener(e -> openFile());
        JButton closeButton = createToolbarButton("Close");
        JButton newNodeButton = createToolbarButton("Chance Node");
        newNodeButton.addActionListener(e -> addNode());
        JButton runButton = createToolbarButton("Run");
        runButton.addActionListener(e -> network.calculate());

        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(closeButton);
        toolBar.addSeparator(new Dimension(20, 0));
        toolBar.add(newNodeButton);
        toolBar.add(runButton);

        mainPanel.add(toolBar, BorderLayout.NORTH);
    }

    private JButton createToolbarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(TOOLBAR_COLOR);
        button.setForeground(BUTTON_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(BUTTON_COLOR);
            }
        });
        
        return button;
    }

    private void setupNetwork() {
        Node node = new Node("Node1");
        node.addState("true");
        node.addState("false");
        
        Node parent1 = new Node("Parent1");
        Node parent2 = new Node("Parent2");
        
        parent1.addState("St1");
        parent1.addState("St2");
        parent1.setBeliefs(new double[]{0.3, 0.7});
        
        parent2.addState("St1");
        parent2.addState("St2");
        parent2.addState("St3");
        parent2.setBeliefs(new double[]{0.2, 0.3, 0.5});
        
        node.addParent(parent1);
        node.addParent(parent2);
        node.setProbs(new double[][]{
            {0.2, 0.8}, {0.15, 0.85}, {0.18, 0.82},
            {0.07, 0.93}, {0.85, 0.15}, {0.62, 0.38}
        });
        
        network.addNode(parent1);
        network.addNode(parent2);
        network.addNode(node);
    }

    private void addNode() {
        network.addNode(new Node());
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".bn");
            }
            public String getDescription() {
                return "Bayesian Network Files (*.bn)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                NetworkFileHandler.loadNetwork(network, selectedFile);
                JOptionPane.showMessageDialog(this, 
                    "Network loaded successfully!", 
                    "Load Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error loading network: " + ex.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".bn");
            }
            public String getDescription() {
                return "Bayesian Network Files (*.bn)";
            }
        });
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".bn")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".bn");
            }
            
            try {
                NetworkFileHandler.saveNetwork(network, selectedFile);
                JOptionPane.showMessageDialog(this, 
                    "Network saved successfully!", 
                    "Save Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error saving network: " + ex.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            window.setVisible(true);
        });
    }
}
