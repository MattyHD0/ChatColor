package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.api.IPattern;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GradientPattern implements IPattern {

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

            return gradient(text, colorsList, bold, italic, underline, magic, strikethrough);
        } catch (NoSuchMethodError e){
            return text;
        }
    }

    private String gradient(String text, Color start, Color end, boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough){

        boolean format = (bold || italic || underline || magic || strikethrough);

        float rStart = start.getRed();
        float gStart = start.getGreen();
        float bStart = start.getBlue();

        float rEnd = end.getRed();
        float gEnd = end.getGreen();
        float bEnd = end.getBlue();

        //System.out.println(MessageFormat.format("START R: {0}, G: {1}, B: {2}", rStart, gStart, bStart));
        //System.out.println(MessageFormat.format("END R: {0}, G: {1}, B: {2}", rEnd, gEnd, bEnd));

        float rMath = (rEnd-rStart)/text.length();
        float gMath = (gEnd-gStart)/text.length();
        float bMath = (bEnd-bStart)/text.length();

        String[] chars = text.split("");
        StringBuilder newText = new StringBuilder();
        int index = 0;

        for (String letter: chars){

            if(format) {
                if (bold) letter = net.md_5.bungee.api.ChatColor.BOLD + letter;
                if (italic) letter = net.md_5.bungee.api.ChatColor.ITALIC + letter;
                if (underline) letter = net.md_5.bungee.api.ChatColor.UNDERLINE + letter;
                if (magic) letter = net.md_5.bungee.api.ChatColor.MAGIC + letter;
                if (strikethrough) letter = net.md_5.bungee.api.ChatColor.STRIKETHROUGH + letter;
            }

            float r = rStart+(rMath*index);
            float g = gStart+(gMath*index);
            float b = bStart+(bMath*index);
            //System.out.println(MessageFormat.format("R: {0}, G: {1}, B: {2}", r, g, b));
            Color color = new Color(r/255, g/255, b/255);
            newText.append(ChatColor.of(color)).append(letter);
            index++;
        }

        return newText.toString();
    }

    public String gradient(String text, List<java.awt.Color> colors, boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough){

        int divisions = colors.size()-1;
        float divideEveryChars = text.length()/divisions > 0 ? (float)text.length()/divisions : 1;
        List<String> substrings = new ArrayList<>();
        StringBuilder finalText = new StringBuilder();

        for(float i = 0; i <= text.length()+divideEveryChars; i += divideEveryChars){

            if(i+divideEveryChars > text.length() && text.length() > 0) {
                int lastSub = substrings.size()-1;
                String latestStr = substrings.get(lastSub);
                substrings.set(lastSub, latestStr+text.substring(Math.round(i)));
                break;
            }

            String sub = text.substring(Math.round(i), Math.round(i+divideEveryChars));
            substrings.add(sub);

        }

        int color = 0;
        for(String s: substrings){

            java.awt.Color color1;
            java.awt.Color color2;

            try {
                color1 = colors.get(color);
                color2 = colors.get(color + 1);
            } catch (IndexOutOfBoundsException e){
                color1 = colors.get(colors.size()-1);
                color2 = colors.get(colors.size()-1);
            }

            finalText.append(
                    gradient(s,
                            color1,
                            color2,
                            bold,
                            italic,
                            underline,
                            magic,
                            strikethrough
                    )
            );

            color++;
        }

        return finalText.toString();
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
