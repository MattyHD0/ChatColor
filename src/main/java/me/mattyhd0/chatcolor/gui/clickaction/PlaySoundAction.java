package me.mattyhd0.chatcolor.gui.clickaction;

import me.mattyhd0.chatcolor.gui.clickaction.api.GuiClickAction;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlaySoundAction implements GuiClickAction {

    public Sound sound;
    public float pitch;
    public float yaw;

    public PlaySoundAction(Sound sound, float pitch, float yaw){

        this.sound = sound;
        this.pitch = pitch;
        this.yaw = yaw;

    }

    @Override
    public void execute(Player player) {

        if(sound != null) player.playSound(player.getLocation(), sound, pitch, yaw);

    }
}
