package co.turtlegames.core.metrics;

import co.turtlegames.core.TurtleModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

public class MetricManager extends TurtleModule {

    private InfluxDB _database;

    public MetricManager(JavaPlugin plugin) {
        super(plugin, "Metric Manager");
    }


    @Override
    public void initializeModule() {

        _database = InfluxDBFactory.connect("http://vmi360387.contaboserver.net:8086");
        _database.setDatabase("turtle");
        _database.setRetentionPolicy("turtle");

    }

    public void register(Metric metric) {

        Point.Builder builder = Point.measurement(metric.getType());

        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        metric.applyFields(builder);

        _database.write(builder.build());

    }

    @Override
    public void deinitializeModule() {
        _database.close();
    }
}
