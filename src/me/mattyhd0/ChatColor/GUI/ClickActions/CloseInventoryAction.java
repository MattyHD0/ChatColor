package me.mattyhd0.ChatColor.GUI.ClickActions;

import me.mattyhd0.ChatColor.GUI.ClickActions.API.GuiClickAction;
import org.bukkit.entity.Player;

public class CloseInventoryAction implements GuiClickAction {

    public CloseInventoryAction(){
    }

    @Override
    public void execute(Player player) {

        player.closeInventory();

    }
}
