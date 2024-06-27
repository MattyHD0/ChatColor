package me.mattyhd0.chatcolor.configuration;

public class ConfigurationManager {

    private SimpleYMLConfiguration config;
    private SimpleYMLConfiguration gui;
    private MessagesYMLFile messages;
    private SimpleYMLConfiguration patterns;
    private SimpleYMLConfiguration data;
    public ConfigurationManager() {
        this.config = new SimpleYMLConfiguration("config.yml");
        this.gui = new SimpleYMLConfiguration("gui.yml");
        this.messages = new MessagesYMLFile("messages.yml");
        this.patterns = new SimpleYMLConfiguration("patterns.yml");
        this.data = new SimpleYMLConfiguration("playerdata.yml");
    }

    public SimpleYMLConfiguration getConfig() {
        return config;
    }

    public SimpleYMLConfiguration getPatterns() {
        return patterns;
    }

    public MessagesYMLFile getMessages() {
        return messages;
    }

    public SimpleYMLConfiguration getGui() {
        return gui;
    }

    public SimpleYMLConfiguration getData() {
        return data;
    }

}
