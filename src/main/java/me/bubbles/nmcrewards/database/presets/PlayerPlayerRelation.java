package me.bubbles.nmcrewards.database.presets;

import me.bubbles.nmcrewards.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.UUID;

public abstract class PlayerPlayerRelation extends Database {

    public PlayerPlayerRelation(String address, int port, String database, String username, String password, String tableName) {
        super(address, port, database, username, password, tableName,
                "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT," +
                        "user CHAR(36)," +
                        "related CHAR(36)," +
                        "PRIMARY KEY (id)" +
                        ")"
        );
    }

    public HashSet<UUID> getRelations(UUID player) {
        HashSet<UUID> result = new HashSet<>();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + tableName + " WHERE user=?");
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(UUID.fromString(rs.getString("related")));
            }
            rs.close();
            statement.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public boolean addRelation(UUID player, UUID player2) {
        removeRelation(player, player2);
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " " +
                    "(user, related) VALUES (?, ?)");

            statement.setString(1, player.toString());
            statement.setString(2, player2.toString());
            statement.execute();
            statement.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeRelation(UUID player, UUID player2) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE user=? AND related=?");
            statement.setString(1, player.toString());
            statement.setString(2, player2.toString());
            statement.execute();
            statement.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isRelatedWith(UUID player, UUID player2) {
        boolean result = false;
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + tableName + " WHERE user=? AND related=?");
            statement.setString(1, player.toString());
            statement.setString(2, player2.toString());
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
