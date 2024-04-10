package me.bubbles.nmcrewards.util;

import me.bubbles.nmcrewards.NMCRewards;
import me.bubbles.nmcrewards.configs.Config;
import me.bubbles.nmcrewards.configs.ConfigManager;
import me.bubbles.nmcrewards.database.databases.ClaimedDB;
import org.bukkit.configuration.file.FileConfiguration;

public class UtilDatabase {
    private static String ADDRESS;
    private static int PORT = 3306;
    private static String DATABASE;
    private static String USERNAME;
    private static String PASSWORD;
    private static boolean initialized = false;
    public UtilDatabase(NMCRewards plugin) {
        if (initialized) throw new IllegalStateException("Already initialized");
        final ConfigManager configManager = plugin.getConfigManager();
        Config config = configManager.getConfig("config.yml");
        FileConfiguration fileConfig = config.getFileConfiguration();
        ADDRESS = fileConfig.getString("database.address");
        DATABASE = fileConfig.getString("database.database");
        USERNAME = fileConfig.getString("database.username");
        PASSWORD = fileConfig.getString("database.password");
        PORT = fileConfig.getInt("database.port");
        initialized = true;
    }

    public static ClaimedDB getClaimedDB() {
        return new ClaimedDB(ADDRESS, PORT, DATABASE, USERNAME, PASSWORD);
    }

}
