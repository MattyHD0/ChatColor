package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import net.md_5.bungee.api.ChatColor;

public class SinglePattern extends BasePattern {

    public SinglePattern(String name, String permission, TextFormatOptions formatOptions, ChatColor... colors) {
        super(name, permission, formatOptions, colors);
    }

    @Override
    public String getText(String text) {
        text = getTextFormatOptions().setFormat(text);
        return getColors().get(0)+text;
    }

}
