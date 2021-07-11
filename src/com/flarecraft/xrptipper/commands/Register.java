package com.flarecraft.xrptipper.commands;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.database.DatabaseManager;
import com.flarecraft.xrptipper.datatypes.player.PlayerProfile;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.runnables.player.PlayerProfileLoadingTask;
import com.flarecraft.xrptipper.transactions.TransactionController;
import com.flarecraft.xrptipper.transactions.XUMM.XUMMRegistrationTask;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Register implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player playa = (Player) sender;
        if (args.length == 0) {

            playa.sendMessage("[ERROR] Include an XRP wallet address to register. ex: /xrpregister [xrplAddress]");
            return false;
        }

        //if profile doesn't exist, create user in flatfile
        try {

            XRPTipperPlayer player = UserManager.getPlayer(playa);
            PlayerProfile profile = player.getProfile();

            profile.setXrplAddress(args[0]);
        } catch (NullPointerException e) {

            DatabaseManager dbManager = XRPTipper.getDatabaseManager();
            dbManager.newUser(sender.getName(), playa.getUniqueId(), args[0]);
            new PlayerProfileLoadingTask(playa).runTaskLaterAsynchronously(XRPTipper.p, 60);
        }

        new XUMMRegistrationTask(args[0], playa).runTaskLaterAsynchronously(XRPTipper.p, 60);

        /* Eventually I would like the newUser function to be very barebones and then all the preferences
        * get added via their own method */
        return true;
    }
}
