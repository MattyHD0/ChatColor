package me.mattyhd0.chatcolor.pattern.manager;

import me.mattyhd0.chatcolor.configuration.YMLFile;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import me.mattyhd0.chatcolor.pattern.type.PatternType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class PatternManager {

    private Map<String, BasePattern> loadedPatternsMap;

    public PatternManager(){

        FileConfiguration patterns = new YMLFile("patterns.yml").get();
        loadedPatternsMap = new HashMap<>();

        for (String key: patterns.getKeys(false)){

            boolean bold = patterns.contains(key + ".bold") && patterns.getBoolean(key + ".bold");
            boolean italic = patterns.contains(key + ".italic") && patterns.getBoolean(key + ".italic");
            boolean underline = patterns.contains(key + ".underline") && patterns.getBoolean(key + ".underline");
            boolean magic = patterns.contains(key + ".magic") && patterns.getBoolean(key + ".magic");
            boolean strikethrough = patterns.contains(key + ".strikethrough") && patterns.getBoolean(key + ".strikethrough");

            PatternType type = PatternType.valueOf(patterns.getString(key+".mode"));
            String permission = patterns.getString(key+".permission");
            ChatColor[] colors = getColors(patterns.getStringList(key+".colors"));

            try {


                TextFormatOptions textFormatOptions = new TextFormatOptions();
                textFormatOptions.of(bold, italic, underline, magic, strikethrough);
                BasePattern pattern = type.buildPattern(key, permission, textFormatOptions, colors);
                /*pattern.setTextFormatOptions(textFormatOptions);
                pattern.setName(key);
                pattern.setColors(colors);
                pattern.setPermission(permission);*/

                if(pattern != null){
                    loadedPatternsMap.put(key, pattern);
                }

            } catch (Exception error){
                error.printStackTrace();
            }


        }

    }

    private ChatColor[] getColors(List<String> colors){

        List<ChatColor> colorsList = new ArrayList<>();

        for(String colorString: colors){

            colorString = colorString.replaceAll("&", "");

            ChatColor color = ChatColor.WHITE;

            if(colorString.length() == 1){
                color = ChatColor.getByChar(colorString.charAt(0));
            } else if (colorString.matches("#[a-zA-Z0-9]{6}")){
                try {
                    color = ChatColor.of(colorString.replace("#", ""));
                } catch (NoSuchMethodError ignored){
                }
            } else {
                color = ChatColor.valueOf(colorString);
            }
            colorsList.add(color);
        }

        return colorsList.toArray(new ChatColor[0]);

    }

    public BasePattern getPatternByName(String name){
        return loadedPatternsMap.get(name);
    }

    public List<BasePattern> getAllPatterns(){
        return loadedPatternsMap.values().stream().collect(Collectors.toList());
    }

}
