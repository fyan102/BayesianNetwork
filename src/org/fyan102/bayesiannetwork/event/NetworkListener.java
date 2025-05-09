package org.fyan102.bayesiannetwork.event;

import java.util.EventListener;

public interface NetworkListener extends EventListener {
    void networkChanged(NetworkEvent event);
} 