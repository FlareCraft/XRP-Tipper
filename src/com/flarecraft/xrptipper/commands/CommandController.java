package com.flarecraft.xrptipper.commands;

import com.flarecraft.xrptipper.XRPTipper;
import org.bukkit.command.PluginCommand;

public class CommandController {

    public static void registerCommands() {

        XRPTipper.p.getLogger().info("Performing command registration");
        registerRegisterCommand();
        registerTipCommand();
        registerSendCommand();
    }

    private static void registerRegisterCommand() {

        PluginCommand command = XRPTipper.p.getCommand("xrpRegister");
        command.setDescription("Enable tipping by registering your XRP Wallet Address");
        command.setExecutor(new Register());
    }

    private static void registerTipCommand() {

        PluginCommand command = XRPTipper.p.getCommand("xrpTip");
        command.setDescription("Send this server a tip.....in XRP!");
        command.setExecutor(new Tip());
    }

    private static void registerSendCommand() {

        PluginCommand command = XRPTipper.p.getCommand("xrpSend");
        command.setDescription("Send a tip to another player! /xrptip [playerName] [amount]");
        command.setExecutor(new Send());
    }
}
