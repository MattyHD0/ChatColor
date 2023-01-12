package me.mattyhd0.chatcolor.pattern.manager;

import me.mattyhd0.chatcolor.configuration.YMLFile;
import me.mattyhd0.chatcolor.pattern.*;
import me.mattyhd0.chatcolor.pattern.api.IPattern;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class PatternManager {

    private Map<String, IPattern> loadedPatternsMap;

    public PatternManager(){

        FileConfiguration patterns = new YMLFile("patterns.yml").get();
        loadedPatternsMap = new HashMap<>();

        for (String key: patterns.getKeys(false)){

            String mode = patterns.getString(key+".mode");
            String permission = patterns.getString(key+".permission");
            List<String> colors = patterns.getStringList(key+".colors");

            boolean bold = patterns.contains(key + ".bold") && patterns.getBoolean(key + ".bold");
            boolean italic = patterns.contains(key + ".italic") && patterns.getBoolean(key + ".italic");
            boolean underline = patterns.contains(key + ".underline") && patterns.getBoolean(key + ".underline");
            boolean magic = patterns.contains(key + ".magic") && patterns.getBoolean(key + ".magic");
            boolean strikethrough = patterns.contains(key + ".strikethrough") && patterns.getBoolean(key + ".strikethrough");

            IPattern pattern = null;

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
            }

        }

    }

    public IPattern getPatternByName(String name){
        return loadedPatternsMap.get(name);
    }

    public List<IPattern> getAllPatterns(){
        return loadedPatternsMap.values().stream().collect(Collectors.toList());
    }

}
