package com.flarecraft.xrptipper.datatypes.player;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.runnables.player.PlayerProfileSaveTask;

import java.util.UUID;

public class PlayerProfile {

    private final String playerName;
    private UUID uuid;
    private String xrplAddress;
    private String xummToken;
    private boolean loaded;
    private String userToken;
    private volatile boolean changed;

    private int saveAttempts = 0;

    public PlayerProfile(String playerName, UUID uuid, String xrplAddress, String xummToken) {

        this.playerName = playerName;
        this.uuid = uuid;
        this.xrplAddress = xrplAddress;
        this.xummToken = xummToken;

        loaded = true;
    }

    public boolean isLoaded() {

        return loaded;
    }

    public void scheduleAsyncSave() {
        new PlayerProfileSaveTask(this, false).runTaskAsynchronously(XRPTipper.p);
    }

    public void save(boolean useSync) {

        PlayerProfile profileCopy = new PlayerProfile(playerName, uuid, xrplAddress, xummToken);
        changed = !XRPTipper.getDatabaseManager().saveUser(profileCopy);

        if (changed) {

            XRPTipper.p.getLogger().severe("PlayerProfile saving failed for player: " + playerName + " " + uuid);
            if(saveAttempts > 0) {

                XRPTipper.p.getLogger().severe("Attempted to save profile for player " + getPlayerName() +
                        " resulted in failure. " + saveAttempts + " have been made so far.");
            }
            if(saveAttempts < 10) {

                saveAttempts++;

                if(XRPTipper.isServerShutdownExecuted() || useSync) {

                    new PlayerProfileSaveTask(this, true).runTask(XRPTipper.p);
                }
                else {

                    scheduleAsyncSave();
                }
                //TODO: Some server shutdown code needs to live here
            } else {
                XRPTipper.p.getLogger().severe("XRPTipper has failed to save the profile for "
                        +getPlayerName()+" numerous times." +
                        " XRPTipper will now stop attempting to save this profile." +
                        " Check your console for errors and inspect your DB for issues.");
            }
        } else {
            saveAttempts = 0;
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getXrplAddress() { return xrplAddress; }

    public void setXummToken(String XummToken) { this.xummToken = XummToken; }
    public String getXummToken() { return xummToken; }

    public void setXrplAddress(String xrplAddress) { this.xrplAddress = xrplAddress; }

}
