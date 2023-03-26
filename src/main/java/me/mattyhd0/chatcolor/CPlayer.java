package me.mattyhd0.chatcolor;

import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.bukkit.entity.Player;

public class CPlayer {

    public Player player;
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

}
