package me.mattyhd0.ChatColor.Patterns;

import me.NitkaNikita.AdvancedColorAPI.api.types.AdvancedColor;
import me.NitkaNikita.AdvancedColorAPI.api.types.GradientedText;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GradientPattern implements Pattern {

    private String name;
    private List<String> colors;
    private String permission;
    private double colorMerging;

    public GradientPattern(String name, List<String> colors, String permission, double colorMerging) {

        this.name = name;
        this.colors = colors;
        this.permission = permission;
        this.colorMerging = colorMerging;

    }

    @Override
    public String getText(String text) {

        if(Bukkit.getPluginManager().getPlugin("AdvancedColorAPI") != null) {

            ArrayList<AdvancedColor> advancedColors = new ArrayList<>();

            for (String color : this.colors) {
                advancedColors.add(new AdvancedColor(color.replaceAll("#", "")));
            }

            text = GradientedText.generateGradient(text, advancedColors, colorMerging).getFullText().toLegacyText();

        }

        return text;
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
