package me.bubbles.nmcrewards.util.string;

import me.bubbles.nmcrewards.NMCRewards;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class UtilString {
    private static final int CENTER_PX = 154;
    private static NMCRewards PLUGIN;
    private static boolean initialized = false;

    public UtilString(NMCRewards plugin) {
        if (initialized) throw new IllegalStateException("Already initialized");
        PLUGIN=plugin;
        initialized=true;
    }

    public static String colorFillPlaceholders(String message) {
        FileConfiguration config = PLUGIN.getConfigManager().getConfig("config.yml").getFileConfiguration();
        ConfigurationSection placeholders = config.getConfigurationSection("placeholders");
        for(String string : placeholders.getKeys(false)) {
            String regex = "%"+string+"%";
            String replacement = placeholders.getString(string);
            if(replacement!=null) {
                message = message.replace(regex,replacement);
            }
        }
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    public static String centerMessage(String message) {
        if (message == null || message.isEmpty()) return "";
        message = colorFillPlaceholders(message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        for (char c : message.toCharArray()) {
            if (c == '�') {
                previousCode = true;

            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';

            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb + message;

    }

    public static String commaize(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i<strings.length; i++) {
            stringBuilder.append(strings[i]);
            if(i<strings.length-1) {
                stringBuilder.append("%primary%, ");
            }
        }
        return stringBuilder.toString();
    }

}
