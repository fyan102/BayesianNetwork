package org.fyan102.bayesiannetwork;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NetworkView extends JPanel {
    private ArrayList<NodeView> nodes;
    private Network network;
    private ArrayList<Link> links;
    
    // Modern UI constants
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color GRID_COLOR = new Color(230, 230, 230);
    private static final int GRID_SIZE = 20;

    public NetworkView() {
        nodes = new ArrayList<>();
        network = new Network();
        links = new ArrayList<>();
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setOpaque(true);
    }

    public NetworkView(Network network) {
        nodes = new ArrayList<>();
        links = new ArrayList<>();
        this.network = network;
        setLayout(null);
        setBackground(BACKGROUND_COLOR);
        setOpaque(true);
        initNetwork();
    }

    public void addNode(NodeView node) {
        nodes.add(node);
        network.addNode(node.getNode());
        add(node);
        repaint();
    }

    public void addNode(Node node) {
        if (node.getNumberOfStates() == 0) {
            node.addState("true");
            node.addState("false");
            node.setBeliefs(new double[]{0.5, 0.5});
        }
        network.addNode(node);
        NodeView nodeView = new NodeView(node);
        nodes.add(nodeView);
        add(nodeView);
        repaint();
    }

    public void calculate() {
        network.calculate();
        repaint();
        System.out.println(network);
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public Network getNetwork() {
        return network;
    }

    public NodeView getNode(int index) {
        return nodes.get(index);
    }

    public ArrayList<NodeView> getNodes() {
        return nodes;
    }

    private void initNetwork() {
        for (Node node : network.getNodes()) {
            NodeView nodeView = new NodeView(node);
            nodes.add(nodeView);
            add(nodeView);
        }
    }

    public void removeNode(NodeView node) {
        nodes.remove(node);
        remove(node);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw grid
        drawGrid(g2d);
        
        // Draw links
        for (Link link : links) {
            link.draw(g2d);
        }
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1));
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw vertical lines
        for (int x = 0; x < width; x += GRID_SIZE) {
            g2d.drawLine(x, 0, x, height);
        }
        
        // Draw horizontal lines
        for (int y = 0; y < height; y += GRID_SIZE) {
            g2d.drawLine(0, y, width, y);
        }
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
        repaint();
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNodes(ArrayList<NodeView> nodes) {
        this.nodes = nodes;
        removeAll();
        for (NodeView node : nodes) {
            add(node);
        }
        repaint();
    }

    public void updateLinks() {
        // Clear existing links
        links.clear();
        
        // Create new links based on parent-child relationships
        for (NodeView nodeView : nodes) {
            Node node = nodeView.getNode();
            for (Node parent : node.getParents()) {
                NodeView parentView = findNodeView(parent);
                if (parentView != null) {
                    Link link = new Link();
                    
                    // Calculate connection points on the edges of the nodes
                    Point from = calculateConnectionPoint(parentView, nodeView);
                    Point to = calculateConnectionPoint(nodeView, parentView);
                    
                    link.addPoint(from);
                    link.addPoint(to);
                    links.add(link);
                }
            }
        }
        
        repaint();
    }
    
    private NodeView findNodeView(Node node) {
        for (NodeView nodeView : nodes) {
            if (nodeView.getNode() == node) {
                return nodeView;
            }
        }
        return null;
    }
    
    private Point calculateConnectionPoint(NodeView from, NodeView to) {
        Point fromCenter = new Point(
            from.getX() + from.getWidth() / 2,
            from.getY() + from.getHeight() / 2
        );
        Point toCenter = new Point(
            to.getX() + to.getWidth() / 2,
            to.getY() + to.getHeight() / 2
        );
        
        // Calculate the angle between centers
        double angle = Math.atan2(toCenter.y - fromCenter.y, toCenter.x - fromCenter.x);
        
        // Calculate the intersection point with the node's edge
        int radius = Math.max(from.getWidth(), from.getHeight()) / 2;
        return new Point(
            (int)(fromCenter.x + radius * Math.cos(angle)),
            (int)(fromCenter.y + radius * Math.sin(angle))
        );
    }
}
