package com.flarecraft.xrptipper.config;

import com.flarecraft.xrptipper.XRPTipper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigLoader {

    protected static final XRPTipper plugin = XRPTipper.p;
    protected String fileName;
    protected final File configFile;
    protected FileConfiguration config;

    public ConfigLoader(String fileName) {
        this.fileName = fileName;
        configFile = new File(plugin.getDataFolder(), fileName);
        loadFile();

    }

    protected void loadFile() {

        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
