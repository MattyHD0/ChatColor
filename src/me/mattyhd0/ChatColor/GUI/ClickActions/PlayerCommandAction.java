package me.mattyhd0.ChatColor.GUI.ClickActions;

import me.mattyhd0.ChatColor.GUI.ClickActions.API.GuiClickAction;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
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
