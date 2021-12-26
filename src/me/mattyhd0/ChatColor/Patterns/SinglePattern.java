package me.mattyhd0.ChatColor.Patterns;

import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.Utility.Util;

import java.util.List;

public class SinglePattern implements Pattern {

    private String name;
    private List<String> colors;
    private String permission;

    public SinglePattern(String name, List<String> colors, String permission) {

        this.name = name;
        this.colors = colors;
        this.permission = permission;

    }

    @Override
    public String getText(String text) {
        return Util.color("&"+colors.get(0)+text);
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
