package me.mattyhd0.chatcolor;

import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CPlayer {

    public Player player;
    private boolean modified = false;
    private String lastMessage = null;
    private BasePattern basePattern;

    public CPlayer(Player player, BasePattern basePattern){
        this.player = player;
        this.basePattern = basePattern;
    }
    public BasePattern getPattern() {
        return basePattern;
    }
    public void setPattern(BasePattern pattern){
        this.basePattern = pattern;
    }

    public void disablePattern(){
        this.basePattern = null;
    }

    public boolean canUsePattern(BasePattern pattern){
        return (pattern.getPermission() == null || player.hasPermission(pattern.getPermission()));
    }

    public void setLastMessages(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessages() {
        return lastMessage == null ? "" : lastMessage;
    }

    public void saveData(){
        if (ChatColorPlugin.getInstance().getConnectionPool() == null) {
            SimpleYMLConfiguration data = ChatColorPlugin.getInstance().getConfigurationManager().getData();
            data.set("data." + player.getUniqueId(), getPattern() == null ? null : getPattern().getName(false));
            data.save();
        } else {
            try {
                Connection connection = ChatColorPlugin.getInstance().getConnectionPool().getConnection();
                PreparedStatement statement;
                if (getPattern() == null) {
                    statement = connection.prepareStatement(
                            "DELETE FROM playerdata WHERE uuid=?");
                    statement.setString(1, player.getUniqueId().toString());
                } else {
                    statement = connection.prepareStatement(
                            "INSERT INTO playerdata(uuid, pattern) VALUES(?,?) ON DUPLICATE KEY UPDATE pattern= VALUES(pattern)");
                    statement.setString(1, player.getUniqueId().toString());
                    statement.setString(2, getPattern().getName(false));
                }
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                Bukkit.getServer().getConsoleSender().sendMessage(
                        Util.color("&c[ChatColor] An error occurred while trying to set the pattern of "+player.getUniqueId()+" ("+player.getName()+") via MySQL")
                );
                e.printStackTrace();
            }

        }
    }

}
