package me.bubbles.nmcrewards.likes;

import me.bubbles.nmcrewards.NMCRewards;
import me.bubbles.nmcrewards.util.ticker.Timer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.UUID;

public class Likes extends Timer {

    private static boolean init=false;
    private URL url;
    private HashSet<UUID> likes;

    public Likes(NMCRewards plugin) {
        super(plugin, 0);
        if(init) throw new IllegalStateException("Already initialized");
        setCap(plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getInt("update"));
        try {
            url=new URL("https://api.namemc.com/server/"+plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getString("nameMC")+"/likes");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasLike(UUID uuid) {
        return likes.contains(uuid);
    }

    private void update() {
        likes=new HashSet<>();
        try {
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed to retrieve data from API: " + connection.getResponseMessage());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            reader.close();
            connection.disconnect();

            String jsonString = jsonStringBuilder.toString();

            String[] playerIdArray = jsonString.split(",");
            for (String uuid : playerIdArray) {
                uuid = uuid.trim();
                likes.add(UUID.fromString(uuid));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
