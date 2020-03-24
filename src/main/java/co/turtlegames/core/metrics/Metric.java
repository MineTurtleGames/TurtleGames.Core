package co.turtlegames.core.metrics;

import org.influxdb.dto.Point;

public abstract class Metric {

    private String _type;

    public Metric(String type) {
        _type = type;
    }

    public abstract void applyFields(Point.Builder builder);

    public String getType() {
        return _type;
    }

}
