package me.mattyhd0.ChatColor;

import me.mattyhd0.ChatColor.Configuration.YMLFile;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import me.mattyhd0.ChatColor.Utility.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CPlayer {

    public Player player;

    public CPlayer(Player player){
        this.player = player;
    }

    public void setPattern(Pattern pattern){
        if(ChatColor.MYSQL_CONNECTION == null) {
            YMLFile dataFile = new YMLFile("playerdata.yml");
            FileConfiguration data = dataFile.get();
            data.set("data." + player.getUniqueId().toString(), pattern.getName(false));
            dataFile.save();
        } else {
            try {

                Statement statement = ChatColor.MYSQL_CONNECTION.createStatement();

                statement.execute(
                        formatQuery("INSERT INTO playerdata(uuid, pattern) VALUES('uuid', 'pattern') ON DUPLICATE KEY UPDATE pattern= VALUES(pattern);")
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
        if(ChatColor.MYSQL_CONNECTION == null) {
            YMLFile dataFile = new YMLFile("playerdata.yml");
            FileConfiguration data = dataFile.get();
            data.set("data." + player.getUniqueId().toString(), null);
            dataFile.save();
        } else {
            try {

                Statement statement = ChatColor.MYSQL_CONNECTION.createStatement();

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

    public Pattern getPattern(){
        String pattern = "";
        if(ChatColor.MYSQL_CONNECTION == null) {
            YMLFile dataFile = new YMLFile("playerdata.yml");
            pattern = dataFile.get().getString("data." + player.getUniqueId());
        } else {
            try {

                Statement statement = ChatColor.MYSQL_CONNECTION.createStatement();

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
        return PatternLoader.getPatternByName(pattern);
    }

    public boolean canUsePattern(Pattern pattern){
        return (pattern.getPermission() == null || player.hasPermission(pattern.getPermission()));
    }

    private String formatQuery(String string){

        String uuid = player.getUniqueId().toString();
        String name = player.getName();

        return string
                .replaceAll("\\{uuid}", uuid)
                .replaceAll("\\{player}",name);

    }

}
