package me.mattyhd0.chatcolor.gui.clickaction;

import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ConsoleCommandAction implements GuiClickAction {

    private String command;

    public ConsoleCommandAction(String command){
        this.command = command;
    }

    @Override
    public void execute(Player player) {

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        Bukkit.getServer().dispatchCommand(
                console,
                command.replaceAll("%player%", player.getName())
        );

    }
}
