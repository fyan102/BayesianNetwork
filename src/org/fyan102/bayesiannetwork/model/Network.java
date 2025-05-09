package org.fyan102.bayesiannetwork.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        String lastResult = "";
        boolean changed = true;
        
        while (changed && i < nodes.size() + 1) {
            changed = false;
            for (Node node : nodes) {
                node.updateBelieves();
            }
            String result = toString();
            if (!lastResult.equals(result)) {
                lastResult = result;
                changed = true;
            }
            i++;
        }
        
        return i <= nodes.size();
    }

    /**
     * Check if adding a link would create a cycle in the network
     * @param from The source node
     * @param to The target node
     * @return true if adding the link would create a cycle
     */
    public boolean wouldCreateCycle(Node from, Node to) {
        if (from == to) {
            return true;
        }
        
        // Use DFS to detect cycles
        Set<Node> visited = new HashSet<>();
        Set<Node> recursionStack = new HashSet<>();
        
        return hasCycle(to, visited, recursionStack);
    }
    
    private boolean hasCycle(Node node, Set<Node> visited, Set<Node> recursionStack) {
        if (recursionStack.contains(node)) {
            return true;
        }
        
        if (visited.contains(node)) {
            return false;
        }
        
        visited.add(node);
        recursionStack.add(node);
        
        for (Node child : node.getChildren()) {
            if (hasCycle(child, visited, recursionStack)) {
                return true;
            }
        }
        
        recursionStack.remove(node);
        return false;
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
