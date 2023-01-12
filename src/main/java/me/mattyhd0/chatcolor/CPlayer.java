package me.mattyhd0.chatcolor;

import me.mattyhd0.chatcolor.configuration.YMLFile;
import me.mattyhd0.chatcolor.pattern.api.IPattern;
import me.mattyhd0.chatcolor.pattern.manager.PatternManager;
import me.mattyhd0.chatcolor.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CPlayer {

    public Player player;
    public static Map<Player, String> lastMessages = new HashMap<>();

    public CPlayer(Player player){
        this.player = player;
    }

    public void setPattern(IPattern pattern){
        if(ChatColor.getInstance().getMysqlConnection() == null) {
            YMLFile dataFile = new YMLFile("playerdata.yml");
            FileConfiguration data = dataFile.get();
            data.set("data." + player.getUniqueId().toString(), pattern.getName(false));
            dataFile.save();
        } else {
            try {

                Statement statement = ChatColor.getInstance().getMysqlConnection().createStatement();

                statement.execute(
                        formatQuery("INSERT INTO playerdata(uuid, pattern) VALUES('{uuid}', '{pattern}') ON DUPLICATE KEY UPDATE pattern= VALUES(pattern);", pattern)
                );

            } catch (SQLException e){

                Bukkit.getServer().getConsoleSender().sendMessage(
                        Util.color(
                                formatQuery("&c[ChatColor] An error occurred while trying to set the pattern of {uuid} ({player}) via MySQL")
                                )
                );
                e.printStackTrace();

            }
        }
    }

    public void disablePattern(){
        if(ChatColor.getInstance().getMysqlConnection() == null) {
            YMLFile dataFile = new YMLFile("playerdata.yml");
            FileConfiguration data = dataFile.get();
            data.set("data." + player.getUniqueId().toString(), null);
            dataFile.save();
        } else {
            try {

                Statement statement = ChatColor.getInstance().getMysqlConnection().createStatement();

                statement.execute(
                        formatQuery("DELETE FROM playerdata WHERE uuid = '{uuid}';")
                );

            } catch (SQLException e){

                Bukkit.getServer().getConsoleSender().sendMessage(
                        Util.color(
                                formatQuery("&c[ChatColor] An error occurred while trying to remove the pattern from {uuid} ({player}) via MySQL")
                                )
                );
                e.printStackTrace();

            }
        }
    }

    public IPattern getPattern(){
        String pattern = "";
        if(ChatColor.getInstance().getMysqlConnection() == null) {
            YMLFile dataFile = new YMLFile("playerdata.yml");
            pattern = dataFile.get().getString("data." + player.getUniqueId());
        } else {
            try {

                Statement statement = ChatColor.getInstance().getMysqlConnection().createStatement();

                ResultSet resultSet = statement.executeQuery(
                        formatQuery("SELECT * FROM playerdata WHERE uuid = '{uuid}';")
                );

                while (resultSet.next()) {
                    pattern = resultSet.getString("pattern");
                }


            } catch (SQLException e){

                Bukkit.getServer().getConsoleSender().sendMessage(
                        Util.color(
                                formatQuery("&c[ChatColor] An error occurred while trying to get the pattern of {uuid} ({player}) via MySQL")
                                )
                );
                e.printStackTrace();

            }
        }
        return ChatColor.getInstance().getPatternManager().getPatternByName(pattern);
    }

    public boolean canUsePattern(IPattern pattern){
        return (pattern.getPermission() == null || player.hasPermission(pattern.getPermission()));
    }

    public void setLastMessages(String lastMessages) {
        CPlayer.lastMessages.put(this.player, lastMessages);
    }

    public String getLastMessages() {
        String message = CPlayer.lastMessages.get(player);
        return message != null ? message : "";
    }

    private String formatQuery(String string, IPattern pattern){

        String uuid = player.getUniqueId().toString();
        String name = player.getName();

        string = pattern == null ? string : string.replaceAll("\\{pattern}", pattern.getName(false));

        return string
                .replaceAll("\\{uuid}", uuid)
                .replaceAll("\\{player}", name);

    }

    private String formatQuery(String string){
        return formatQuery(string, null);
    }

}
