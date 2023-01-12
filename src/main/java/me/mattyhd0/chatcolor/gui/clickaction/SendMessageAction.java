package me.mattyhd0.chatcolor.gui.clickaction;

import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import me.mattyhd0.chatcolor.util.Util;
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
