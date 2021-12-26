package me.mattyhd0.ChatColor.Utility;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mattyhd0.ChatColor.ChatColor;
import me.mattyhd0.ChatColor.Configuration.Config;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;

import java.util.List;

public class Placeholders {

    public static String setPlaceholders(String text, @Nullable Pattern pattern, @Nullable Player player){

        FileConfiguration config = Config.getGui();

        if(pattern != null){

            text = text.replaceAll("\\{pattern_name}", pattern.getName(false))
                    .replaceAll("\\{pattern_name_formatted}", pattern.getName(true))
                    .replaceAll("\\{example_text}", pattern.getText(config.getString("gui.colored-example-text")));

        }

        if(player != null){

            if(ChatColor.supportPlugin("PlaceholderAPI")){

                text = PlaceholderAPI.setPlaceholders(player, text);

            }

        }

        return text;


    }

    public static List<String> setPlaceholders(List<String> texts, @Nullable Pattern pattern, @Nullable Player player){

        for(int i = 0; i < texts.size(); i++){

            texts.set(i, setPlaceholders(texts.get(i), pattern, player));

        }

        return texts;

    }

}
