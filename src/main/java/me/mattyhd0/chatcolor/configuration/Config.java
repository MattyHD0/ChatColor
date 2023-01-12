package me.mattyhd0.chatcolor.configuration;

import me.mattyhd0.chatcolor.ChatColor;
import me.mattyhd0.chatcolor.util.Util;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private static YMLFile configFile;
    private static YMLFile guiFile;
    private static YMLFile messagesFile;
    private static YMLFile patternsFile;
    private static YMLFile playerdataFile;

    private static FileConfiguration config;
    private static FileConfiguration gui;
    private static FileConfiguration messages;
    private static FileConfiguration patterns;
    private static FileConfiguration playerdata;

    public static void loadConfiguration(){

        configFile = new YMLFile("config.yml");
        guiFile = new YMLFile("gui.yml");
        messagesFile = new YMLFile("messages.yml");
        patternsFile = new YMLFile("patterns.yml");
        playerdataFile = new YMLFile("playerdata.yml");

        config = configFile.get();
        gui = guiFile.get();
        messages = messagesFile.get();
        patterns = patternsFile.get();
        playerdata = playerdataFile.get();

    }

    public static FileConfiguration get() {
        return ChatColor.getInstance().getConfig();
    }
    
    public static String getMessage(String message) {

        String msg = messages.getString("messages." + message);

        if (msg != null) {
            msg = msg.replaceAll("%prefix%", messages.getString("messages.prefix"));
            return Util.color(msg);
        }

        return Util.color("&c[ChatColor] Error: Message " + message + " does not exist in messages.yml");
    }

    public static List<String> getMessageList(String message) {
        YMLFile messagesFile = new YMLFile("messages.yml");
        List<String> msgList = messagesFile.get().getStringList("messages." + message);
        List<String> coloredList = new ArrayList<>();

        if (msgList.size() > 0) {

            for(String line: msgList){

                line = line.replaceAll("%prefix%", messages.getString("messages.prefix"));
                coloredList.add(Util.color(line));

            }

            return coloredList;

        } else {

            List<String> error = new ArrayList<>();
            error.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c[ChatColor] Error: Message " + message + " does not exist in messages.yml"));
            return error;
        }

    }
    
    public static boolean getBoolean(String path) {
        boolean bol = config.getBoolean(path);
        try {
            return bol;
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public static FileConfiguration getConfig(){
        return config;
    }

    public static FileConfiguration getGui(){
        return gui;
    }

    public static FileConfiguration getMessages(){
        return messages;
    }

    public static FileConfiguration getPatterns(){
        return patterns;
    }

    public static FileConfiguration getPlayerdata(){
        return playerdata;
    }

}
