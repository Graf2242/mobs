package base.utils;

import base.masterService.nodes.Node;

public interface MetricsService extends Runnable, Node {

    static MetricsService instance(int port) {
        return null;
    }

    void incrementCounter(String name, String help);

    void incrementCounter(String name, String Help, double amt);

    void incrementGauge(String name, String help);

    void incrementGauge(String name, String Help, double amt);

    void decrementGauge(String name, String help);

    void decrementGauge(String name, String Help, double amt);

    org.apache.logging.log4j.Logger getLogger();
}
