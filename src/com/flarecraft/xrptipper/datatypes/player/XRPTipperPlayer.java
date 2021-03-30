package com.flarecraft.xrptipper.datatypes.player;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class XRPTipperPlayer {

    private final Player player;
    private final PlayerProfile profile;
    private final String playerName;

    public XRPTipperPlayer(Player player, PlayerProfile profile) {

        this.playerName = player.getName();
        UUID uuid = player.getUniqueId();
        this.player = player;
        this.profile = profile;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public void logout(boolean syncSave) {

        System.out.println("I made it to logout");
        Player thisPlayer = getPlayer();

        if(syncSave) {
            getProfile().save(true);
        } else {
            // TODO: I think that server saving goes here
        }

        UserManager.remove(thisPlayer);

    }
}
