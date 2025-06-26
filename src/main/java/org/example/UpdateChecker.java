package org.example;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateChecker implements Listener {
    private final BiomeAnnouncer plugin;
    private final String currentVersion;
    private String latestVersion;
    private boolean updateAvailable = false;

    public UpdateChecker(BiomeAnnouncer plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void checkForUpdates() {
        if (!plugin.getConfig().getBoolean("update-checker.enabled", true)) {
            plugin.getLogger().info(ChatColor.YELLOW + "[BiomeAnnouncer] Update checker is disabled in config.");
            return;
        }

        try {
            URL url = new URL("https://api.github.com/repos/WojciechStawicki/BiomeAnnouncerPL/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("User-Agent", "BiomeAnnouncer/" + currentVersion);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Extract version from tag_name using regex
                Pattern pattern = Pattern.compile("\"tag_name\":\\s*\"([^\"]+)\"");
                Matcher matcher = pattern.matcher(response.toString());

                if (matcher.find()) {
                    latestVersion = matcher.group(1).replace("v", ""); // Remove 'v' prefix if present
                    updateAvailable = !currentVersion.equals(latestVersion);

                    if (updateAvailable) {
                        Bukkit.getConsoleSender().sendMessage("[BiomeAnnouncerPL] Dostępna jest nowa aktualizacja!");
                        Bukkit.getConsoleSender().sendMessage("Aktualna wersja: " + currentVersion);
                        Bukkit.getConsoleSender().sendMessage("Najnowsza wersja: " + latestVersion);
                        Bukkit.getConsoleSender().sendMessage("Pobierz: https://github.com/WojciechStawicki/BiomeAnnouncerPL/releases/latest");
                    } else {
                        Bukkit.getConsoleSender().sendMessage("[BiomeAnnouncerPL] Korzystasz z najnowszej wersji!");
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Nie udało się sprawdzić aktualizacji: " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (updateAvailable && player.hasPermission("biomeannouncer.admin")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage(ChatColor.YELLOW + "[BiomeAnnouncerPL] Dostępna jest nowa aktualizacja!!");
                player.sendMessage(ChatColor.YELLOW + "Aktualna wersja: " + currentVersion);
                player.sendMessage(ChatColor.YELLOW + "Najnowsza wersja: " + latestVersion);
                player.sendMessage(ChatColor.YELLOW + "Download: https://github.com/WojciechStawicki/BiomeAnnouncerPL/releases/latest");
            }, 40L);
        }
    }
} 