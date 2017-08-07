package scot.mygov.housing;

import com.codahale.metrics.MetricRegistry;

public enum MetricName {

    RESPONSE_TIMES("response-times"),
    REQUESTS("requests"),
    ERRORS("errors"),
    REQUEST_RATE("request-rate"),
    ERROR_RATE("error-rate");

    private final String metricName;

    private MetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricName() {
        return metricName;
    }

    public String name(Object o) {
        return MetricRegistry.name(o.getClass(), metricName);
    }
}
