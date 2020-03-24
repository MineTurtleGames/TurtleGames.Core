package co.turtlegames.core.metrics.type;

import co.turtlegames.core.metrics.Metric;
import org.influxdb.dto.Point;

public class ServerStatusMetric extends Metric {

    private int _delta;

    public ServerStatusMetric(int delta) {
        super("SERVER_STATUS");

        _delta = delta;
    }

    public void applyFields(Point.Builder builder) {

        builder.addField("Delta", _delta);

    }

}
