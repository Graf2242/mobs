package utils.MessageSystem.messages.Metrics;

import base.masterService.nodes.Address;
import base.utils.Message;
import base.utils.MetricsService;

public abstract class _MetricMessageTemplate extends Message {
    public _MetricMessageTemplate(Address from) {
        super(from, MetricsService.class);
    }
}
