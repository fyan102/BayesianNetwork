package org.fyan102.bayesiannetwork.ui;

import org.fyan102.bayesiannetwork.controller.NetworkController;
import org.fyan102.bayesiannetwork.model.Network;
import org.fyan102.bayesiannetwork.model.Node;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    private NetworkController controller;
    private NetworkView networkView;
    private JPanel toolbar;
    private JPanel contentPanel;
    
    // Modern UI constants
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color TOOLBAR_COLOR = new Color(240, 240, 240);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Color BUTTON_COLOR = new Color(65, 105, 225); // Royal Blue
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237); // Cornflower Blue
    private static final Color BUTTON_PRESSED_COLOR = new Color(0, 80, 150); // Windows 11 pressed blue
    private static final Color TEXT_COLOR = new Color(32, 32, 32);           // Windows 11 text
    private static final Color MENU_HOVER_COLOR = new Color(237, 237, 237);  // Windows 11 menu hover
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final int CORNER_RADIUS = 8;  // Windows 11 corner radius
    private static final int TOOLBAR_HEIGHT = 40;

    public Window() {
        super("Bayesian Network");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
        // Initialize components
        initComponents();
        
        // Setup UI
        setupMenuBar();
        setupToolbar();
        setupContentPanel();
        
        // Load application icon
        loadApplicationIcon();
        
        // Create demo network
        createDemoNetwork();
    }

    private void initComponents() {
        // Create network view and controller
        controller = new NetworkController();
        networkView = controller.getNetworkView();
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(TOOLBAR_COLOR);
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        // File menu
        JMenu fileMenu = createMenu("File");
        JMenuItem newItem = createMenuItem("New", e -> newNetwork());
        JMenuItem openItem = createMenuItem("Open", e -> openFile());
        JMenuItem saveItem = createMenuItem("Save", e -> saveFile());
        JMenuItem exitItem = createMenuItem("Exit", e -> System.exit(0));
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Node menu
        JMenu nodeMenu = createMenu("Node");
        JMenuItem addNodeItem = createMenuItem("Add Node", e -> addNode());
        nodeMenu.add(addNodeItem);
        
        // Network menu
        JMenu networkMenu = createMenu("Network");
        JMenuItem createLinkItem = createMenuItem("Create Link", e -> networkView.toggleLinkCreationMode());
        JMenuItem runItem = createMenuItem("Run", e -> controller.calculate());
        networkMenu.add(createLinkItem);
        networkMenu.add(runItem);

        // Help menu
        JMenu helpMenu = createMenu("Help");
        JMenuItem helpItem = createMenuItem("Help", e -> showHelp());
        JMenuItem aboutItem = createMenuItem("About", e -> showAbout());
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(nodeMenu);
        menuBar.add(networkMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void setupToolbar() {
        toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(TOOLBAR_COLOR);
        toolbar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Add node button
        JButton addNodeButton = createToolbarButton("Add Node");
        addNodeButton.addActionListener(e -> addNode());
        
        // Create link button
        JButton createLinkButton = createToolbarButton("Create Link");
        createLinkButton.addActionListener(e -> networkView.toggleLinkCreationMode());
        
        // Run button
        JButton runButton = createToolbarButton("Run");
        runButton.addActionListener(e -> controller.calculate());
        
        toolbar.add(addNodeButton);
        toolbar.add(createLinkButton);
        toolbar.add(runButton);
        
        add(toolbar, BorderLayout.NORTH);
    }

    private void setupContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.add(networkView, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setFont(MENU_FONT);
        menu.setForeground(TEXT_COLOR);
        
        menu.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                menu.setBackground(MENU_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                menu.setBackground(TOOLBAR_COLOR);
            }
        });
        
        return menu;
    }

    private JMenuItem createMenuItem(String title, ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(MENU_FONT);
        item.setForeground(TEXT_COLOR);
        item.addActionListener(listener);
        
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(MENU_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(TOOLBAR_COLOR);
            }
        });
        
        return item;
    }

    private JButton createToolbarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
        
        return button;
    }

    private void newNetwork() {
        controller.clear();
        networkView.removeAll();
        networkView.repaint();
    }

    private void addNode() {
        String name = JOptionPane.showInputDialog(this, "Enter node name:", "Add Node", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            Node node = new Node(name.trim());
            controller.addNode(node);
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.loadNetwork(file);
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.saveNetwork(file);
        }
    }

    private void showHelp() {
        JDialog helpDialog = new JDialog(this, "Help", true);
        helpDialog.setLayout(new BorderLayout());
        helpDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setBackground(BACKGROUND_COLOR);
        helpText.setFont(MENU_FONT);
        helpText.setText(
            "Bayesian Network Help\n\n" +
            "1. Adding Nodes:\n" +
            "   - Click 'Add Node' button or use Node menu\n" +
            "   - Enter a name for the node\n\n" +
            "2. Creating Links:\n" +
            "   - Click 'Create Link' button\n" +
            "   - Click on source node\n" +
            "   - Click on target node\n\n" +
            "3. Running the Network:\n" +
            "   - Click 'Run' button to calculate probabilities\n\n" +
            "4. Saving/Loading:\n" +
            "   - Use File menu to save or load networks"
        );
        
        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        helpDialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(BUTTON_FONT);
        closeButton.setBackground(BUTTON_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> helpDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        helpDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        helpDialog.setSize(500, 400);
        helpDialog.setLocationRelativeTo(this);
        helpDialog.setVisible(true);
    }

    private void showAbout() {
        JDialog aboutDialog = new JDialog(this, "About", true);
        aboutDialog.setLayout(new BorderLayout());
        aboutDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Bayesian Network", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel versionLabel = new JLabel("Version 1.0", JLabel.CENTER);
        versionLabel.setFont(MENU_FONT);
        versionLabel.setForeground(TEXT_COLOR);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setBackground(BACKGROUND_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(versionLabel);
        
        aboutDialog.add(textPanel, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(BUTTON_FONT);
        closeButton.setBackground(BUTTON_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> aboutDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        aboutDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.setSize(400, 300);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setVisible(true);
    }

    private void createDemoNetwork() {
        // Create nodes
        Node rain = new Node("Rain");
        rain.setStates(List.of("Yes", "No"));
        rain.setBeliefs(List.of(0.2, 0.8)); // 20% chance of rain

        Node sprinkler = new Node("Sprinkler");
        sprinkler.setStates(List.of("On", "Off"));
        sprinkler.setBeliefs(List.of(0.1, 0.9)); // 10% chance sprinkler is on

        Node wetGrass = new Node("Wet Grass");
        wetGrass.setStates(List.of("Wet", "Dry"));
        wetGrass.setBeliefs(List.of(0.0, 1.0)); // Initial belief

        // Add nodes to network
        controller.addNode(rain);
        controller.addNode(sprinkler);
        controller.addNode(wetGrass);

        // Create links
        controller.addEdge(rain, wetGrass);
        controller.addEdge(sprinkler, wetGrass);

        // Set conditional probabilities
        List<ArrayList<Double>> rainProbs = new ArrayList<>();
        rainProbs.add(new ArrayList<>(List.of(0.2, 0.8))); // P(Rain=Yes) = 0.2
        rainProbs.add(new ArrayList<>(List.of(0.8, 0.2))); // P(Rain=No) = 0.8
        rain.setProbs(rainProbs);

        List<ArrayList<Double>> sprinklerProbs = new ArrayList<>();
        sprinklerProbs.add(new ArrayList<>(List.of(0.1, 0.9))); // P(Sprinkler=On) = 0.1
        sprinklerProbs.add(new ArrayList<>(List.of(0.9, 0.1))); // P(Sprinkler=Off) = 0.9
        sprinkler.setProbs(sprinklerProbs);

        // P(WetGrass=Wet | Rain=Yes, Sprinkler=On) = 0.99
        // P(WetGrass=Wet | Rain=Yes, Sprinkler=Off) = 0.9
        // P(WetGrass=Wet | Rain=No, Sprinkler=On) = 0.9
        // P(WetGrass=Wet | Rain=No, Sprinkler=Off) = 0.0
        List<ArrayList<Double>> wetGrassProbs = new ArrayList<>();
        wetGrassProbs.add(new ArrayList<>(List.of(0.99, 0.01))); // Wet | Rain=Yes, Sprinkler=On
        wetGrassProbs.add(new ArrayList<>(List.of(0.9, 0.1)));   // Wet | Rain=Yes, Sprinkler=Off
        wetGrassProbs.add(new ArrayList<>(List.of(0.9, 0.1)));   // Wet | Rain=No, Sprinkler=On
        wetGrassProbs.add(new ArrayList<>(List.of(0.0, 1.0)));   // Wet | Rain=No, Sprinkler=Off
        wetGrass.setProbs(wetGrassProbs);

        // Calculate initial beliefs
        controller.calculate();
    }

    private void loadApplicationIcon() {
        try {
            URL iconUrl = getClass().getResource("/icon.ico");
            if (iconUrl != null) {
                setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            window.setVisible(true);
        });
    }
}
