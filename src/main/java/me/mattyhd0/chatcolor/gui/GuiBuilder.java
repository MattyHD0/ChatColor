package me.mattyhd0.chatcolor.gui;

import me.mattyhd0.chatcolor.ChatColor;
import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import me.mattyhd0.chatcolor.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiBuilder {

    public String title;
    public InventoryType inventoryType;
    public byte rows;
    public Map<Integer, List<GuiClickAction>> clickActions = new HashMap<>();
    public Map<Integer, ItemStack> guiItems = new HashMap<>();

    /*
        Constructors
    */

    public GuiBuilder() {

    }

    public GuiBuilder(String title, InventoryType inventoryType){
        this.title = title;
        this.inventoryType = inventoryType;
    }

    public GuiBuilder(String title, int rows){
        this.title = title;
        this.rows = (byte) rows;
    }

    /*
        SETTERS
     */

    public GuiBuilder setTitle(String title){
        this.title = Util.color(title);
        return this;
    }

    public GuiBuilder setRows(int rows){
        this.rows = (byte)rows;
        return this;
    }

    public GuiBuilder setGuiItem(int slot, ItemStack itemStack){
        guiItems.put(slot, itemStack);
        return this;
    }

    public GuiBuilder setGuiItem(int slot, ItemStack itemStack, GuiClickAction... actions){
        guiItems.put(slot, itemStack);
        clickActions.put(slot, Arrays.asList(actions));
        return this;
    }

    public GuiBuilder setGuiItem(int slot, ItemStack itemStack, List<GuiClickAction> actions){
        guiItems.put(slot, itemStack);
        clickActions.put(slot, actions);
        return this;
    }

    public GuiBuilder setClickAction(int slot, GuiClickAction... actions){
        clickActions.put(slot, Arrays.asList(actions));
        return this;
    }

    public GuiBuilder setInventoryType(InventoryType inventoryType){
        this.inventoryType = inventoryType;
        return this;
    }

    /*
        GETTERS
     */

    public String getTitle(){
        return title;
    }

    public byte getRows(){
        return rows;
    }

    public ItemStack getGuiItem(int slot){
        return guiItems.get(slot);
    }

    public Map<Integer, ItemStack> getGuiItems() {
        return guiItems;
    }

    public List<GuiClickAction> getClickActions(int slot){
        return clickActions.get(slot);
    }

    public Map<Integer, List<GuiClickAction>> getGuiClickActions() {
        return clickActions;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    /*
        VOIDS
     */

    public void open(Player player){

        Inventory gui = Bukkit.createInventory(null, 6*9, title);

        if(rows >= 1 && rows <= 6){
            gui = Bukkit.createInventory(null, rows*9, title);
        }

        if(inventoryType != null) {
            gui = Bukkit.createInventory(null, inventoryType, title);
        }

        for(Map.Entry<Integer, ItemStack> entry: guiItems.entrySet()){

            try {
                gui.setItem(entry.getKey(), entry.getValue());
            } catch (ArrayIndexOutOfBoundsException exception){
                exception.printStackTrace();
            }


        }

        player.openInventory(gui);
        GuiListener.setPlayerOpenedGui(player, this);

    }

    public void open(Player player, long delay){

        new BukkitRunnable(){
            @Override
            public void run() {
                open(player);
            }
        }.runTaskLater(ChatColor.getInstance(), delay);


    }

}
