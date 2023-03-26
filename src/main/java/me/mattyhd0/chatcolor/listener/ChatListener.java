package me.mattyhd0.chatcolor.listener;

import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.MyChatColor;
import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.bukkit.ChatColor;
import org.bukkit.event.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements EventExecutor {
    private ChatColorPlugin plugin;

    public ChatListener(ChatColorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {

        if(event instanceof AsyncPlayerChatEvent){
            onChat((AsyncPlayerChatEvent) event);
        }

    }

    public void onChat(AsyncPlayerChatEvent event) {

        SimpleYMLConfiguration config = ChatColorPlugin.getInstance().getConfigurationManager().getConfig();

        Player player = event.getPlayer();

        CPlayer cPlayer = plugin.getDataMap().get(player.getUniqueId());

        if(config.getBoolean("config.translate-chat-colors")){
            event.setMessage(
                    MyChatColor.translateAlternateColorCodes(event.getMessage(), player)
            );
        }
        if(cPlayer != null) {

            BasePattern pattern = cPlayer.getPattern();

            if (pattern != null) {

                boolean showPatternIfHasPerm = config.getBoolean("config.show-pattern-only-if-has-permissions");
                String message = ChatColor.stripColor(event.getMessage());
                String coloredMessage = pattern.getText(message);

                if (showPatternIfHasPerm && cPlayer.canUsePattern(pattern)) {
                    event.setMessage(coloredMessage);
                } else if (!showPatternIfHasPerm) {
                    event.setMessage(coloredMessage);
                }

            }

            cPlayer.setLastMessages(event.getMessage());

        }

    }

}
