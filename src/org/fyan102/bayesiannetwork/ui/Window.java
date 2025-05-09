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

public class Window extends JFrame {
    private final NetworkController controller;
    private final NetworkView networkView;
    private final JPanel mainPanel;
    
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
        setTitle("Bayesian Network");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
        // Initialize components
        networkView = new NetworkView();
        controller = new NetworkController(new Network(), networkView);
        
        // Setup UI
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        setupMenuBar();
        setupToolBar();
        
        networkView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(networkView, BorderLayout.CENTER);
        
        add(mainPanel);
        loadApplicationIcon();
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

    private void setupToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(TOOLBAR_COLOR);
        toolBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        JButton addNodeButton = createToolbarButton("Add Node");
        addNodeButton.addActionListener(e -> addNode());
        
        JButton createLinkButton = createToolbarButton("Create Link");
        createLinkButton.addActionListener(e -> networkView.toggleLinkCreationMode());
        
        JButton runButton = createToolbarButton("Run");
        runButton.addActionListener(e -> controller.calculate());
        
        toolBar.add(addNodeButton);
        toolBar.add(createLinkButton);
        toolBar.add(runButton);
        
        mainPanel.add(toolBar, BorderLayout.NORTH);
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
        button.setBackground(TOOLBAR_COLOR);
        button.setForeground(BUTTON_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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

    private void addNode() {
        String name = JOptionPane.showInputDialog(this, "Enter node name:", "Add Node", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            controller.addNode(name.trim());
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.loadFromFile(file).ifPresent(network -> {
                controller.clear();
                network.getNodes().forEach(node -> controller.addNode(node.getName()));
            });
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            controller.saveToFile(file);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window();
            window.setVisible(true);
        });
    }
}
