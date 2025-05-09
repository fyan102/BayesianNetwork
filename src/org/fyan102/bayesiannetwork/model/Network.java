package org.fyan102.bayesiannetwork.model;

import java.util.ArrayList;

public class Network {
    private ArrayList<Node> nodes;

    public Network() {
        nodes = new ArrayList<>();
    }

    public Network(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public Network(Node... nodes) {
        for (Node node : nodes) {
            this.nodes.add(node);
        }
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public boolean calculate() {
        int i = 0;
        for (String lastResult = ""; i < nodes.size() + 1; i++) {
            for (Node node : nodes) {
                node.updateBelieves();
            }
            String result = toString();
            if (!lastResult.equals(result)) {
                lastResult = result;
            }
            else {
                break;
            }
        }
        if (i == nodes.size()) {
            return false;
        }
        else {
            return true;
        }
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void removeNode(int index) {
        nodes.remove(index);
    }

    public void setNode(int index, Node node) {
        nodes.set(index, node);
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "Network{" +
                "nodes=" + nodes +
                '}';
    }
}
