package com.flarecraft.xrptipper.transactions.XUMM;

import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.transactions.TransactionController;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class XUMMRegistrationTask extends BukkitRunnable {

    private final String address;
    private final Player player;
    public XUMMRegistrationTask(String address, Player player) {

        this.player = player;
        this.address = address;
    }

    @Override
    public void run() {

        String userToken = TransactionController.handleRegistration(address, player);
        XRPTipperPlayer XRPPlayer = UserManager.getPlayer(player);
        PlayerProfile profile = XRPPlayer.getProfile();
        profile.setXummToken(userToken);
        // Next I need to take the user token and apply it to the players profile.
    }
}
