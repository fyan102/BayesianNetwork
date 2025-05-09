package org.fyan102.bayesiannetwork.model;

import java.util.ArrayList;

/**
 * The node class
 */
public class Node {
    private String name;
    private ArrayList<String> states;
    private ArrayList<Double> beliefs;
    private ArrayList<ArrayList<Double>> probs;
    private ArrayList<Node> parents;
    private ArrayList<Node> children;

    /**
     * Constructor of Node class. The default name is "NewNode"
     */
    public Node() {
        setName("NewNode");
        setStates(new ArrayList<>());
        setBeliefs(new ArrayList<>());
        setProbs(new ArrayList<>());
        setParents(new ArrayList<>());
        setChildren(new ArrayList<>());
    }

    /**
     * Constructor of Node class. It will set a name for the node
     *
     * @param name the name of the node
     */
    public Node(String name) {
        setName(name);
        setStates(new ArrayList<>());
        setBeliefs(new ArrayList<>());
        setProbs(new ArrayList<>());
        setParents(new ArrayList<>());
        setChildren(new ArrayList<>());
    }

    /**
     * Add a parent to the node
     *
     * @param parent a parent node that is to be added to this node
     * @return true if added successfully, false otherwise
     */
    public boolean addParent(Node parent) {
        if (!parents.contains(parent) && parent != this) {
            parents.add(parent);
            parent.addChild(this);
            convertToConditionalProbabilities();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Add a state to the node
     *
     * @param state the new state to be added
     * @return true if added successfully, false otherwise
     */
    public boolean addState(String state) {
        state = state.trim();
        if (!states.contains(state) && state.length() > 0) {
            states.add(state);
            beliefs.add(0.0);
            for (ArrayList<Double> prob : probs) {
                prob.add(0.0);
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * remove a parent node
     *
     * @param parent the parent node to be removed
     * @return true if successful, false otherwise
     */
    public boolean deleteParent(Node parent) {
        int index = parents.indexOf(parent);
        if (index >= 0) {
            parents.remove(index);
            parent.removeChild(this);
            convertToSimpleProbabilities();
            return true;
        }
        return false;
    }

    /**
     * remove a state
     *
     * @param state the state to be removed
     * @return true if successful, false otherwise
     */
    public boolean deleteState(String state) {
        int index = states.indexOf(state);
        if (index >= 0) {
            states.remove(index);
            beliefs.remove(index);
            for (ArrayList<Double> prob : probs) {
                prob.remove(index);
            }
            return true;
        }
        return false;
    }

    /**
     * get the value of belief at index
     *
     * @param index the index of value of belief
     * @return the value of belief
     */
    public double getBelief(int index) {
        return beliefs.get(index);
    }

    /**
     * get the value of belief of one state
     *
     * @param state the state of the belief
     * @return the value of belief
     */
    public double getBelief(String state) {
        int index = states.indexOf(state);
        return index >= 0 && index < states.size() ? beliefs.get(index) : 0;
    }

    /**
     * Accessor method for beliefs
     *
     * @return the value of beliefs
     */
    public ArrayList<Double> getBeliefs() {
        return beliefs;
    }

    /**
     * Accessor method for name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the number of parents
     *
     * @return the number of parents
     */
    public int getNumberOfParents() {
        return parents.size();
    }

    /**
     * Return the number of states
     *
     * @return the number of states
     */
    public int getNumberOfStates() {
        return states.size();
    }

    /**
     * get a parent according to index
     *
     * @param index the index of the parent
     * @return the parent
     */
    public Node getParent(int index) {
        return parents.get(index);
    }

    /**
     * get all parents
     *
     * @return all parents
     */
    public ArrayList<Node> getParents() {
        return parents;
    }

    /**
     * get a permutation
     *
     * @param n the level of permutation
     * @return a permutation
     */
    private ArrayList<ArrayList<Integer>> getPermutation(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        else if (n == 1) {
            ArrayList<ArrayList<Integer>> results = new ArrayList<>();

            for (int i = 0; i < parents.get(n - 1).getStates().size(); i++) {
                ArrayList<Integer> states = new ArrayList<Integer>();
                states.add(i);
                results.add(states);
            }
            return results;
        }
        else {
            ArrayList<ArrayList<Integer>> lastResult = getPermutation(n - 1);
            ArrayList<ArrayList<Integer>> results = new ArrayList<>();
            for (ArrayList<Integer> integers : lastResult) {
                for (int j = 0; j < parents.get(n - 1).states.size(); j++) {
                    ArrayList<Integer> newStates = new ArrayList<>(integers);
                    newStates.add(j);
                    results.add(newStates);
                }
            }
            return results;
        }
    }

    /**
     * get all conditional probabilities
     *
     * @return all conditional probabilities
     */
    public ArrayList<ArrayList<Double>> getProbs() {
        return probs;
    }

    /**
     * get a state at index
     *
     * @param index the index of the state
     * @return the state at index
     */
    public String getState(int index) {
        if (index >= 0 && index < states.size()) {
            return states.get(index);
        }
        return "";
    }

    /**
     * get all states
     *
     * @return all states
     */
    public ArrayList<String> getStates() {
        return states;
    }

    /**
     * change the size of conditional probabilities table
     */
    public void resizeProbs() {
        int size = 1;
        probs = new ArrayList<ArrayList<Double>>();
        for (Node parent : parents) {
            size *= parent.states.size();
        }
        for (int i = 0; i < size; i++) {
            ArrayList<Double> probList = new ArrayList<Double>();
            for (int j = 0; j < states.size(); j++) {
                probList.add(0.0);
            }
            probs.add(probList);
        }
    }

    /**
     * mutator of beliefs
     *
     * @param beliefs a new table of beliefs
     */
    public void setBeliefs(ArrayList<Double> beliefs) {
        this.beliefs = beliefs;
    }

    /**
     * change the value of beliefs using an array
     *
     * @param beliefs an array of beliefs
     */
    public void setBeliefs(double[] beliefs) {
        for (int i = 0; i < beliefs.length; i++) {
            System.out.println("Belief "+i+" is "+beliefs[i]);
            if (this.beliefs.size() > i) {
                this.beliefs.set(i, beliefs[i]);
            }
            else {
                this.beliefs.add(beliefs[i]);
            }
        }
    }

    /**
     * change the name
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * change a parent at index
     *
     * @param index  the index of parent
     * @param parent the new parent
     */
    public void setParent(int index, Node parent) {
        parents.set(index, parent);
    }

    /**
     * mutator for the parent list
     *
     * @param parents a new parent list
     */
    public void setParents(ArrayList<Node> parents) {
        this.parents = parents;
    }

    /**
     * change one conditional probabilities
     *
     * @param row    the row
     * @param column the column
     * @param value  the new value
     */
    public void setProb(int row, int column, double value) {
        probs.get(row).set(column, value);
    }

    /**
     * mutator of conditional probabilities
     *
     * @param probs new probabilities
     */
    public void setProbs(ArrayList<ArrayList<Double>> probs) {
        this.probs = probs;
    }

    /**
     * change the conditional probabilities using an array of array
     *
     * @param probsArray the new conditional probabilities
     */
    public void setProbs(double[][] probsArray) {
        for (int i = 0; i < probsArray.length; i++) {
            for (int j = 0; j < probsArray[0].length; j++) {
                setProb(i, j, probsArray[i][j]);
            }
        }
    }

    /**
     * change the state at index
     *
     * @param index the position
     * @param state the new state
     * @return true if successful, false otherwise
     */
    public boolean setState(int index, String state) {
        state = state.trim();
        if (index >= 0 && index < states.size() && state.length() > 0) {
            states.set(index, state);
            return true;
        }
        return false;
    }

    /**
     * the mutator for states
     *
     * @param states a list of new states
     */
    public void setStates(ArrayList<String> states) {
        this.states = states;
    }

    /**
     * the toString method
     *
     * @return a string
     */
    @Override
    public String toString() {
        StringBuffer parentsString = new StringBuffer("[");
        parents.forEach((p) -> parentsString.append(p.name).append(", "));
        if (parents.size() > 0) {
            parentsString.delete(parentsString.length() - 2, parentsString.length());
        }
        parentsString.append("]");
        return "Node " + name + " {" +
                "states=" + states +
                ", beliefs=" + beliefs +
                ", probs=" + probs +
                ", parents=" + parentsString +
                '}';
    }

    /**
     * update the table of believes
     */
    public void updateBelieves() {
        if (parents.isEmpty()) {
            // For nodes without parents, beliefs are already set
            return;
        }

        ArrayList<ArrayList<Integer>> indices = getPermutation(getParents().size());
        for (int i = 0; i < states.size(); i++) { // ith state of this node
            double belief = 0;
            for (int j = 0; j < indices.size(); j++) { // jth permutation
                double prob = probs.get(j).get(i);
                double parentProb = 1.0;
                
                for (int k = 0; k < indices.get(j).size(); k++) { // kth parent
                    Node parent = getParent(k);
                    int parentStateIndex = indices.get(j).get(k);
                    parentProb *= parent.getBelief(parentStateIndex);
                }
                
                belief += prob * parentProb;
            }
            beliefs.set(i, belief);
        }
    }

    private void convertToConditionalProbabilities() {
        if (parents.isEmpty()) {
            return;
        }

        // Calculate number of combinations
        int combinations = 1;
        for (Node parent : parents) {
            combinations *= parent.getNumberOfStates();
        }

        // Create new probability table
        ArrayList<ArrayList<Double>> newProbs = new ArrayList<>();
        for (int i = 0; i < combinations; i++) {
            ArrayList<Double> row = new ArrayList<>();
            for (int j = 0; j < states.size(); j++) {
                // Initialize with uniform distribution
                row.add(1.0 / states.size());
            }
            newProbs.add(row);
        }

        // If we had simple probabilities, distribute them
        if (!probs.isEmpty() && probs.get(0).size() == states.size()) {
            ArrayList<Double> oldProbs = probs.get(0);
            for (int i = 0; i < combinations; i++) {
                newProbs.set(i, new ArrayList<>(oldProbs));
            }
        }

        probs = newProbs;
    }

    private void convertToSimpleProbabilities() {
        if (!parents.isEmpty()) {
            return;
        }

        // Convert to simple probabilities by averaging
        ArrayList<Double> simpleProbs = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            double sum = 0;
            for (ArrayList<Double> row : probs) {
                sum += row.get(i);
            }
            simpleProbs.add(sum / probs.size());
        }

        probs.clear();
        probs.add(simpleProbs);
    }

    public void addChild(Node child) {
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    public void removeChild(Node child) {
        children.remove(child);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

}
