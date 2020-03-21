package co.turtlegames.core.db;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class DatabaseConnector {

    private JavaPlugin _pluginInstance;

    private HikariDataSource _conPool;

    public DatabaseConnector(JavaPlugin pluginInstance, Properties poolProperties) {

        _pluginInstance = pluginInstance;

        _conPool = new HikariDataSource();

        // VV default values VV
        _conPool.setMaximumPoolSize(1);
        _conPool.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        _conPool.setDataSourceProperties(poolProperties);

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

        Bukkit.getScheduler().runTaskAsynchronously(_pluginInstance, () -> {

            Connection con = null;

            try {

                con = _conPool.getConnection();

                I response = action.executeAction(con);

                con.close();
                con = null;

                if(preserveAsync)
                    toComplete.complete(response);
                else
                    Bukkit.getScheduler().runTask(_pluginInstance, () -> {
                        toComplete.complete(response);
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


        });

        return toComplete;

    }

    public <I> CompletableFuture<I> executeActionAsync(IDatabaseAction<I> action) {
        return this.executeActionAsync(action, false);
    }


}
