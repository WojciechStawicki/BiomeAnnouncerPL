package org.example;

import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

public class BiomeAnnouncer extends JavaPlugin {
    private static BiomeAnnouncer instance;
    private BiomeChecker biomeChecker;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize bStats
        int pluginId = 25547;
        getLogger().info("Initializing bStats with plugin ID: " + pluginId);
        try {
            new Metrics(this, pluginId);
            getLogger().info("bStats Metrics has been enabled!");
        } catch (Exception e) {
            getLogger().warning("Failed to initialize bStats: " + e.getMessage());
            e.printStackTrace();
        }

        // Save default config
        saveDefaultConfig();

        // Save Polish biome translations if not present
        saveResource("lang/pl_PL.yml", false);

        // Register command
        getCommand("togglebiomeannouncer").setExecutor(new ToggleCommand());

        // Register biome checker task
        this.biomeChecker = new BiomeChecker(this);
        this.biomeChecker.runTaskTimer(this, 20L, 20L);

        // Initialize update checker if enabled
        if (getConfig().getBoolean("update-checker.enabled", true)) {
            this.updateChecker = new UpdateChecker(this);
            this.updateChecker.checkForUpdates();
        }

        getLogger().info("BiomeAnnouncer has been enabled!");
    }

    public static BiomeAnnouncer getInstance() {
        return instance;
    }

    public BiomeChecker getBiomeChecker() {
        return biomeChecker;
    }
}