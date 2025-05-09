package org.fyan102.bayesiannetwork.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a node in the Bayesian Network
 */
public class Node {
    private String name;
    private List<String> states;
    private List<Double> beliefs;
    private List<List<Double>> probabilities;
    private List<Node> parents;
    private List<Node> children;

    public Node() {
        this("NewNode");
    }

    public Node(String name) {
        this.name = name;
        this.states = new ArrayList<>();
        this.beliefs = new ArrayList<>();
        this.probabilities = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    // Getters and setters with proper encapsulation
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public List<String> getStates() {
        return new ArrayList<>(states);
    }

    public void setStates(List<String> states) {
        this.states = new ArrayList<>(Objects.requireNonNull(states, "States cannot be null"));
    }

    public List<Double> getBeliefs() {
        return new ArrayList<>(beliefs);
    }

    public void setBeliefs(List<Double> beliefs) {
        this.beliefs = new ArrayList<>(Objects.requireNonNull(beliefs, "Beliefs cannot be null"));
    }

    public List<List<Double>> getProbabilities() {
        return new ArrayList<>(probabilities);
    }

    public void setProbabilities(List<List<Double>> probabilities) {
        this.probabilities = new ArrayList<>(Objects.requireNonNull(probabilities, "Probabilities cannot be null"));
    }

    public List<Node> getParents() {
        return new ArrayList<>(parents);
    }

    public List<Node> getChildren() {
        return new ArrayList<>(children);
    }

    // Business logic methods
    public boolean addParent(Node parent) {
        if (parent == null || parent == this || parents.contains(parent)) {
            return false;
        }
        
        if (wouldCreateCycle(parent)) {
            return false;
        }

        parents.add(parent);
        parent.addChild(this);
        updateProbabilities();
        return true;
    }

    public boolean removeParent(Node parent) {
        if (parent == null || !parents.contains(parent)) {
            return false;
        }

        parents.remove(parent);
        parent.removeChild(this);
        updateProbabilities();
        return true;
    }

    private void addChild(Node child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
        }
    }

    public void removeChild(Node child) {
        children.remove(child);
    }

    private boolean wouldCreateCycle(Node potentialParent) {
        return potentialParent == this || 
               potentialParent.getParents().stream()
                   .anyMatch(parent -> parent == this || wouldCreateCycle(parent));
    }

    public void updateBeliefs() {
        if (parents.isEmpty()) {
            return;
        }

        List<Double> newBeliefs = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            double belief = calculateBelief(i);
            newBeliefs.add(belief);
        }
        beliefs = newBeliefs;
    }

    private double calculateBelief(int stateIndex) {
        double belief = 0.0;
        for (List<Double> probabilityRow : probabilities) {
            belief += probabilityRow.get(stateIndex);
        }
        return belief;
    }

    private void updateProbabilities() {
        // Implementation for updating probabilities based on parent states
        // This is a placeholder - implement based on your specific requirements
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
