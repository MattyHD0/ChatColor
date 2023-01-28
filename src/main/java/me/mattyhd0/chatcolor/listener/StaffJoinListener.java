package me.mattyhd0.chatcolor.listener;

import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.updatechecker.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class StaffJoinListener implements Listener {
    @EventHandler
    public void onStaffJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if (player.hasPermission("chatcolor.updatenotify") && ChatColorPlugin.getInstance().getConfigurationManager().getConfig().getBoolean("config.update-checker")) {

            UpdateChecker updateChecker = new UpdateChecker(ChatColorPlugin.getInstance(), 93186);

            if (updateChecker.requestIsValid()) {

                if (!updateChecker.isRunningLatestVersion()) {
                    String message = ChatColor.translateAlternateColorCodes('&', "&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8] &7You are using version &a" + updateChecker.getVersion() + "&7 and the latest version is &a" + updateChecker.getLatestVersion());
                    String message2 = ChatColor.translateAlternateColorCodes('&', "&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8] &7You can download the latest version at: &a" + updateChecker.getSpigotResource().getDownloadUrl());
                    player.sendMessage(message);
                    player.sendMessage(message2);
                }

            } else {

                String message = ChatColor.translateAlternateColorCodes('&', "&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8] &7Could not verify if you are using the latest version of ChatColor :(");
                String message2 = ChatColor.translateAlternateColorCodes('&', "&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8] &7You can disable update checker in config.yml file");
                player.sendMessage(message);
                player.sendMessage(message2);

            }
        }
    }
}
