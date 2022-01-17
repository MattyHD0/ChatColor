package me.mattyhd0.ChatColor.GUI;

import me.mattyhd0.ChatColor.Configuration.Config;
import me.mattyhd0.ChatColor.GUI.ClickActions.API.GuiClickAction;
import me.mattyhd0.ChatColor.GUI.ClickActions.Util.GuiClickActionManager;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import me.mattyhd0.ChatColor.Utility.Placeholders;
import me.mattyhd0.ChatColor.Utility.Util;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ChatColorGUI {

    public static void openGui(Player player){

        FileConfiguration file = Config.getGui();
        FileConfiguration patterns = Config.getPatterns();

        String openSoundStr = file.getString("gui.open-sound");

        Sound sound = null;
        try {
            sound = Sound.valueOf(openSoundStr);
        } catch (IllegalArgumentException | NullPointerException ignored){}

        GuiBuilder builder = new GuiBuilder()
                .setRows(file.getInt("gui.gui.rows"))
                .setTitle(file.getString("gui.gui.title"));

        for(String key : file.getConfigurationSection("gui.items").getKeys(false)){

            key = "gui.items."+key;

            int slot = file.getInt(key+".slot");
            List<Integer> slots = file.getIntegerList(key+".slots");
            List<String> actionsStr = file.getStringList(key+".actions");

            List<GuiClickAction> actions = GuiClickActionManager.getClickActionsFromList(
                    Placeholders.setPlaceholders(actionsStr, null, player)
            );

            if(slots.size() > 0){
                for(int s: slots){
                    builder = builder.setGuiItem(s, Util.getItemFromConfig(file, key), actions);
                }
            } else {
                builder = builder.setGuiItem(slot, Util.getItemFromConfig(file, key), actions);
            }

        }



        for(Pattern pattern : PatternLoader.getAllPatterns()){

            String key = pattern.getName(false)+".gui-item";

            String hasPermission;

            if(pattern.getPermission() == null || player.hasPermission(pattern.getPermission())){
                hasPermission = "has-permission";
            } else {
                hasPermission = "has-not-permission";
            }

            List<String> actionsStr = patterns.getStringList(key+"."+hasPermission+".actions");

            ItemStack itemStack = Util.getItemFromConfig(patterns, key+"."+hasPermission);
            ItemMeta meta = itemStack.getItemMeta();

            if(meta != null) {

                meta.setDisplayName(Placeholders.setPlaceholders(meta.getDisplayName(), pattern, player));
                meta.setLore(Placeholders.setPlaceholders(meta.getLore(), pattern, player));
                itemStack.setItemMeta(meta);

            }


            int slot = patterns.getInt(key+".slot");
            List<GuiClickAction> actions = GuiClickActionManager.getClickActionsFromList(
                    Placeholders.setPlaceholders(actionsStr, pattern, player)
            );


            builder = builder.setGuiItem(slot, itemStack, actions);

        }

        builder.open(player, 1L);
        if(sound != null) player.playSound(player.getLocation(), sound, 1, 1);

    }

}
