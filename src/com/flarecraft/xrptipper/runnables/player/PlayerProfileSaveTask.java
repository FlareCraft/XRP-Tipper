package com.flarecraft.xrptipper.runnables.player;

import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerProfileSaveTask extends BukkitRunnable {

    private final PlayerProfile playerProfile;
    private final boolean isSync;

    public PlayerProfileSaveTask(PlayerProfile playerProfile, boolean isSync) {

        this.playerProfile = playerProfile;
        this.isSync = isSync;
    }

    @Override
    public void run() {

        playerProfile.save(isSync);
    }
}
