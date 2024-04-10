package me.bubbles.nmcrewards.util;

import me.bubbles.nmcrewards.NMCRewards;
import me.bubbles.nmcrewards.util.string.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UtilSender {

    private CommandSender sender;
    private NMCRewards plugin;

    public UtilSender(NMCRewards plugin, CommandSender sender) {
        this.plugin=plugin;
        this.sender=sender;
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void sendMessage(String message) {
        if(isPlayer()) {
            getPlayer().sendMessage(UtilString.colorFillPlaceholders(message));
            return;
        }
        Bukkit.getConsoleSender().sendMessage(UtilString.colorFillPlaceholders(message.replace("\n","\n&f")));
    }

    public Player getPlayer() {
        if(!isPlayer()){
            return null;
        }
        return (Player) sender;
    }

    public boolean hasPermission(String permission) {
        if(!isPlayer()) {
            return true;
        }
        return getPlayer().hasPermission(permission);
    }

}
