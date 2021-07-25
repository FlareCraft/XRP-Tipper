package com.flarecraft.xrptipper.commands;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.transactions.TransactionController;
import com.flarecraft.xrptipper.transactions.XUMM.XUMMRegistrationTask;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Send implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if (args.length == 0 || args.length == 1) {

            player.sendMessage("[ERROR] Missing arguments. Format is /xrpSend [playerName] [amount]");
            return false;
        }
        XRPTipperPlayer tipperPlayer = UserManager.getPlayer(player);
        double paymentAmount = Double.parseDouble(args[1]);
        try {

            TransactionController.sendHandler(tipperPlayer.getProfile(), paymentAmount, player, Bukkit.getPlayer(args[0]));
        } catch (TransactionController e) {

            if(e.getMessage().equals("Expired registration link")) {

                player.sendMessage("It looks like your registration has expired. Use the link below to re-register");
                new XUMMRegistrationTask(tipperPlayer.getProfile().getXrplAddress(),player).runTaskLaterAsynchronously(XRPTipper.p, 60);
            }
        }

        return true;
    }
}
