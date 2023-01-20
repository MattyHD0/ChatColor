package me.mattyhd0.chatcolor.pattern;

import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.pattern.format.TextFormatOptions;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GradientPattern extends BasePattern {

    public GradientPattern(String name, String permission, TextFormatOptions formatOptions, ChatColor... colors) {
        super(name, permission, formatOptions, colors);
    }

    @Override
    public String getText(String text) {
        try {
            return gradient(text, getColors(), getTextFormatOptions());
        } catch (NoSuchMethodError ignored){}
        return text;
    }

    private String gradient(String text, ChatColor start, ChatColor end, TextFormatOptions formatOptions){

        Color color1 = start.getColor();
        Color color2 = end.getColor();

        float rStart = color1.getRed();
        float gStart = color1.getGreen();
        float bStart = color1.getBlue();

        float rEnd = color2.getRed();
        float gEnd = color2.getGreen();
        float bEnd = color2.getBlue();

        //System.out.println(MessageFormat.format("START R: {0}, G: {1}, B: {2}", rStart, gStart, bStart));
        //System.out.println(MessageFormat.format("END R: {0}, G: {1}, B: {2}", rEnd, gEnd, bEnd));

        float rMath = (rEnd-rStart)/text.length();
        float gMath = (gEnd-gStart)/text.length();
        float bMath = (bEnd-bStart)/text.length();

        String[] chars = text.split("");
        StringBuilder newText = new StringBuilder();
        int index = 0;

        for (String letter: chars){

            letter = formatOptions.setFormat(letter);

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

    public String gradient(String text, List<ChatColor> colors, TextFormatOptions formatOptions){

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

            ChatColor color1;
            ChatColor color2;

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
                            formatOptions
                    )
            );

            color++;
        }

        return finalText.toString();
    }

}
