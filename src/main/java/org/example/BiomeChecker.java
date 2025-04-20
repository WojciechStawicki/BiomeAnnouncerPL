package org.example;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class BiomeChecker extends BukkitRunnable {
    private final BiomeAnnouncer plugin;
    private final Map<UUID, String> lastBiome = new HashMap<>();
    private final Map<UUID, Long> lastAnnounce = new HashMap<>();
    private final Map<UUID, Boolean> announcerEnabled = new HashMap<>();

    public BiomeChecker(BiomeAnnouncer plugin) {
        this.plugin = plugin;
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
        // Convert to lowercase first
        String formatted = biome.toLowerCase();

        // Handle Terra biomes
        if (formatted.startsWith("terra:")) {
            // Split by '/' and get the last part which contains the actual biome name
            String[] parts = formatted.split("/");
            formatted = parts[parts.length - 1];
        }

        // Remove any remaining prefixes
        formatted = formatted.replace("terra:", "")
                .replace("minecraft:", "")
                .replace("_", " ");

        // Capitalize each word
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