package me.mattyhd0.ChatColor.Listeners;

import me.mattyhd0.ChatColor.CPlayer;
import me.mattyhd0.ChatColor.Configuration.Config;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(final AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        CPlayer cPlayer = new CPlayer(player);
        Pattern pattern = cPlayer.getPattern();

        if (pattern != null) {

            boolean showPatternIfHasPerm = Config.getBoolean("config.show-pattern-only-if-has-permissions");
            String message = ChatColor.stripColor(event.getMessage());

            if(showPatternIfHasPerm && cPlayer.canUsePattern(pattern)){
                event.setMessage(pattern.getText(message));
            } else if (!showPatternIfHasPerm){
                event.setMessage(pattern.getText(message));
            }

        }

    }

}
