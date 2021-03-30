package com.flarecraft.xrptipper.util.player;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class UserManager {

    private UserManager() {}

    public static void track(XRPTipperPlayer tipperPlayer) {

        tipperPlayer.getPlayer().setMetadata(XRPTipper.playerDataKey, new FixedMetadataValue(XRPTipper.p, tipperPlayer));
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
}
