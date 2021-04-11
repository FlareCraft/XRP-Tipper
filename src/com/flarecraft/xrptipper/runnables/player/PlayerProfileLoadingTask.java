package com.flarecraft.xrptipper.runnables.player;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerProfileLoadingTask extends BukkitRunnable {

    private final Player player;

    public PlayerProfileLoadingTask(Player player) {

        this.player = player;
    }

    @Override
    public void run() {

        //Quit if the player is logged out
        if (!player.isOnline()) {

            System.out.println("Aborting profile loading for " + player.getName() + " - player logged out");
            return;
        }

        PlayerProfile profile = XRPTipper.getDatabaseManager().loadPlayerProfile(player.getName(), player.getUniqueId(), true);

        if (profile.isLoaded()) {

            new ApplySuccessfulProfile(new XRPTipperPlayer(player, profile)).runTask(XRPTipper.p);
        }
    }

    private class ApplySuccessfulProfile extends BukkitRunnable {

        private final XRPTipperPlayer tipperPlayer;

        private ApplySuccessfulProfile(XRPTipperPlayer tipperPlayer) {
            this.tipperPlayer = tipperPlayer;
        }

        @Override
        public void run() {

            if (!player.isOnline()) {
                System.out.println("Player is not online");
                return;
            }

            UserManager.track(tipperPlayer);
            // I may need to add UserManager.track(tipperPlayer) here eventually. Need to investigate more

            System.out.println("Profile is loaded");
        }
    }
}
