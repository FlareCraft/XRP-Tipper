package com.flarecraft.xrptipper.database;

import com.flarecraft.xrptipper.XRPTipper;

public class DatabaseManagerFactory {

    public static DatabaseManager getDatabaseManager() {

        XRPTipper.p.getLogger().info("Setting up flatfile DB");
        return new FlatfileDatabaseManager();
    }
}
