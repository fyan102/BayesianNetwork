package org.fyan102.bayesiannetwork.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.fyan102.bayesiannetwork.model.NetworkData;
import org.fyan102.bayesiannetwork.model.Node;
import org.fyan102.bayesiannetwork.ui.NodeView;
import org.fyan102.bayesiannetwork.ui.NetworkView;
import org.fyan102.bayesiannetwork.ui.Link;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkFileHandler {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveNetwork(NetworkView networkView, File file) throws IOException {
        NetworkData networkData = new NetworkData();
        
        // Convert nodes
        List<NetworkData.NodeData> nodeDataList = new ArrayList<>();
        for (NodeView nodeView : networkView.getNodes()) {
            Node node = nodeView.getNode();
            NetworkData.NodeData nodeData = new NetworkData.NodeData();
            
            // Set basic node information
            nodeData.setName(node.getName());
            nodeData.setStates(new ArrayList<>(node.getStates()));
            
            // Convert beliefs to array
            double[] beliefs = new double[node.getBeliefs().size()];
            for (int i = 0; i < beliefs.length; i++) {
                beliefs[i] = node.getBeliefs().get(i);
            }
            nodeData.setBeliefs(beliefs);
            
            // Convert conditional probabilities to 2D array
            List<ArrayList<Double>> probs = node.getProbs();
            double[][] condProbs = new double[probs.size()][];
            for (int i = 0; i < probs.size(); i++) {
                List<Double> row = probs.get(i);
                condProbs[i] = new double[row.size()];
                for (int j = 0; j < row.size(); j++) {
                    condProbs[i][j] = row.get(j);
                }
            }
            nodeData.setConditionalProbabilities(condProbs);
            
            // Set node position
            Point location = nodeView.getLocation();
            nodeData.setX(location.x);
            nodeData.setY(location.y);
            
            // Set parent information
            List<String> parentNames = new ArrayList<>();
            for (Node parent : node.getParents()) {
                parentNames.add(parent.getName());
            }
            nodeData.setParentNames(parentNames);
            
            nodeDataList.add(nodeData);
        }
        networkData.setNodes(nodeDataList);
        
        // Convert links
        List<NetworkData.LinkData> linkDataList = new ArrayList<>();
        for (NodeView nodeView : networkView.getNodes()) {
            Node node = nodeView.getNode();
            for (Node parent : node.getParents()) {
                NetworkData.LinkData linkData = new NetworkData.LinkData();
                linkData.setFromNode(parent.getName());
                linkData.setToNode(node.getName());
                
                // Create a simple straight line between nodes
                NodeView parentView = findNodeView(networkView, parent);
                if (parentView != null) {
                    // Calculate connection points on the edges of the nodes
                    Point from = calculateConnectionPoint(parentView, nodeView);
                    Point to = calculateConnectionPoint(nodeView, parentView);
                    
                    linkData.getPoints().add(new NetworkData.PointData(from.x, from.y));
                    linkData.getPoints().add(new NetworkData.PointData(to.x, to.y));
                }
                
                linkDataList.add(linkData);
            }
        }
        networkData.setLinks(linkDataList);
        
        // Write to file
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(networkData, writer);
        }
    }

    private static NodeView findNodeView(NetworkView networkView, Node node) {
        for (NodeView nodeView : networkView.getNodes()) {
            if (nodeView.getNode() == node) {
                return nodeView;
            }
        }
        return null;
    }

    private static Point calculateConnectionPoint(NodeView from, NodeView to) {
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

    public static void loadNetwork(NetworkView networkView, File file) throws IOException {
        // Read and parse the JSON file
        NetworkData networkData;
        try (FileReader reader = new FileReader(file)) {
            networkData = gson.fromJson(reader, NetworkData.class);
        }

        // Clear existing network
        networkView.getNodes().clear();
        networkView.getLinks().clear();
        networkView.removeAll();

        // Create a map to store nodes by name for easy lookup
        Map<String, Node> nodeMap = new HashMap<>();
        Map<String, NodeView> nodeViewMap = new HashMap<>();

        // First pass: Create all nodes
        for (NetworkData.NodeData nodeData : networkData.getNodes()) {
            Node node = new Node(nodeData.getName());
            node.setStates(new ArrayList<>(nodeData.getStates()));
            
            // Convert beliefs array to ArrayList
            ArrayList<Double> beliefs = new ArrayList<>();
            for (double belief : nodeData.getBeliefs()) {
                beliefs.add(belief);
            }
            node.setBeliefs(beliefs);
            
            // Convert conditional probabilities 2D array to ArrayList<ArrayList<Double>>
            ArrayList<ArrayList<Double>> condProbs = new ArrayList<>();
            for (double[] row : nodeData.getConditionalProbabilities()) {
                ArrayList<Double> probRow = new ArrayList<>();
                for (double prob : row) {
                    probRow.add(prob);
                }
                condProbs.add(probRow);
            }
            node.setProbs(condProbs);

            NodeView nodeView = new NodeView(node);
            nodeView.setLocation(nodeData.getX(), nodeData.getY());
            
            nodeMap.put(node.getName(), node);
            nodeViewMap.put(node.getName(), nodeView);
            
            networkView.addNode(nodeView);
        }

        // Second pass: Set up parent relationships
        for (NetworkData.NodeData nodeData : networkData.getNodes()) {
            Node node = nodeMap.get(nodeData.getName());
            for (String parentName : nodeData.getParentNames()) {
                Node parent = nodeMap.get(parentName);
                if (parent != null) {
                    node.addParent(parent);
                }
            }
        }

        // Create links
        for (NetworkData.LinkData linkData : networkData.getLinks()) {
            Link link = new Link();
            for (NetworkData.PointData pointData : linkData.getPoints()) {
                link.addPoint(new Point(pointData.getX(), pointData.getY()));
            }
            networkView.getLinks().add(link);
        }

        // Refresh the view
        networkView.repaint();
    }
} 