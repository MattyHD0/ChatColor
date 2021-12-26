package me.mattyhd0.ChatColor.PatternAPI;

import java.util.HashMap;
import java.util.Map;

public class PerGroupPattern {

    //Unused code :(
    public static Map<String, Pattern> perGropPatterns = new HashMap<>();

    public static void setGroupPatter(String group, Pattern pattern){
        perGropPatterns.put(group, pattern);
    }

    public static Pattern getGroupPattern(String group){
        return perGropPatterns.get(group);
    }

}
