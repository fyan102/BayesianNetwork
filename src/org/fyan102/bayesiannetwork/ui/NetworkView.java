package org.fyan102.bayesiannetwork.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fyan102.bayesiannetwork.model.Node;
import org.fyan102.bayesiannetwork.model.Network;
import org.fyan102.bayesiannetwork.ui.config.UIConfig;

public class NetworkView extends JPanel implements MouseListener {
    private final Network network;
    private final Map<Node, NodeView> nodeViews;
    private final List<Link> links;
    private boolean linkCreationMode;
    private NodeView sourceNode;
    
    // Modern UI constants
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color GRID_COLOR = new Color(230, 230, 230);
    private static final int GRID_SIZE = 20;
    private static final Color BUTTON_COLOR = new Color(65, 105, 225); // Royal Blue
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public NetworkView(Network network) {
        this.network = network;
        this.nodeViews = new HashMap<>();
        this.links = new ArrayList<>();
        this.linkCreationMode = false;
        
        setLayout(null);
        setBackground(UIConfig.BACKGROUND_COLOR);
        setPreferredSize(new Dimension(UIConfig.DEFAULT_WINDOW_WIDTH, UIConfig.DEFAULT_WINDOW_HEIGHT));
        addMouseListener(this);
    }

    public Network getNetwork() {
        return network;
    }

    public List<NodeView> getNodes() {
        return new ArrayList<>(nodeViews.values());
    }

    public List<Link> getLinks() {
        return new ArrayList<>(links);
    }

    public void addNode(Node node) {
        if (!nodeViews.containsKey(node)) {
            NodeView nodeView = new NodeView(node);
            nodeViews.put(node, nodeView);
            add(nodeView);
            nodeView.setLocation(100 + nodeViews.size() * 50, 100 + nodeViews.size() * 50);
            nodeView.setVisible(true);
            updateLinks();
            repaint();
        }
    }

    public void removeNode(Node node) {
        NodeView nodeView = nodeViews.remove(node);
        if (nodeView != null) {
            remove(nodeView);
            links.removeIf(link -> link.getFrom() == nodeView || link.getTo() == nodeView);
            updateLinks();
            repaint();
        }
    }

    public void addLink(Node from, Node to) {
        NodeView fromView = nodeViews.get(from);
        NodeView toView = nodeViews.get(to);
        if (fromView != null && toView != null) {
            links.add(new Link(fromView, toView));
            updateLinks();
            repaint();
        }
    }

    public void removeLink(Node from, Node to) {
        NodeView fromView = nodeViews.get(from);
        NodeView toView = nodeViews.get(to);
        if (fromView != null && toView != null) {
            links.removeIf(link -> link.getFrom() == fromView && link.getTo() == toView);
            updateLinks();
            repaint();
        }
    }

    public void updateLinks() {
        links.clear();
        for (Node node : network.getNodes()) {
            NodeView nodeView = nodeViews.get(node);
            if (nodeView != null) {
                for (Node child : node.getChildren()) {
                    NodeView childView = nodeViews.get(child);
                    if (childView != null) {
                        links.add(new Link(nodeView, childView));
                    }
                }
            }
        }
        repaint();
    }

    public void toggleLinkCreationMode() {
        linkCreationMode = !linkCreationMode;
        if (linkCreationMode) {
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            sourceNode = null;
        }
        repaint();
    }

    public boolean isLinkCreationMode() {
        return linkCreationMode;
    }

    public void refresh() {
        nodeViews.values().forEach(NodeView::refresh);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw grid
        drawGrid(g2d);
        
        // Draw links
        for (Link link : links) {
            link.draw(g2d);
        }
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(UIConfig.GRID_COLOR);
        for (int x = 0; x < getWidth(); x += UIConfig.GRID_SIZE) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += UIConfig.GRID_SIZE) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (linkCreationMode) {
            Component component = getComponentAt(e.getPoint());
            if (component instanceof NodeView) {
                NodeView clickedNode = (NodeView) component;
                if (sourceNode == null) {
                    sourceNode = clickedNode;
                } else if (sourceNode != clickedNode) {
                    // Notify controller to add edge
                    firePropertyChange("addEdge", null, new EdgeEvent(sourceNode.getNode(), clickedNode.getNode()));
                    sourceNode = null;
                    toggleLinkCreationMode();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static class EdgeEvent {
        private final Node from;
        private final Node to;

        public EdgeEvent(Node from, Node to) {
            this.from = from;
            this.to = to;
        }

        public Node getFrom() {
            return from;
        }

        public Node getTo() {
            return to;
        }
    }
}
