package me.mattyhd0.chatcolor;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyChatColor {

    public static String translateAlternateColorCodes(String textToTranslate, Player player) {
        char altColorChar = '&';
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            int next = i + 1;
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                ChatColor color = ChatColor.getByChar(b[next]);
                if(color == null) continue;
                if (player.hasPermission("chatcolor.*") || hasColorPermission(player, color)) {
                    b[i] = ChatColor.COLOR_CHAR;
                    b[next] = Character.toLowerCase(b[next]);
                } else {
                    b[i] = Character.MIN_VALUE;
                    b[next] = Character.MIN_VALUE;
                }
            }
        }

        return applyHex(new String(b), player).replace(String.valueOf(Character.MIN_VALUE), "");
    }

    public static String applyHex(String textToTranslate, Player player){

        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(textToTranslate);

        if(textToTranslate.length() > 0){

            while (matcher.find()) {

                String color = textToTranslate.substring(matcher.start()+1, matcher.end());

                try {
                    ChatColor md5Color = ChatColor.of(color);
                    if(hasColorPermission(player, md5Color)){
                        textToTranslate = textToTranslate.replace("&"+color, md5Color.toString());
                    } else {
                        textToTranslate = textToTranslate.replace("&"+color, "");
                    }
                } catch (NoSuchMethodError ignored){
                }

                matcher = pattern.matcher(textToTranslate);

            }

        }

        return textToTranslate;

    }

    public static boolean isHex(ChatColor color){
        return (isColor(color) && color.getName().startsWith("#"));
    }

    public static boolean isFormat(ChatColor color){
        return (color.getColor() == null);
    }

    public static boolean isColor(ChatColor color){
        return (color.getColor() != null);
    }


    public static boolean hasColorPermission(Player player, ChatColor color){
        for (String permission: getPermissionsOf(color)){
            if(player.hasPermission(permission)){
                return true;
            }
        }

        return false;
    }

    public static String[] getPermissionsOf(ChatColor color){

        if(isHex(color)){
            return new String[]{ "chatcolor.hex.*", "chatcolor.hex."+color.getName().replace("#", "") };
        }

        if(isColor(color)){
            return new String[]{ "chatcolor.color.*", "chatcolor.color."+color.getName()};
        }

        if(isFormat(color)) {
            return new String[]{ "chatcolor.format.*", "chatcolor.format."+color.getName()};
        }

        return new String[]{"chatcolor.unknown"};

    }

}
