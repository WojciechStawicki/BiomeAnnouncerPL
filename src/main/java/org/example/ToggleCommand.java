package org.example;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("biomeannouncer.toggle")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    BiomeAnnouncer.getInstance().getConfig().getString("messages.no-permission")));
            return true;
        }

        BiomeChecker checker = BiomeAnnouncer.getInstance().getBiomeChecker();
        checker.toggleAnnouncer(player.getUniqueId());

        String message = checker.isAnnouncerEnabled(player.getUniqueId()) ?
                BiomeAnnouncer.getInstance().getConfig().getString("messages.disabled") :
                BiomeAnnouncer.getInstance().getConfig().getString("messages.enabled");

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                BiomeAnnouncer.getInstance().getConfig().getString("messages.prefix") + message));

        return true;
    }
}