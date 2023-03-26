package me.mattyhd0.chatcolor.gui.clickaction;

import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.bukkit.entity.Player;

public class SetPatternAction implements GuiClickAction {

    private String patternName;

    public SetPatternAction(String patternName){
        this.patternName = patternName;
    }

    @Override
    public void execute(Player player) {

        BasePattern pattern = ChatColorPlugin.getInstance().getPatternManager().getPatternByName(patternName);
        CPlayer cPlayer = ChatColorPlugin.getInstance().getDataMap().get(player.getUniqueId());
        if(cPlayer != null) cPlayer.setPattern(pattern);
    }
}
