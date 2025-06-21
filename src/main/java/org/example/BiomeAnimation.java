package org.example;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BiomeAnimation extends BukkitRunnable {
    private final BiomeAnnouncer plugin;
    private final Player player;
    private final String biomeName;
    private final char[] chars;
    private int currentChar = 0;
    private final StringBuilder display = new StringBuilder();

    public BiomeAnimation(BiomeAnnouncer plugin, Player player, String biomeName) {
        this.plugin = plugin;
        this.player = player;
        this.biomeName = biomeName;
        this.chars = biomeName.toCharArray();
    }

    @Override
    public void run() {
        if (currentChar >= chars.length) {
            String colorCode = plugin.getConfig().getString("animation.color", "&f");
            String coloredText = ChatColor.translateAlternateColorCodes('&', colorCode + biomeName);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(coloredText));
            cancel();
            return;
        }

        display.append(chars[currentChar++]);
        if (plugin.getConfig().getBoolean("animation.sound.enabled")) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP,
                    (float) plugin.getConfig().getDouble("animation.sound.volume"),
                    (float) plugin.getConfig().getDouble("animation.sound.pitch"));
        }
        String colorCode = plugin.getConfig().getString("animation.color", "&f");
        String coloredText = ChatColor.translateAlternateColorCodes('&', colorCode + display.toString());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(coloredText));
    }
}