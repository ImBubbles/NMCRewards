package me.bubbles.nmcrewards.commands.base;

import me.bubbles.nmcrewards.commands.manager.CommandBase;
import me.bubbles.nmcrewards.NMCRewards;
import me.bubbles.nmcrewards.database.databases.ClaimedDB;
import me.bubbles.nmcrewards.util.UtilDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class BaseCommand extends CommandBase {
    private final int index=0;

    public BaseCommand(NMCRewards plugin) {
        super(plugin, "namemc");
        setPermission("claim");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        super.run(sender, args);
        if(!utilSender.isPlayer()) {
            return;
        }
        if(!permissionCheck()) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            ClaimedDB claimedDB = UtilDatabase.getClaimedDB();
            boolean claimed = false;
            if(claimedDB.hasValue(utilSender.getPlayer().getUniqueId())) {
                if(claimedDB.getValue(utilSender.getPlayer().getUniqueId())) {
                    claimed=true;
                }
            }
            boolean finalClaimed = claimed;
            Bukkit.getScheduler().runTask(plugin, () -> {
                if(finalClaimed) {
                    utilSender.sendMessage("%prefix%%primary%You've already claimed your rewards.");
                    return;
                }
                if(!plugin.getLikes().hasLike(utilSender.getPlayer().getUniqueId())) {
                    utilSender.sendMessage(plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getString("notLiking"));
                    return;
                }
                String message = plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getString("rewardMsg");
                if (!message.isEmpty()) {
                    utilSender.sendMessage(message);
                }
                List<String> list = plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getStringList("reward");
                for(String string : list) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replaceAll("%player%", utilSender.getPlayer().getName()));
                }
            });
        });


    }

}
