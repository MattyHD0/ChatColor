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
            double colorMerging = patterns.getDouble(key+".color-merging");

            Pattern pattern = null;

            switch (mode){
                case "RANDOM":
                    pattern = new RandomPattern(key, colors, permission);
                    break;
                case "SINGLE":
                    pattern = new SinglePattern(key, colors, permission);
                    break;
                case "LINEAR":
                    pattern = new LinearPattern(key, colors, permission, false);
                    break;
                case "LINEAR_IGNORE_SPACES":
                    pattern = new LinearPattern(key, colors, permission, true);
                    break;
                case "GRADIENT":
                    pattern = new GradientPattern(key, colors, permission, colorMerging);
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
