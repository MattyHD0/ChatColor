package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import me.mattyhd0.chatcolor.util.Util;
import net.md_5.bungee.api.ChatColor;

public class RandomPattern extends BasePattern {

    public RandomPattern(String name, String permission, TextFormatOptions formatOptions, ChatColor... colors) {
        super(name, permission, formatOptions, colors);
    }

    @Override
    public String getText(String text) {

        StringBuilder finalString = new StringBuilder();

        for (String character: text.split("")){

            int num = Math.round((float)Math.random()*(getColors().size()-1));
            ChatColor randomColor = getColors().get(num);

            character = getTextFormatOptions().setFormat(character);

            finalString.append(randomColor).append(character);

        }

        return Util.color(finalString.toString());
    }

}
