package com.flarecraft.xrptipper.util.player;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;

public class UserManager {

    private static HashSet<XRPTipperPlayer> playerDataSet;
    private UserManager() {}

    public static void track(XRPTipperPlayer tipperPlayer) {

        tipperPlayer.getPlayer().setMetadata(XRPTipper.playerDataKey, new FixedMetadataValue(XRPTipper.p, tipperPlayer));

        if(playerDataSet == null) {

            playerDataSet = new HashSet<>();
        }
        playerDataSet.add(tipperPlayer);
    }

    public static XRPTipperPlayer getPlayer(Player player) {

        if(player != null && player.hasMetadata(XRPTipper.playerDataKey)) {
            return (XRPTipperPlayer) player.getMetadata(XRPTipper.playerDataKey).get(0).value();
        } else {
            return null;
        }
    }

    public static void remove(Player player) {

        XRPTipperPlayer xrpTipperPlayer = getPlayer(player);
        player.removeMetadata(XRPTipper.playerDataKey, XRPTipper.p);

        // TODO: This function will be needed once I implement server shutdown saving
        //playerDataSet.remove(xrpTipperPlayer);
    }

    public static boolean hasPlayerDataKey(Entity entity) {
        return entity != null && entity.hasMetadata(XRPTipper.playerDataKey);
    }

    public static void saveAll() {

        if(playerDataSet == null) {

            return;
        }
        ImmutableList<XRPTipperPlayer> trackedSyncData = ImmutableList.copyOf(playerDataSet);

        XRPTipper.p.getLogger().info("Saving XRPTipper player profiles..... (" + trackedSyncData.size() + ")");

        for(XRPTipperPlayer playerData : trackedSyncData) {

            try {
                XRPTipper.p.getLogger().info("Saving data for player: " + playerData.getPlayerName());
                playerData.getProfile().save(true);
            } catch (Exception e) {
                XRPTipper.p.getLogger().warning("Could not save XRPTipper player data for player: " + playerData.getPlayerName());
            }
        }
        XRPTipper.p.getLogger().info("Finished save operation for " + trackedSyncData.size() + " players!");
    }

    public static void clearAll() {
        for (Player player : XRPTipper.p.getServer().getOnlinePlayers()) {
            remove(player);
        }

        if(playerDataSet != null)
            playerDataSet.clear(); //Clear sync save tracking
    }
}
