package me.mattyhd0.ChatColor.GUI.ClickActions;

import me.mattyhd0.ChatColor.CPlayer;
import me.mattyhd0.ChatColor.GUI.ClickActions.API.GuiClickAction;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import org.bukkit.entity.Player;

public class SetPatternAction implements GuiClickAction {

    private String patternName;

    public SetPatternAction(String patternName){
        this.patternName = patternName;
    }

    @Override
    public void execute(Player player) {

        Pattern pattern = PatternLoader.getPatternByName(patternName);
        CPlayer cPlayer = new CPlayer(player);
        cPlayer.setPattern(pattern);

    }
}
