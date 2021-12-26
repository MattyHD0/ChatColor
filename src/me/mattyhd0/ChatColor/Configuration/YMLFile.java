package me.mattyhd0.ChatColor.Configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import me.mattyhd0.ChatColor.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

public class YMLFile {

    private String fileName;
    private File file;
    private FileConfiguration fileConfiguration;
    
    public YMLFile(String fileName) {
        this.fileName = fileName;
        this.file = new File(ChatColor.get().getDataFolder(), this.fileName);
        this.check();
    }
    
    public YMLFile(File file) {
        this.fileName = file.getName();
        this.file = file;
        this.check();
    }
    
    public FileConfiguration get() {
        return this.fileConfiguration;
    }
    
    public void check() {
        if (!this.file.exists()) {
            this.createFile();
        }
        this.loadFile();
    }
    
    public void createFile() {
        this.file.getParentFile().mkdirs();
        ChatColor.get().saveResource(this.fileName, false);
    }
    
    public void loadFile() {
        this.fileConfiguration = new YamlConfiguration();
        try {
            this.fileConfiguration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            this.get().save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
