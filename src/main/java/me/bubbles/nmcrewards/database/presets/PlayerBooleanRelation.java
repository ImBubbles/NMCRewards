package me.bubbles.nmcrewards.database.presets;

import me.bubbles.nmcrewards.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public abstract class PlayerBooleanRelation extends Database {

    public PlayerBooleanRelation(String address, int port, String database, String username, String password, String tableName) {
        super(address, port, database, username, password, tableName,
                "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "user CHAR(36)," +
                        "value TINYINT," +
                        "PRIMARY KEY (user)" +
                        ")"
        );
    }

    public boolean getValue(UUID player) {
        Boolean result = false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + tableName + " WHERE user=?");
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int boolVal = rs.getInt("value");
                if(rs.getInt("value")==1) {
                    result=true;
                }
            }
            rs.close();
            statement.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public boolean setRelation(UUID player, boolean value) {
        removeRelation(player);
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " " +
                    "(user, value) VALUES (?, ?)");

            statement.setString(1, player.toString());
            int boolVal = value==false ? 0 : 1;
            statement.setInt(2, boolVal);
            statement.execute();
            statement.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeRelation(UUID player) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE user=?");
            statement.setString(1, player.toString());
            statement.execute();
            statement.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean hasValue(UUID player) {
        boolean result = false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + tableName + " WHERE user=?");
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            result = rs.next();
            rs.close();
            statement.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

}
