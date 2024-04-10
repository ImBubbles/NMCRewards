package me.bubbles.nmcrewards.util.ticker;

import me.bubbles.nmcrewards.NMCRewards;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class Ticker {

    private boolean enabled;
    private NMCRewards plugin;

    public Ticker(NMCRewards plugin) {
        this.plugin=plugin;
    }

    private void tick() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if(enabled) {
                plugin.onTick();
                tick();
            }
        }, 1);
    }

    public Ticker toggle() {
        enabled=!enabled;
        if(enabled) {
            tick();
        }
        return this;
    }

    public Ticker setEnabled(boolean bool) {
        if(enabled==bool) {
            return this;
        }
        enabled=bool;
        if(enabled) {
            tick();
        }
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

}