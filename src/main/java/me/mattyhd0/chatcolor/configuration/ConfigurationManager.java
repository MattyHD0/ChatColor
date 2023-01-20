package me.mattyhd0.chatcolor.configuration;

public class ConfigurationManager {

    private YMLFile config;
    private YMLFile gui;
    private YMLFile messages;
    private YMLFile patterns;

    private YMLFile data;

    public ConfigurationManager() {
        this.config = new YMLFile("config.yml");
        this.gui = new YMLFile("gui.yml");
        this.messages = new YMLFile("messages.yml");
        this.messages = new YMLFile("patterns.yml");
        this.data = new YMLFile("playerdata.yml");
    }

    public YMLFile getConfig() {
        return config;
    }

    public YMLFile getPatterns() {
        return patterns;
    }

    public YMLFile getMessages() {
        return messages;
    }

    public YMLFile getGui() {
        return gui;
    }

    public YMLFile getData() {
        return data;
    }
    
}
