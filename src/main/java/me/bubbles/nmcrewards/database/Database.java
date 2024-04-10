package me.bubbles.nmcrewards.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Database implements IDatabase {

    private final String address;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    protected String tableName;


    public Database(String address, int port, String database, String username, String password, String tableName, String launchStatement) {
        this.tableName = tableName;
        this.address = address;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        try {

            if (launchStatement != null) {
                // TABLE EXAMPLE
                /*PreparedStatement statement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS User_Data (" +
                                "uuid CHAR(36)," +
                                "xp INT NOT NULL DEFAULT 0," +
                                "PRIMARY KEY (uuid)" +
                                ")");*/
                try (Connection connection = getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(launchStatement);
                    statement.execute();
                    statement.close();
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            throw new RuntimeException("Failed to connect to database!");
        }
    }

    public Database(String address, int port, String database, String username, String password, String tableName) {
        this(address, port, database, username, password, tableName, null);
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://" + address + ":" + port + "/" + database, username, password);
    }

}
