package org.example;

import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

public class BiomeAnnouncer extends JavaPlugin {
    private static BiomeAnnouncer instance;
    private BiomeChecker biomeChecker;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize bStats
        int pluginId = 25547;
        getLogger().info("Initializing bStats with plugin ID: " + pluginId);
        try {
            Metrics metrics = new Metrics(this, pluginId);
            getLogger().info("bStats Metrics has been enabled!");
        } catch (Exception e) {
            getLogger().warning("Failed to initialize bStats: " + e.getMessage());
            e.printStackTrace();
        }

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