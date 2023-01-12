package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.api.IPattern;
import me.mattyhd0.chatcolor.util.Util;

import java.util.List;

public class RandomPattern implements IPattern {

    private String name;
    private List<String> colors;
    private String permission;

    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean magic;
    private boolean strikethrough;

    public RandomPattern(String name, List<String> colors, String permission, boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough) {

        this.name = name;
        this.colors = colors;
        this.permission = permission;

        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.magic = magic;
        this.strikethrough = strikethrough;

    }

    @Override
    public String getText(String text) {

        String finalString = "";
        boolean format = (bold || italic || underline || magic || strikethrough);

        for (String character: text.split("")){

            int num = Math.round((float)Math.random()*(colors.size()-1));
            String randomColor = "&"+colors.get(num);

            if(format) {
                if (bold) character = net.md_5.bungee.api.ChatColor.BOLD + character;
                if (italic) character = net.md_5.bungee.api.ChatColor.ITALIC + character;
                if (underline) character = net.md_5.bungee.api.ChatColor.UNDERLINE + character;
                if (magic) character = net.md_5.bungee.api.ChatColor.MAGIC + character;
                if (strikethrough) character = net.md_5.bungee.api.ChatColor.STRIKETHROUGH + character;
            }

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
