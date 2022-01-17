package me.mattyhd0.ChatColor.Patterns;

import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.Utility.Util;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GradientPattern implements Pattern {

    private String name;
    private List<String> colors;
    private String permission;

    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean magic;
    private boolean strikethrough;

    public GradientPattern(String name, List<String> colors, String permission, boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough) {

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

        List<Color> colorsList = new ArrayList<>();
        try {
            colors.forEach(color -> {

                colorsList.add(
                        net.md_5.bungee.api.ChatColor.of(color).getColor()
                );

            });

            return Util.bukkitGradient(text, colorsList, bold, italic, underline, magic, strikethrough);
        } catch (NoSuchMethodError e){
            return text;
        }
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
