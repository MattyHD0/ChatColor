package me.mattyhd0.ChatColor;

import me.mattyhd0.ChatColor.Configuration.YMLFile;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CPlayer {

    public Player player;

    public CPlayer(Player player){
        this.player = player;
    }

    public void setPattern(Pattern pattern){
        YMLFile dataFile = new YMLFile("playerdata.yml");
        FileConfiguration data = dataFile.get();
        data.set("data."+player.getUniqueId().toString(), pattern.getName(false));
        dataFile.save();
    }

    public void disablePattern(){
        YMLFile dataFile = new YMLFile("playerdata.yml");
        FileConfiguration data = dataFile.get();
        data.set("data."+player.getUniqueId().toString(), null);
        dataFile.save();
    }

    public Pattern getPattern(){
        YMLFile dataFile = new YMLFile("playerdata.yml");
        return PatternLoader.getPatternByName(dataFile.get().getString("data."+player.getUniqueId()));
    }

    public boolean canUsePattern(Pattern pattern){
        return (pattern.getPermission() == null || player.hasPermission(pattern.getPermission()));
    }

}
