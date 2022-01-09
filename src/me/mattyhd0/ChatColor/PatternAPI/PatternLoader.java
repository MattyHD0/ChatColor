package me.mattyhd0.ChatColor.PatternAPI;

import me.mattyhd0.ChatColor.Configuration.YMLFile;
import me.mattyhd0.ChatColor.Patterns.GradientPattern;
import me.mattyhd0.ChatColor.Patterns.LinearPattern;
import me.mattyhd0.ChatColor.Patterns.RandomPattern;
import me.mattyhd0.ChatColor.Patterns.SinglePattern;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class PatternLoader {

    private static Map<String, Pattern> loadedPatternsMap = new HashMap<>();
    private static List<Pattern> loadedPatternsList = new ArrayList<>();

    public static void loadAllPatterns(){

        FileConfiguration patterns = new YMLFile("patterns.yml").get();
        loadedPatternsList.clear();
        loadedPatternsMap.clear();

        for (String key: patterns.getKeys(false)){

            String mode = patterns.getString(key+".mode");
            String permission = patterns.getString(key+".permission");
            List<String> colors = patterns.getStringList(key+".colors");

            boolean bold = patterns.contains(key + ".bold") && patterns.getBoolean(key + ".bold");
            boolean italic = patterns.contains(key + ".italic") && patterns.getBoolean(key + ".italic");
            boolean underline = patterns.contains(key + ".underline") && patterns.getBoolean(key + ".underline");
            boolean magic = patterns.contains(key + ".magic") && patterns.getBoolean(key + ".magic");
            boolean strikethrough = patterns.contains(key + ".strikethrough") && patterns.getBoolean(key + ".strikethrough");

            Pattern pattern = null;

            switch (mode){
                case "RANDOM":
                    pattern = new RandomPattern(key, colors, permission,
                            bold,
                            italic,
                            underline,
                            magic,
                            strikethrough);
                    break;
                case "SINGLE":
                    pattern = new SinglePattern(key, colors, permission,
                            bold,
                            italic,
                            underline,
                            magic,
                            strikethrough);
                    break;
                case "LINEAR":
                    pattern = new LinearPattern(key, colors, permission, false,
                            bold,
                            italic,
                            underline,
                            magic,
                            strikethrough);
                    break;
                case "LINEAR_IGNORE_SPACES":
                    pattern = new LinearPattern(key, colors, permission, true,
                            bold,
                            italic,
                            underline,
                            magic,
                            strikethrough);
                    break;
                case "GRADIENT":
                    pattern = new GradientPattern(key, colors, permission,
                            bold,
                            italic,
                            underline,
                            magic,
                            strikethrough);
                    break;

            }

            if(pattern != null){
                loadedPatternsMap.put(key, pattern);
                loadedPatternsList.add(pattern);
            }

        }

    }

    public static Pattern getPatternByName(String name){
        return loadedPatternsMap.get(name);
    }

    public static List<Pattern> getAllPatterns(){
        return loadedPatternsList;
    }

}
