package me.mattyhd0.chatcolor.gui.clickaction;

import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import org.bukkit.entity.Player;

public class PlayerCommandAction implements GuiClickAction {

    private String command;

    public PlayerCommandAction(String command){
        this.command = command;
    }

    @Override
    public void execute(Player player) {

        player.performCommand(command);

    }
}
