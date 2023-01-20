package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import net.md_5.bungee.api.ChatColor;

public class LinearPattern extends BasePattern {

    private boolean ignoreSpaces;

    public LinearPattern(String name, String permission, TextFormatOptions formatOptions, ChatColor... colors){
        super(name, permission, formatOptions, colors);
        ignoreSpaces = false;
    }

    @Override
    public String getText(String text) {

        String[] characters = text.split("");
        String finalString = "";
        int index = 0;

        for (String character : characters) {

            finalString = finalString + getColors().get(index) + character;
            if (index < getColors().size() - 1) {
                if(!ignoreSpaces){
                    index++;
                } else {
                    if (!character.equals(" ")) {
                        ++index;
                    }
                }
            } else {
                index = 0;
            }
        }

        return finalString;
    }

    public void setIgnoreSpaces(boolean ignoreSpaces) {
        this.ignoreSpaces = ignoreSpaces;
    }

    public boolean isIgnoreSpaces() {
        return ignoreSpaces;
    }
}
