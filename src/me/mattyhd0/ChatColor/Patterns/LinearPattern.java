package me.mattyhd0.ChatColor.Patterns;

import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.Utility.Util;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LinearPattern implements Pattern {

    private String name;
    private List<String> colors;
    private String permission;
    private boolean ignoreSpaces;

    public LinearPattern(String name, List<String> colors, String permission, boolean ignoreSpaces) {

        this.name = name;
        this.colors = colors;
        this.permission = permission;
        this.ignoreSpaces = ignoreSpaces;

    }

    @Override
    public String getText(String text) {

        String[] characters = text.split("");
        String finalString = "";
        int index = 0;

        for (String character : characters) {
            finalString = finalString + "&" + colors.get(index) + character;
            if (index < colors.size() - 1) {

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
