package me.bubbles.nmcrewards.database.databases;

import me.bubbles.nmcrewards.database.presets.PlayerBooleanRelation;

public class ClaimedDB extends PlayerBooleanRelation {

    public ClaimedDB(String address, int port, String database, String username, String password) {
        super(address, port, database, username, password, "Claimed_Rewards");
    }

}
