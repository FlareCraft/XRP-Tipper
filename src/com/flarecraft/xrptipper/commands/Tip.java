package com.flarecraft.xrptipper.commands;

import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.transactions.TransactionController;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tip implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {

        System.out.println("Player has executed the 'Tip' command");
        Player playa = (Player) sender;
        XRPTipperPlayer test = UserManager.getPlayer(playa);
        System.out.println(test.getProfile().getPlayerName());
        // Probably need to put a try/catch handling here to ensure that I get an Int
        System.out.println(strings[0]);
        double paymentAmount = Double.parseDouble(strings[0]);
        System.out.println(paymentAmount);
        TransactionController.tipHandler(test.getProfile(), paymentAmount);
        return true;
    }

}
