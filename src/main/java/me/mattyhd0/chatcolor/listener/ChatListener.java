package me.mattyhd0.chatcolor.listener;

import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.MyChatColor;
import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {

        SimpleYMLConfiguration config = ChatColorPlugin.getInstance().getConfigurationManager().getConfig();

        Player player = event.getPlayer();
        CPlayer cPlayer = new CPlayer(player);
        BasePattern pattern = cPlayer.getPattern();
        cPlayer.setLastMessages(event.getMessage());

        if(config.getBoolean("config.translate-chat-colors")){
            event.setMessage(
                    MyChatColor.translateAlternateColorCodes(event.getMessage(), player)
            );
        }

        if (pattern != null) {

            boolean showPatternIfHasPerm = config.getBoolean("config.show-pattern-only-if-has-permissions");
            String message = ChatColor.stripColor(event.getMessage());
            String coloredMessage = pattern.getText(message);

            if (showPatternIfHasPerm && cPlayer.canUsePattern(pattern)) {
                event.setMessage(coloredMessage);
                cPlayer.setLastMessages(coloredMessage);
            } else if (!showPatternIfHasPerm) {
                event.setMessage(coloredMessage);
                cPlayer.setLastMessages(coloredMessage);
            }

        }

    }

}
