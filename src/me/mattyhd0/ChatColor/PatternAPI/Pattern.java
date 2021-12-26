package me.mattyhd0.ChatColor.PatternAPI;

import java.util.List;

public interface Pattern {

    public String getText(String text);

    public String getName(boolean formatted);

    public String getPermission();

    public List<String> getColors();

}
