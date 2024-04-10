package me.bubbles.nmcrewards.commands.base;

import me.bubbles.nmcrewards.commands.manager.CommandBase;
import me.bubbles.nmcrewards.NMCRewards;
import me.bubbles.nmcrewards.database.databases.ClaimedDB;
import me.bubbles.nmcrewards.util.UtilDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

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
        ClaimedDB claimedDB = UtilDatabase.getClaimedDB();
        boolean claimed = false;
        if(claimedDB.hasValue(utilSender.getPlayer().getUniqueId())) {
            if(claimedDB.getValue(utilSender.getPlayer().getUniqueId())) {
                claimed=true;
            }
        }
        if(claimed) {
            utilSender.sendMessage("%prefix%%primary%You've already claimed your rewards.");
            return;
        }
        if(!plugin.getLikes().hasLike(utilSender.getPlayer().getUniqueId())) {
            utilSender.sendMessage(plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getString("notLiking"));
            return;
        }
        String message = plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getString("rewardMsg");
        if(!(message==null||message.isEmpty())) {
            utilSender.sendMessage(message);
        }
        ConfigurationSection section = plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getConfigurationSection("reward");
        for(String string : section.getKeys(false)) {
            String command = section.getString(string);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }

    }

}
