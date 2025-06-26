package org.example;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;
import java.io.File;

public class BiomeChecker extends BukkitRunnable {
    private final BiomeAnnouncer plugin;
    private final Map<UUID, String> lastBiome = new HashMap<>();
    private final Map<UUID, Long> lastAnnounce = new HashMap<>();
    private final Map<UUID, Boolean> announcerEnabled = new HashMap<>();
    private YamlConfiguration biomeLang;

    public BiomeChecker(BiomeAnnouncer plugin) {
        this.plugin = plugin;
        loadBiomeTranslations();
    }

    private void loadBiomeTranslations() {
        try {
            String lang = plugin.getConfig().getString("language", "pl_PL");
            File langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
            if (langFile.exists()) {
                biomeLang = YamlConfiguration.loadConfiguration(langFile);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Could not load biome translations: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String worldName = player.getWorld().getName();
            if (!plugin.getConfig().getStringList("enabled-worlds").contains(worldName)) continue;

            String currentBiome = player.getLocation().getBlock().getBiome().toString();
            UUID playerUUID = player.getUniqueId();

            if (!currentBiome.equals(lastBiome.get(playerUUID))) {
                Long lastTime = lastAnnounce.get(playerUUID);
                if (lastTime != null && System.currentTimeMillis() - lastTime < plugin.getConfig().getInt("cooldown.duration") * 1000) continue;

                if (announcerEnabled.getOrDefault(playerUUID, false)) continue;

                lastAnnounce.put(playerUUID, System.currentTimeMillis());
                lastBiome.put(playerUUID, currentBiome);

                String biomeName = formatBiomeName(currentBiome);
                new BiomeAnimation(plugin, player, biomeName).runTaskTimer(plugin, 0L, plugin.getConfig().getInt("animation.typing-speed"));
            }
        }
    }

    private String formatBiomeName(String biome) {
        String namespace = "minecraft";
        String name = biome.toLowerCase();

        if (biome.contains(":")) {
            String[] parts = biome.toLowerCase().split(":");
            if (parts.length == 2) {
                namespace = parts[0];
                name = parts[1];
            }
        }

        // Replace slashes with dots for nested YAML lookup
        String yamlName = name.replace("/", ".");

        if (biomeLang != null) {
            String path = "biome." + namespace + "." + yamlName;
            String translation = biomeLang.getString(path);
            if (translation != null) {
                return translation;
            }
        }

        // fallback: prettify
        String formatted = name.replace("_", " ").replace("/", " ");
        String[] words = formatted.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                result.append(Character.toUpperCase(words[i].charAt(0)))
                      .append(words[i].substring(1));
                if (i < words.length - 1) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }

    public void toggleAnnouncer(UUID playerUUID) {
        announcerEnabled.put(playerUUID, !announcerEnabled.getOrDefault(playerUUID, false));
    }

    public boolean isAnnouncerEnabled(UUID playerUUID) {
        return announcerEnabled.getOrDefault(playerUUID, false);
    }
}