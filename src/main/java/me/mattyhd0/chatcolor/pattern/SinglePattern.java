package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.api.IPattern;
import me.mattyhd0.chatcolor.util.Util;

import java.util.List;

public class SinglePattern implements IPattern {

    private String name;
    private List<String> colors;
    private String permission;

    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean magic;
    private boolean strikethrough;

    public SinglePattern(String name, List<String> colors, String permission, boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough) {

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

        if(bold) text = net.md_5.bungee.api.ChatColor.BOLD+text;
        if(italic) text = net.md_5.bungee.api.ChatColor.ITALIC+text;
        if(underline) text = net.md_5.bungee.api.ChatColor.UNDERLINE+text;
        if(magic) text = net.md_5.bungee.api.ChatColor.MAGIC+text;
        if(strikethrough) text = net.md_5.bungee.api.ChatColor.STRIKETHROUGH+text;

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
