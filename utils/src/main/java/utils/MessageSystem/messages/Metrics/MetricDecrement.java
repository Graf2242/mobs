package utils.MessageSystem.messages.Metrics;

import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import base.utils.MetricsService;

public class MetricDecrement extends _MetricMessageTemplate {
    private String metric;
    private String help = "";

    public MetricDecrement(Address from, String metric) {
        super(from);
    }

    public MetricDecrement(Address from, String metric, String help) {
        super(from);
        this.metric = metric;
        this.help = help;
    }

    @Override
    public void exec(Node node) {
        MetricsService metricsService = (MetricsService) node;
        metricsService.decrementGauge(metric, help);
        metricsService.getLogger().info("Metrics decrement message exec");
    }
}
