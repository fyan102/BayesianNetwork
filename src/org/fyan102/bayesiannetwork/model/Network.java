package org.fyan102.bayesiannetwork.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a Bayesian Network
 */
public class Network {
    private final Set<Node> nodes;
    private final Map<Node, Set<Node>> adjacencyList;

    public Network() {
        this.nodes = new HashSet<>();
        this.adjacencyList = new HashMap<>();
    }

    public Network(Collection<Node> nodes) {
        this();
        nodes.forEach(this::addNode);
    }

    public boolean addNode(Node node) {
        if (node == null) {
            return false;
        }
        
        if (nodes.add(node)) {
            adjacencyList.put(node, new HashSet<>());
            return true;
        }
        return false;
    }

    public boolean removeNode(Node node) {
        if (node == null || !nodes.contains(node)) {
            return false;
        }

        // Remove all connections
        for (Node parent : node.getParents()) {
            parent.removeChild(node);
        }
        for (Node child : node.getChildren()) {
            child.removeParent(node);
        }

        nodes.remove(node);
        adjacencyList.remove(node);
        return true;
    }

    public boolean addEdge(Node from, Node to) {
        if (from == null || to == null || !nodes.contains(from) || !nodes.contains(to)) {
            return false;
        }

        if (wouldCreateCycle(from, to)) {
            return false;
        }

        if (to.addParent(from)) {
            adjacencyList.get(from).add(to);
            return true;
        }
        return false;
    }

    public boolean removeEdge(Node from, Node to) {
        if (from == null || to == null || !nodes.contains(from) || !nodes.contains(to)) {
            return false;
        }

        if (to.removeParent(from)) {
            adjacencyList.get(from).remove(to);
            return true;
        }
        return false;
    }

    public Set<Node> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public Set<Node> getChildren(Node node) {
        return Collections.unmodifiableSet(adjacencyList.getOrDefault(node, Collections.emptySet()));
    }

    public boolean calculate() {
        if (nodes.isEmpty()) {
            return true;
        }

        // Topological sort to ensure we update nodes in the correct order
        List<Node> sortedNodes = topologicalSort();
        if (sortedNodes == null) {
            return false; // Cycle detected
        }

        // Update beliefs in topological order
        for (Node node : sortedNodes) {
            node.updateBeliefs();
        }

        return true;
    }

    private List<Node> topologicalSort() {
        Set<Node> visited = new HashSet<>();
        Set<Node> recursionStack = new HashSet<>();
        List<Node> result = new ArrayList<>();

        for (Node node : nodes) {
            if (!visited.contains(node)) {
                if (!topologicalSortUtil(node, visited, recursionStack, result)) {
                    return null; // Cycle detected
                }
            }
        }

        Collections.reverse(result);
        return result;
    }

    private boolean topologicalSortUtil(Node node, Set<Node> visited, Set<Node> recursionStack, List<Node> result) {
        if (recursionStack.contains(node)) {
            return false; // Cycle detected
        }

        if (visited.contains(node)) {
            return true;
        }

        visited.add(node);
        recursionStack.add(node);

        for (Node child : getChildren(node)) {
            if (!topologicalSortUtil(child, visited, recursionStack, result)) {
                return false;
            }
        }

        recursionStack.remove(node);
        result.add(node);
        return true;
    }

    private boolean wouldCreateCycle(Node from, Node to) {
        if (from == to) {
            return true;
        }

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

        for (Node child : getChildren(node)) {
            if (hasCycle(child, visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }

    @Override
    public String toString() {
        return nodes.stream()
                .map(Node::toString)
                .collect(Collectors.joining("\n"));
    }
}
