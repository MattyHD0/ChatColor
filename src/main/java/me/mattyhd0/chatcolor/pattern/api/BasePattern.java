package me.mattyhd0.chatcolor.pattern.api;

import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.List;

public abstract class BasePattern {

    private String name;
    private List<ChatColor> colors;
    private TextFormatOptions textFormatOptions;
    private String permission;

    /*public BasePattern(){
        this.name = "undefined";
        this.colors = new ArrayList<>();
        this.textFormatOptions = new TextFormatOptions();
        this.permission = null;
    }*/

    public BasePattern(String name, String permission, TextFormatOptions formatOptions, ChatColor... colors){
        this.name = name;
        this.permission = permission;
        this.colors = Arrays.asList(colors);
        textFormatOptions = formatOptions;
    }

    public abstract String getText(String text);

    public void setName(String name) {
        this.name = name;
    }

    public String getName(boolean formatted){
        if(formatted){
            return getText(name);
        }
        return name;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }

    public void setTextFormatOptions(TextFormatOptions textFormatOptions) {
        this.textFormatOptions = textFormatOptions;
    }

    public TextFormatOptions getTextFormatOptions() {
        return textFormatOptions;
    }

    public void setColors(ChatColor... colors){
        this.colors = Arrays.asList(colors);
    }

    public List<ChatColor> getColors(){
        return colors;
    }

}
