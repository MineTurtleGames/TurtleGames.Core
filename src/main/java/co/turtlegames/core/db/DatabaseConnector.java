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
    public <I> CompletableFuture<I> executeActionAsync(IDatabaseAction action) {

        CompletableFuture<I> toComplete = new CompletableFuture<I>();

        Bukkit.getScheduler().runTaskAsynchronously(_pluginInstance, () -> {

            Connection con;
            try {
                con = _conPool.getConnection();
            } catch(SQLException ex) {

                Bukkit.getScheduler().runTask(_pluginInstance, () -> {
                    toComplete.completeExceptionally(ex);
                });

                return;

            }

            try {

                I response = (I) action.executeAction(con);

                Bukkit.getScheduler().runTask(_pluginInstance, () -> {
                    toComplete.complete(response);
                });

                con.close();


            } catch(SQLException | DatabaseException ex) {

                if(!(ex instanceof DatabaseException))
                    ex.printStackTrace();;

                Bukkit.getScheduler().runTask(_pluginInstance, () -> {
                    toComplete.completeExceptionally(ex);
                });

            }


        });

        return toComplete;

    }

}
