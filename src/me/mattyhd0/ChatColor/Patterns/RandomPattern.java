package me.mattyhd0.ChatColor.Patterns;

import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.Utility.Util;

import java.util.List;

public class RandomPattern implements Pattern {

    private String name;
    private List<String> colors;
    private String permission;

    public RandomPattern(String name, List<String> colors, String permission) {

        this.name = name;
        this.colors = colors;
        this.permission = permission;

    }

    @Override
    public String getText(String text) {

        String finalString = "";

        for (String character: text.split("")){

            int num = Math.round((float)Math.random()*(colors.size()-1));
            String randomColor = "&"+colors.get(num);

            finalString = finalString + randomColor + character;

        }

        return Util.color(finalString);
    }

    @Override
    public String getName(boolean formatted) {

        String name = this.name;

        if(formatted){
            name = getText(name);
        }

        return name;
    }


    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public List<String> getColors() {
        return this.colors;
    }

}
