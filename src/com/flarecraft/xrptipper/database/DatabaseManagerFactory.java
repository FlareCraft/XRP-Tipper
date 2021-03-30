package com.flarecraft.xrptipper.database;

public class DatabaseManagerFactory {

    public static DatabaseManager getDatabaseManager() {

        return new FlatfileDatabaseManager();
    }
}
