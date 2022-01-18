package me.mattyhd0.ChatColor.UpdateChecker;

import com.sun.istack.internal.NotNull;
import org.bukkit.plugin.Plugin;

public class UpdateChecker {

    private String version;
    private SpigotResource spigotResource;

    public UpdateChecker(@NotNull Plugin plugin, int spigotResourceId){

        version = plugin.getDescription().getVersion();
        spigotResource = SpigotAPI.getSpigotResource(spigotResourceId);

    }

    public boolean isRunningLatestVersion() {
        return version.equals(spigotResource.getCurrentVersion());
    }

    public String getVersion() {
        return version;
    }
    
    public String getLatestVersion() {
        return spigotResource.getCurrentVersion();
    }

    public SpigotResource getSpigotResource() {
        return spigotResource;
    }

    public boolean requestIsValid() {
        return spigotResource != null;
    }

}
