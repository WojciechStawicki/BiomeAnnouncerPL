package com.yourname.biomeannouncer;

import org.bukkit.plugin.java.JavaPlugin;

public class BiomeAnnouncer extends JavaPlugin {
    private static BiomeAnnouncer instance;
    private BiomeChecker biomeChecker;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config
        saveDefaultConfig();

        // Register command
        getCommand("togglebiomeannouncer").setExecutor(new ToggleCommand());

        // Register biome checker task
        this.biomeChecker = new BiomeChecker(this);
        this.biomeChecker.runTaskTimer(this, 20L, 20L);

        getLogger().info("BiomeAnnouncer has been enabled!");
    }

    public static BiomeAnnouncer getInstance() {
        return instance;
    }

    public BiomeChecker getBiomeChecker() {
        return biomeChecker;
    }
}