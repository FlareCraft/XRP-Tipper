package com.flarecraft.xrptipper.database;

import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;

import java.util.List;
import java.util.UUID;

public interface DatabaseManager {

    void newUser(String playerName, UUID uuid, String string);

    PlayerProfile loadPlayerProfile(String playerName, UUID uuid, boolean createNew);
    boolean saveUser(PlayerProfile profile);
}
