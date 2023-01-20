package me.mattyhd0.chatcolor.placeholderapi;

import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.bukkit.entity.Player;
import me.mattyhd0.chatcolor.ChatColor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.plugin.Plugin;

public class ChatColorPlaceholders extends PlaceholderExpansion
{
    private Plugin plugin;
    
    public ChatColorPlaceholders() {
        this.plugin = ChatColor.getInstance();
    }
    
    public boolean canRegister() {
        return true;
    }
    
    public String getAuthor() {
        return "MattyHD0";
    }
    
    public String getIdentifier() {
        return "chatcolor";
    }
    
    public String getRequiredPlugin() {
        return "ChatColor";
    }
    
    public String getVersion() {
        return "1.0";
    }
    
    public String onPlaceholderRequest(Player player, String identifier) {

        CPlayer cPlayer = new CPlayer(player);
        BasePattern pattern = cPlayer.getPattern();

        if(pattern == null) {
            return "";
        }

        switch (identifier){

            case "pattern_name":
                return pattern.getName(false);
            case "pattern_name_formatted":
                return pattern.getName(true);
            case "last_message":
                return cPlayer.getLastMessages();
            default:
                return "";
        }

    }

}
