package co.turtlegames.core.db;

import co.turtlegames.core.util.UtilDev;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseConnector {

    private JavaPlugin _pluginInstance;

    private HikariDataSource _conPool;
    private Executor _defaultExecutor;

    public DatabaseConnector(JavaPlugin pluginInstance, Properties poolProperties) {

        _pluginInstance = pluginInstance;

        _conPool = new HikariDataSource();

        // VV default values VV
        _conPool.setMaximumPoolSize(15);
        _conPool.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        _conPool.setDataSourceProperties(poolProperties);

        _defaultExecutor = Executors.newCachedThreadPool();

    }

    public void deinitialize() {

        _conPool.close();

    }

    public <I> I executeActionSync(IDatabaseAction<I> action) throws SQLException,DatabaseException {

        Connection con = _conPool.getConnection();
        I data = (I) action.executeAction(con);

        con.close();
        return data;

    }

    @SuppressWarnings("all")
    public <I> CompletableFuture<I> executeActionAsync(IDatabaseAction<I> action, boolean preserveAsync) {

        CompletableFuture<I> toComplete = new CompletableFuture<I>();

        long[] timeStamp = new long[5];
        timeStamp[0] = System.currentTimeMillis();

        Runnable dbRunnable = () -> {

            timeStamp[1] = System.currentTimeMillis();

            Connection con = null;

            try {

                con = _conPool.getConnection();
                timeStamp[2] = System.currentTimeMillis();

                I response = action.executeAction(con);

                timeStamp[3] = System.currentTimeMillis();

                con.close();
                con = null;

                if(preserveAsync) {

                    toComplete.complete(response);
                    timeStamp[4] = System.currentTimeMillis();

                    this.handleTimestamp(timeStamp);

                } else
                    Bukkit.getScheduler().runTask(_pluginInstance, () -> {

                        toComplete.complete(response);
                        timeStamp[4] = System.currentTimeMillis();

                        this.handleTimestamp(timeStamp);

                    });

            } catch(SQLException | DatabaseException ex) {

                if(!(ex instanceof DatabaseException))
                    ex.printStackTrace();

                if(preserveAsync)
                    toComplete.completeExceptionally(ex);
                else
                    Bukkit.getScheduler().runTask(_pluginInstance, () -> {
                        toComplete.completeExceptionally(ex);
                    });

            } finally {

                try {
                    if(con != null)
                        con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace(); // fellas we have a problem
                }

            }


        };

        _defaultExecutor.execute(dbRunnable);
        return toComplete;

    }

    public <I> CompletableFuture<I> executeActionAsync(IDatabaseAction<I> action) {
        return this.executeActionAsync(action, false);
    }

    private void handleTimestamp(long[] times) {

        long timeToThread = times[1] - times[0];
        long timeToCon = times[2] - times[1];
        long timeToExecute = times[3] - times[2];

        long timeToReturn = times[4] - times[3];

        if(timeToThread > 3000)
            UtilDev.alert(UtilDev.AlertLevel.WARN, "DatabaseConnector took a long time (" + timeToThread + "ms) to pull thread from thread pool");
        if(timeToCon > 3000)
            UtilDev.alert(UtilDev.AlertLevel.WARN, "DatabaseConnector took a long time (" + timeToCon + "ms) to pull connection from hikaricp");
        if(timeToExecute > 3000)
            UtilDev.alert(UtilDev.AlertLevel.WARN, "DatabaseConnector took a long time (" + timeToExecute + "ms) to execute db action");
        if(timeToReturn > 3000)
            UtilDev.alert(UtilDev.AlertLevel.WARN, "DatabaseConnector took a long time (" + timeToReturn + "ms) to fork main thread");

    }


}
