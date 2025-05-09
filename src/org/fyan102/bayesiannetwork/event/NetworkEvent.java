package org.fyan102.bayesiannetwork.event;

import org.fyan102.bayesiannetwork.model.Node;
import java.util.EventObject;

public class NetworkEvent extends EventObject {
    public enum Type {
        NODE_ADDED,
        NODE_REMOVED,
        EDGE_ADDED,
        EDGE_REMOVED,
        NETWORK_CLEARED,
        NETWORK_LOADED,
        NETWORK_SAVED,
        BELIEFS_UPDATED
    }

    private final Type type;
    private final Node source;
    private final Node target;

    public NetworkEvent(Object source, Type type) {
        this(source, type, null, null);
    }

    public NetworkEvent(Object source, Type type, Node sourceNode) {
        this(source, type, sourceNode, null);
    }

    public NetworkEvent(Object source, Type type, Node sourceNode, Node targetNode) {
        super(source);
        this.type = type;
        this.source = sourceNode;
        this.target = targetNode;
    }

    public Type getType() {
        return type;
    }

    public Node getSourceNode() {
        return source;
    }

    public Node getTargetNode() {
        return target;
    }
} 