package co.turtlegames.core.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseAction<T> {

    public T executeAction(Connection con) throws SQLException, DatabaseException;

}
