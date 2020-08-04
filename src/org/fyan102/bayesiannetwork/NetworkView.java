package org.fyan102.bayesiannetwork;

import java.util.ArrayList;

public class NetworkView {
    private ArrayList<NodeView> nodes;
    private Network network;
    private ArrayList<Link> links;

    public NetworkView() {
        nodes = new ArrayList<>();
        network = new Network();
        links = new ArrayList<>();
    }

    public NetworkView(Network network) {
        nodes = new ArrayList<>();
        links = new ArrayList<>();
        this.network = network;
        initNetwork();
    }

    public void addNode(NodeView node) {
        nodes.add(node);
        network.addNode(node.getNode());
    }

    public void addNode(Node node) {
        network.addNode(node);
        nodes.add(new NodeView(node));
    }

    public void calculate() {
        network.calculate();
        repaint();
        System.out.println(network);
    }

    public ArrayList<javafx.scene.Node> getAllComponents() {
        ArrayList<javafx.scene.Node> components = new ArrayList<>();
        for (NodeView node : nodes) {
            components.addAll(node.getComponents());
        }
        return components;
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
        }
    }

    public void removeNode(NodeView node) {
        nodes.remove(node);
        repaint();
    }

    public void repaint() {
        for (NodeView node : nodes) {
            node.repaint();
        }
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNodes(ArrayList<NodeView> nodes) {
        this.nodes = nodes;
    }
}
