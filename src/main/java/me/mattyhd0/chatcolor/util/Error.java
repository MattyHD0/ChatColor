package me.mattyhd0.chatcolor.util;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class Error {

    public List<String> errors = new ArrayList<>();
    String description;

    public Error(){

    }

    public void addError(String error){
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void showErrorsBukkit(){
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        console.sendMessage("");
        console.sendMessage(
                Util.color("&4[ERROR] &cErrors have been found: (&e{errors_amount}&c)")
                        .replaceAll("\\{errors_amount}", errors.size()+"")
        );
        if(description != null){
            console.sendMessage(Util.color("&e"+description));
        }
        console.sendMessage("");

        for(String string: errors){
            console.sendMessage(Util.color("&c"+string));
        }
        console.sendMessage("");
    }

    public boolean hasErrors(){
        return (errors.size() > 0);
    }
}
