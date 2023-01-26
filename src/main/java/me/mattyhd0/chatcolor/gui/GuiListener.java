package me.mattyhd0.chatcolor.gui;

import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiListener implements Listener {

    public static List<Player> playersWithGui = new ArrayList<>();
    public static Map<Player, Map<Integer, List<GuiClickAction>>> inventoryActions = new HashMap<>();

    public static void setPlayerOpenedGui(Player player, GuiBuilder builder){
        playersWithGui.add(player);
        inventoryActions.put(player, builder.getGuiClickActions());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        Player player = (Player) event.getWhoClicked();

        if(playersWithGui.contains(player)){

            event.setCancelled(true);

            int slot = event.getSlot();
            List<GuiClickAction> actions = inventoryActions.get(player).get(slot);
            Inventory clickedInventory = event.getClickedInventory();

            if(clickedInventory != null && clickedInventory != player.getInventory() && actions != null) {

                for(GuiClickAction action: actions){

                    if(action != null) action.execute((Player) event.getWhoClicked());

                }

            }


        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event){

        Player player = (Player) event.getPlayer();
        if(inventoryActions.get(player) != null) inventoryActions.remove(player);
        if(playersWithGui.contains(player)) {
            while (playersWithGui.contains(player)) playersWithGui.remove(player);
        }

    }

}
