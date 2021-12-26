package me.mattyhd0.ChatColor.PlaceholderAPI;

import me.mattyhd0.ChatColor.CPlayer;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import org.bukkit.entity.Player;
import me.mattyhd0.ChatColor.ChatColor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.plugin.Plugin;

public class ChatColorPlaceholders extends PlaceholderExpansion
{
    private Plugin plugin;
    
    public ChatColorPlaceholders() {
        this.plugin = ChatColor.get();
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
    
    public String onPlaceholderRequest(final Player player, final String identifier) {

        if (player == null) {
            return "";
        }

        Pattern pattern = new CPlayer(player).getPattern();

        if (identifier.equals("pattern_name")) {
            if (pattern != null) {

                return pattern.getName(false);

            }

        } else if (identifier.equals("pattern_name_formatted")) {
            if (pattern != null) {

                return pattern.getName(true);

            }
        }

        return "";
    }
}
