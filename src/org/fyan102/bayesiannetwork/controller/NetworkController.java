package org.fyan102.bayesiannetwork.controller;

import org.fyan102.bayesiannetwork.model.Network;
import org.fyan102.bayesiannetwork.model.Node;
import org.fyan102.bayesiannetwork.ui.NetworkView;
import org.fyan102.bayesiannetwork.util.NetworkFileHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller class that coordinates between the Network model and NetworkView
 */
public class NetworkController {
    private final Network network;
    private final NetworkView networkView;
    private final NetworkFileHandler fileHandler;

    public NetworkController() {
        this.network = new Network();
        this.networkView = new NetworkView(network);
        this.fileHandler = new NetworkFileHandler();
    }

    public NetworkView getNetworkView() {
        return networkView;
    }

    public boolean addNode(Node node) {
        if (network.addNode(node)) {
            networkView.addNode(node);
            return true;
        }
        return false;
    }

    public boolean removeNode(Node node) {
        if (network.removeNode(node)) {
            networkView.removeNode(node);
            return true;
        }
        return false;
    }

    public boolean addEdge(Node from, Node to) {
        if (network.addEdge(from, to)) {
            networkView.addLink(from, to);
            return true;
        }
        return false;
    }

    public boolean removeEdge(Node from, Node to) {
        if (network.removeEdge(from, to)) {
            networkView.removeLink(from, to);
            return true;
        }
        return false;
    }

    public boolean calculate() {
        if (network.calculate()) {
            networkView.updateLinks();
            return true;
        }
        return false;
    }

    public boolean saveNetwork(File file) {
        try {
            NetworkFileHandler.saveNetwork(networkView, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadNetwork(File file) {
        try {
            NetworkFileHandler.loadNetwork(networkView, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Node> getNodes() {
        return List.copyOf(network.getNodes());
    }

    public void clear() {
        network.getNodes().forEach(this::removeNode);
    }
} 