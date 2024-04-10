package me.bubbles.nmcrewards;

import me.bubbles.nmcrewards.commands.manager.CommandManager;
import me.bubbles.nmcrewards.configs.ConfigManager;
import me.bubbles.nmcrewards.util.UtilDatabase;
import me.bubbles.nmcrewards.likes.Likes;
import me.bubbles.nmcrewards.util.string.UtilString;
import me.bubbles.nmcrewards.util.ticker.Ticker;
import me.bubbles.nmcrewards.util.ticker.TimerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;

public final class NMCRewards extends JavaPlugin {

    private ConfigManager configManager;
    private TimerManager timerManager;
    private CommandManager commandManager;
    private Ticker ticker;
    private Likes likes;

    @Override
    public void onEnable() {
        // Config
        configManager=new ConfigManager(this);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        configManager.addConfig(
                "config.yml"
        );

        // PREP UTILITY
        new UtilDatabase(this);
        new UtilString(this);

        // MANAGERS
        // THIS ORDER IS VERY IMPORTANT, SWAPPING THINGS AROUND WILL MAY CAUSE VALUES TO BE RETURNED AS NULL
        timerManager=new TimerManager();
        commandManager=new CommandManager(this);

        // Ticker
        ticker=new Ticker(this).setEnabled(true);
        likes = new Likes(this);
        timerManager.addTimer(likes);
    }

    // TICKER
    public void onTick() {
        timerManager.onTick();
    }

    @Override
    public void onDisable() {
        ticker.setEnabled(false);
        timerManager.removeTimer(likes);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
    public TimerManager getTimerManager() {
        return timerManager;
    }
    public Likes getLikes() {
        return likes;
    }
    public long getEpochTimestamp() {
        return Instant.now().getEpochSecond();
    }

}
