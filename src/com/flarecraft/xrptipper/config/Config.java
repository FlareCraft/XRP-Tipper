package com.flarecraft.xrptipper.config;

public class Config extends AutoUpdateConfigLoader  {
    private static Config instance;

    private Config() {
        super("config.yml");
    }

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }

        return instance;
    }

    public String getServerWalletAddress() { return config.getString("General.Server.XRP_Wallet_Address"); }
    public String getXUMMApiKey() { return config.getString("General.Server.XUMM.API_KEY");};
    public String getXUMMApiSecret() { return config.getString("General.Server.XUMM.API_SECRET");}
}