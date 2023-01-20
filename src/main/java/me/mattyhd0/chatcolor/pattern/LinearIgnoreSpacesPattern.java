package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import net.md_5.bungee.api.ChatColor;

public class LinearIgnoreSpacesPattern extends LinearPattern {

    public LinearIgnoreSpacesPattern(String name, String permission, TextFormatOptions formatOptions, ChatColor... colors){
        super(name, permission, formatOptions, colors);
        setIgnoreSpaces(true);
    }

    @Override
    public String getText(String text) {
        return super.getText(text);
    }

}
