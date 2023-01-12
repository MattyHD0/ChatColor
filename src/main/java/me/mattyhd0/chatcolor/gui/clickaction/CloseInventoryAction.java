package me.mattyhd0.chatcolor.gui.clickaction;

import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import org.bukkit.entity.Player;

public class CloseInventoryAction implements GuiClickAction {

    public CloseInventoryAction(){
    }

    @Override
    public void execute(Player player) {

        player.closeInventory();

    }
}
