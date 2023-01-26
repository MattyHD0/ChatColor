package me.mattyhd0.chatcolor.configuration;

import me.mattyhd0.chatcolor.util.Util;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessagesYMLFile extends SimpleYMLConfiguration {

    public MessagesYMLFile(String fileName) {
        super(fileName);
    }

    public MessagesYMLFile(File file) {
        super(file);
    }

    public String getMessage(String key){

        String msg = this.getString("messages." + key);

        if (msg != null) {
            msg = msg.replaceAll("%prefix%", this.getString("messages.prefix"));
            return Util.color(msg);
        }

        return Util.color("&c[ChatColor] Error: Message " + key + " does not exist in messages.yml");

    }

    public List<String> getMessageList(String key){

        String prefix = this.getString("messages.prefix");
        List<String> msgList = this.getStringList("messages." + key);
        List<String> coloredList = new ArrayList<>();

        if (msgList.size() > 0) {

            for(String line: msgList){
                line = line.replaceAll("%prefix%", prefix);
                coloredList.add(Util.color(line));
            }

            return coloredList;

        } else {
            List<String> error = new ArrayList<>();
            error.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c[ChatColor] Error: Message " + key + " does not exist in messages.yml"));
            return error;
        }

    }



}
