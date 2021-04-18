package com.flarecraft.xrptipper;

import com.flarecraft.xrptipper.commands.CommandController;
import com.flarecraft.xrptipper.database.DatabaseManager;
import com.flarecraft.xrptipper.database.DatabaseManagerFactory;
import com.flarecraft.xrptipper.listeners.PlayerListener;
import com.flarecraft.xrptipper.transactions.XUMM.XUMM;
import com.flarecraft.xrptipper.util.player.UserManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class XRPTipper extends JavaPlugin {

    public static XRPTipper p;

    public XRPTipper() {
        p = this;
    }

    private static boolean serverShutdownExecuted = false;

    /* Managers */
    private static DatabaseManager databaseManager;

    /* File Paths */
    private static String mainDirectory;
    private static String flatFileDirectory;
    private static String playerPaymentPreferencesFile;

    /* Metadata Values */
    public final static String playerDataKey = "XRPTipper: Player Data";

    /* Payment Providers */
    private static XUMM xumm;

    @Override
    public void onEnable() {

        setupFilePaths();
        databaseManager = DatabaseManagerFactory.getDatabaseManager();
        CommandController.registerCommands();
        registerEvents();
        p.saveDefaultConfig();
        setupPaymentProviders();
    }

    @Override
    public void onDisable() {

        setServerShutdown(true);
        XRPTipper.p.getLogger().info("Server shutdown has been executed, executing shutdown procedures...");

        try {

            UserManager.saveAll();
            UserManager.clearAll();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static DatabaseManager getDatabaseManager() {

        return databaseManager;
    }

    public static String getPlayerPaymentPreferencesFilePath() {

        return playerPaymentPreferencesFile;
    }

    public static String getMainDirectory() {

        return mainDirectory;
    }

    public static XUMM getXumm() {

        return xumm;
    }

    public static synchronized boolean isServerShutdownExecuted() {
        return serverShutdownExecuted;
    }

    private void setupFilePaths() {

        //xrptipper = getFile();
        mainDirectory = getDataFolder().getPath() + File.separator;
        System.out.println("mainDirectory: " + mainDirectory);
        flatFileDirectory = mainDirectory + "flatfile" + File.separator;
        System.out.println("flatFileDirectory: " + flatFileDirectory);
        playerPaymentPreferencesFile = flatFileDirectory + "xrptipper.paymentpreferences";
        System.out.println("playerPaymentPreferencesFile: " + playerPaymentPreferencesFile);
    }

    private void setupPaymentProviders() {

        xumm = new XUMM();
    }

    private void setServerShutdown(boolean state) {

        serverShutdownExecuted = state;
    }

    private void registerEvents() {

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
    }

}
