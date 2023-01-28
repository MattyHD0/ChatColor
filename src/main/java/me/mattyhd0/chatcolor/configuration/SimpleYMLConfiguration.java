package me.mattyhd0.chatcolor.configuration;

import me.mattyhd0.chatcolor.ChatColorPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SimpleYMLConfiguration extends YamlConfiguration {

    private File file;

    public SimpleYMLConfiguration(String fileName) {
        super();
        this.file = new File(ChatColorPlugin.getInstance().getDataFolder(), fileName);
        this.check();
    }

    public SimpleYMLConfiguration(File file) {
        this.file = file;
        this.check();
    }
    
    public void check() {
        if (!this.file.exists()) {
            this.createFile();
        }
        this.loadFile();
    }
    
    public void createFile() {
        this.file.getParentFile().mkdirs();
        ChatColorPlugin.getInstance().saveResource(this.file.getName(), false);
    }
    
    public void loadFile() {
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            this.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
