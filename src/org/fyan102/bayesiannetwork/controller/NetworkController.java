package org.fyan102.bayesiannetwork.controller;

import org.fyan102.bayesiannetwork.model.Network;
import org.fyan102.bayesiannetwork.model.Node;
import org.fyan102.bayesiannetwork.ui.NetworkView;
import org.fyan102.bayesiannetwork.util.NetworkFileHandler;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Controller class that coordinates between the Network model and NetworkView
 */
public class NetworkController {
    private final Network network;
    private final NetworkView view;
    private final NetworkFileHandler fileHandler;

    public NetworkController(Network network, NetworkView view) {
        this.network = network;
        this.view = view;
        this.fileHandler = new NetworkFileHandler();
    }

    public boolean addNode(String name) {
        Node node = new Node(name);
        if (network.addNode(node)) {
            view.addNode(node);
            return true;
        }
        return false;
    }

    public boolean removeNode(Node node) {
        if (network.removeNode(node)) {
            view.removeNode(node);
            return true;
        }
        return false;
    }

    public boolean addEdge(Node from, Node to) {
        if (network.addEdge(from, to)) {
            view.addEdge(from, to);
            return true;
        }
        return false;
    }

    public boolean removeEdge(Node from, Node to) {
        if (network.removeEdge(from, to)) {
            view.removeEdge(from, to);
            return true;
        }
        return false;
    }

    public boolean calculate() {
        boolean success = network.calculate();
        if (success) {
            view.refresh();
        }
        return success;
    }

    public boolean saveToFile(File file) {
        return fileHandler.saveNetwork(network, file);
    }

    public Optional<Network> loadFromFile(File file) {
        return fileHandler.loadNetwork(file);
    }

    public List<Node> getNodes() {
        return List.copyOf(network.getNodes());
    }

    public void clear() {
        network.getNodes().forEach(this::removeNode);
    }
} 