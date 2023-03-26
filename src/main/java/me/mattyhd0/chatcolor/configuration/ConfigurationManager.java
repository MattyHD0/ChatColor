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
        /*if(config.getBoolean("config.auto-update-config",true)) {
            updateConfig();
            updateMessages();
        }*/
    }

    private void updateMessages() {
        boolean updated = addConfig(messages,"messages.commands.chatcolor.player-not-loaded","%prefix% &cReconnect to the server. If issue persist, contact an administrator.");
        updated = addConfig(messages,"messages.commands.chatcoloradmin.player-not-loaded","%prefix% &cTarget player is not loaded. Try in some seconds..") || updated;
        if(updated) messages.save();
    }

    private void updateConfig() {
        boolean updated = addConfig(config,"config.mysql.additional-url","&useSSL=false&autoReconnect=true");
        updated = addConfig(config,"config.data-delay",30) || updated;
        updated = addConfig(config,"config.auto-update-config",true) || updated;
        if(updated) config.save();
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

    public boolean addConfig(SimpleYMLConfiguration config, String path, Object value) {
        if(!config.contains(path)) {
            config.set(path,value);
            return true;
        }
        return false;
    }
}
