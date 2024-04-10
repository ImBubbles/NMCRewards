package me.bubbles.nmcrewards.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabase {

    String getTableName();
    Connection getConnection() throws SQLException;
}
