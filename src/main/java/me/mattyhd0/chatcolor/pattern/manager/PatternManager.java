package me.mattyhd0.chatcolor.pattern.manager;

import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import me.mattyhd0.chatcolor.pattern.type.PatternType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class PatternManager {

    private Map<String, BasePattern> loadedPatternsMap;

    public PatternManager(){

        SimpleYMLConfiguration patterns = new SimpleYMLConfiguration("patterns.yml");
        patterns.loadFile();
        loadedPatternsMap = new HashMap<>();

        for (String key: patterns.getKeys(false)){
            load(patterns.getConfigurationSection(key));
        }

    }

    public void load(ConfigurationSection configurationSection){

        me.mattyhd0.chatcolor.ChatColor.getInstance().sendConsoleMessage("&7Loading pattern "+configurationSection.getName()+"...");

        PatternType type = PatternType.SINGLE;
        String patternMode = configurationSection.getString("mode");

        try{
            type  = PatternType.valueOf(patternMode);
        } catch (Exception e){
            me.mattyhd0.chatcolor.ChatColor.getInstance().sendConsoleMessage("&cPattern mode '"+patternMode+"' is invalid using '"+type.toString()+"' instead. valid pattern modes: "+String.join(", ", Arrays.toString(PatternType.values())));
        }

        String permission = configurationSection.getString("permission");
        ChatColor[] colors = getColors(configurationSection.getStringList("colors"));

        try {

            TextFormatOptions textFormatOptions = TextFormatOptions.fromConfigurationSection(configurationSection);
            BasePattern pattern = type.buildPattern(configurationSection.getName(), permission, textFormatOptions, colors);

            if(pattern != null){
                loadedPatternsMap.put(configurationSection.getName(), pattern);
                me.mattyhd0.chatcolor.ChatColor.getInstance().sendConsoleMessage("&7Loaded pattern "+pattern.getName(true)+"&7!");
            }

        } catch (Exception error){
            error.printStackTrace();
        }

    }
    private ChatColor[] getColors(List<String> colors){

        List<ChatColor> colorsList = new ArrayList<>();

        for(String colorString: colors){

            colorString = colorString.replaceAll("&", "");

            ChatColor color = ChatColor.WHITE;

            try {

                if(colorString.length() == 1){
                    color = ChatColor.getByChar(colorString.charAt(0));
                } else if (colorString.matches("#[a-zA-Z0-9]{6}")){
                    try {
                        color = ChatColor.of(colorString);
                    } catch (NoSuchMethodError ignored){}
                } else {
                    color = ChatColor.valueOf(colorString);
                }

            } catch (Exception e){

                me.mattyhd0.chatcolor.ChatColor.getInstance().sendConsoleMessage("&CCannot load color '"+colorString+"' using '"+color.getName()+"' instead, valid formats are: 'f', '&&cf', 'WHITE', '#&cFFFFFF'");

            }


            colorsList.add(color);
        }

        return colorsList.toArray(new ChatColor[0]);

    }

    public BasePattern getPatternByName(String name){
        return loadedPatternsMap.get(name);
    }

    public List<BasePattern> getAllPatterns(){
        return new ArrayList<>(loadedPatternsMap.values());
    }

}
