package com.flarecraft.xrptipper.listeners;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.runnables.player.PlayerProfileLoadingTask;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {

        System.out.println("XRPTipper - Player has joined the game");
        Player player = event.getPlayer();
        new PlayerProfileLoadingTask(player).runTaskLaterAsynchronously(XRPTipper.p, 60);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (!UserManager.hasPlayerDataKey(player)) {

            return;
        }

        XRPTipperPlayer xrpTipperPlayer = UserManager.getPlayer(player);

        if(xrpTipperPlayer == null) {

            return;
        }

        xrpTipperPlayer.logout(true);
    }
}