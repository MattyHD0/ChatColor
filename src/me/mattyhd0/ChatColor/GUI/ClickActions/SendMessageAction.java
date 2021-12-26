package me.mattyhd0.ChatColor.GUI.ClickActions;

import me.mattyhd0.ChatColor.GUI.ClickActions.API.GuiClickAction;
import me.mattyhd0.ChatColor.Utility.Util;
import org.bukkit.entity.Player;

public class SendMessageAction implements GuiClickAction {

    private String message;

    public SendMessageAction(String message){
        this.message = message;
    }

    @Override
    public void execute(Player player) {

        player.sendMessage(Util.color(message));

    }
}
