package org.fyan102.bayesiannetwork.model;

import java.util.ArrayList;
import java.util.List;

public class NetworkData {
    private List<NodeData> nodes;
    private List<LinkData> links;

    public NetworkData() {
        this.nodes = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    public List<NodeData> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeData> nodes) {
        this.nodes = nodes;
    }

    public List<LinkData> getLinks() {
        return links;
    }

    public void setLinks(List<LinkData> links) {
        this.links = links;
    }

    public static class NodeData {
        private String name;
        private List<String> states;
        private double[] beliefs;
        private double[][] conditionalProbabilities;
        private int x;
        private int y;
        private List<String> parentNames;

        public NodeData() {
            this.states = new ArrayList<>();
            this.parentNames = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getStates() {
            return states;
        }

        public void setStates(List<String> states) {
            this.states = states;
        }

        public double[] getBeliefs() {
            return beliefs;
        }

        public void setBeliefs(double[] beliefs) {
            this.beliefs = beliefs;
        }

        public double[][] getConditionalProbabilities() {
            return conditionalProbabilities;
        }

        public void setConditionalProbabilities(double[][] conditionalProbabilities) {
            this.conditionalProbabilities = conditionalProbabilities;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public List<String> getParentNames() {
            return parentNames;
        }

        public void setParentNames(List<String> parentNames) {
            this.parentNames = parentNames;
        }
    }

    public static class LinkData {
        private String fromNode;
        private String toNode;
        private List<PointData> points;

        public LinkData() {
            this.points = new ArrayList<>();
        }

        public String getFromNode() {
            return fromNode;
        }

        public void setFromNode(String fromNode) {
            this.fromNode = fromNode;
        }

        public String getToNode() {
            return toNode;
        }

        public void setToNode(String toNode) {
            this.toNode = toNode;
        }

        public List<PointData> getPoints() {
            return points;
        }

        public void setPoints(List<PointData> points) {
            this.points = points;
        }
    }

    public static class PointData {
        private int x;
        private int y;

        public PointData() {}

        public PointData(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
} 