package me.mattyhd0.ChatColor.GUI.ClickActions.Util;

import me.mattyhd0.ChatColor.GUI.ClickActions.*;
import me.mattyhd0.ChatColor.GUI.ClickActions.API.GuiClickAction;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

public class GuiClickActionManager {

    public static List<GuiClickAction> getClickActionsFromList(List<String> stringList){

        List<GuiClickAction> actions = new ArrayList<>();

        for(String string: stringList){

            actions.add(getClickActionFromString(string));

        }

        return actions;


    }

    public static GuiClickAction getClickActionFromString(String string){

        String[] splitted = string.split(":", 2);

        if(splitted[0].equals("MESSAGE")){

            return new SendMessageAction(splitted[1]);


        } else if (splitted[0].equals("CONSOLE-COMMAND")){

            return new ConsoleCommandAction(splitted[1]);

        } else if (splitted[0].equals("PLAYER-COMMAND")){

            return new PlayerCommandAction(splitted[1]);

        } else if (splitted[0].equals("SET-PATTERN")){

            return new SetPatternAction(splitted[1]);

        } else if (splitted[0].equals("CLOSE-INVENTORY")){

            return new CloseInventoryAction();

        } else if (splitted[0].equals("SOUND")){

            String[] arg = splitted[1].split(" ");
            try {
                return new PlaySoundAction(Sound.valueOf(arg[0]), Float.parseFloat(arg[1]), Float.parseFloat(arg[2]));
            } catch (IllegalArgumentException ignored){}

        }

        return null;

    }

}
