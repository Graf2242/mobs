package utils.MessageSystem.messages.Metrics;

import base.masterService.nodes.Address;
import base.masterService.nodes.Node;
import base.utils.MetricsService;

public class MetricsIncrement extends _MetricMessageTemplate {
    private String metric;
    private String help = "No help is done here";

    public MetricsIncrement(Address from, String metric) {
        super(from);
        this.metric = metric;
    }

    public MetricsIncrement(Address from, String metric, String help) {
        super(from);
        this.metric = metric;
        this.help = help;
        System.out.println("Metric increment " + metric + help);
    }

    @Override
    public void exec(Node node) {
        MetricsService metricsService = (MetricsService) node;
        metricsService.getLogger().info(String.format("Metrics increment message exec, name %s, help %s", metric, help));
        metricsService.incrementGauge(metric, help);
    }
}
