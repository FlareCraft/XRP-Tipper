package com.flarecraft.xrptipper.commands;

import com.flarecraft.xrptipper.XRPTipper;
import com.flarecraft.xrptipper.datatypes.player.XRPTipperPlayer;
import com.flarecraft.xrptipper.transactions.TransactionController;
import com.flarecraft.xrptipper.transactions.XUMM.XUMMRegistrationTask;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tip implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {

        XRPTipper.p.getLogger().info("Player has executed the 'Tip' command");
        Player playa = (Player) sender;
        XRPTipperPlayer tipperPlayer = UserManager.getPlayer(playa);
        double paymentAmount = Double.parseDouble(strings[0]);
        try {
        
            TransactionController.tipHandler(tipperPlayer.getProfile(), paymentAmount, playa);
        } catch(TransactionController e) {

            if(e.getMessage().equals("Expired registration link")) {

                playa.sendMessage("It looks like your registration has expired. Use the link below to re-register");
                new XUMMRegistrationTask(tipperPlayer.getProfile().getXrplAddress(), playa).runTaskLaterAsynchronously(XRPTipper.p, 60);
            }
        } catch(NullPointerException e) {

            playa.sendMessage("[ERROR] Register your wallet using the /xrpregister command before using /xrptip");
        }
        return true;
    }
}
