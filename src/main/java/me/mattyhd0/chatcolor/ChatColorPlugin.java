package me.mattyhd0.chatcolor;

import me.mattyhd0.chatcolor.command.ChatColorAdminCommand;
import me.mattyhd0.chatcolor.configuration.ConfigurationManager;
import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.gui.GuiListener;
import me.mattyhd0.chatcolor.pattern.manager.PatternManager;
import me.mattyhd0.chatcolor.updatechecker.UpdateChecker;
import me.mattyhd0.chatcolor.util.Util;
import me.mattyhd0.chatcolor.util.Version;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.ConsoleCommandSender;
import me.mattyhd0.chatcolor.placeholderapi.ChatColorPlaceholders;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import me.mattyhd0.chatcolor.listener.ConnectionListener;
import me.mattyhd0.chatcolor.listener.ChatListener;
import me.mattyhd0.chatcolor.command.ChatColorCommand;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatColorPlugin extends JavaPlugin {

    private static ChatColorPlugin INSTANCE;
    private Version serverVersion;
    private PatternManager patternManager;
    private ConfigurationManager configurationManager;
    private List<String> supportedPlugins = new ArrayList<>();

    private String prefix;
    private Metrics metrics;
    private Connection mysqlConnection;
    private HashMap<UUID,CPlayer> dataMap = new HashMap<>();

    public void onEnable() {
        ChatColorPlugin.INSTANCE = this;
        prefix = Util.color("&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8]");
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Enabling ChatColor v" + this.getDescription().getVersion()));
        this.serverVersion = Version.from(getServer());
        metrics = new Metrics(this, 11648);
        saySupport("PlaceholderAPI");
        reload();
        setupListeners();
        setupCommands();
        updateChecker(this, 93186);
        setupPlaceholderAPI();
    }

    public void reload(){
        configurationManager = new ConfigurationManager();
        patternManager = new PatternManager();
        if(mysqlConnection != null){
            try {
                mysqlConnection.close();
                mysqlConnection = null;
            } catch (SQLException ignored){}
        }
        if(configurationManager.getConfig().getBoolean("config.mysql.enable")) openMysqlConnection();
    }
    
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Disabling ChatColor v" + this.getDescription().getVersion()));
        for (CPlayer cPlayer: dataMap.values()){
            cPlayer.saveData();
        }
        if(mysqlConnection != null) {
            try {
                mysqlConnection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void setupListeners(){

        EventPriority priority = configurationManager.getConfig().contains("config.listener-priority") ?
                EventPriority.valueOf(configurationManager.getConfig().getString("config.listener-priority")) :
                EventPriority.LOW;



        getServer().getPluginManager().registerEvent(
                AsyncPlayerChatEvent.class,
                new Listener() {},
                priority,
                new ChatListener(this),
                this

        );
       //getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    public void setupCommands(){
        ChatColorCommand chatColorCommand = new ChatColorCommand(this);
        ChatColorAdminCommand chatColorAdminCommand = new ChatColorAdminCommand(this);
        getCommand("chatcolor").setExecutor(chatColorCommand);
        getCommand("chatcolor").setTabCompleter(chatColorCommand);
        getCommand("chatcoloradmin").setExecutor(chatColorAdminCommand);
        getCommand("chatcoloradmin").setTabCompleter(chatColorAdminCommand);
    }
    
    public void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChatColorPlaceholders().register();
        }
    }

    private void updateChecker(Plugin plugin, int spigotId) {
        if (ChatColorPlugin.getInstance().getConfigurationManager().getConfig().getBoolean("config.update-checker")) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this,()-> {
                UpdateChecker updateChecker = new UpdateChecker(plugin, spigotId);
                ConsoleCommandSender console = Bukkit.getConsoleSender();
                if (updateChecker.requestIsValid()) {
                    if (updateChecker.isRunningLatestVersion()) {
                        String message = Util.color(prefix+" &7You are using the latest version of ChatColor!");
                        console.sendMessage(message);
                    } else {
                        String message = Util.color(prefix+" &7You are using version &a" + updateChecker.getVersion() + "&7 and the latest version is &a" + updateChecker.getLatestVersion());
                        String message2 = Util.color(prefix+" &7You can download the latest version at: &a" + updateChecker.getSpigotResource().getDownloadUrl());
                        console.sendMessage(message);
                        console.sendMessage(message2);
                    }
                } else {
                    String message = Util.color(prefix+" &7Could not verify if you are using the latest version of ChatColor :(");
                    String message2 = Util.color(prefix+" &7You can disable update checker in config.yml file");
                    console.sendMessage(message);
                    console.sendMessage(message2);
                }
            }, 20 * 30,20 * 60 * 60 * 24);
        }
    }

    public void saySupport(String plugin){

        boolean support = Bukkit.getPluginManager().getPlugin(plugin) != null;
        String supportStr = "&cNo";

        if(support) {
            supportStr = "&aYes";
            supportedPlugins.add(plugin);
        }

        Bukkit.getConsoleSender().sendMessage(Util.color( prefix+"&7 "+plugin+" support: "+supportStr));

    }

    public void openMysqlConnection(){

        SimpleYMLConfiguration config = configurationManager.getConfig();

        String host     = config.getString("config.mysql.host");
        String port     = config.getString("config.mysql.port");
        String username = config.getString("config.mysql.username");
        String password = config.getString("config.mysql.password");
        String database = config.getString("config.mysql.database");
        String additionalUrl = config.getString("config.mysql.additional-url","&useSSL=false&&autoReconnect=true");

        try{

            String urlConnection = ("jdbc:mysql://{host}:{port}/{database}?user={username}&password={password}"+additionalUrl)
                    .replaceAll("\\{host}", host)
                    .replaceAll("\\{port}", port)
                    .replaceAll("\\{username}", username)
                    .replaceAll("\\{password}", password)
                    .replaceAll("\\{database}", database);

            mysqlConnection = DriverManager.getConnection(urlConnection);

            if(mysqlConnection == null) Bukkit.getServer().getConsoleSender().sendMessage(
                    Util.color("&c[ChatColor] There was an error connecting to the MySQL Database")
            );

            Statement statement = mysqlConnection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS playerdata ( uuid varchar(36) NOT NULL, pattern varchar(45) NOT NULL, PRIMARY KEY (uuid) );");

        } catch (SQLException e){
            Bukkit.getServer().getConsoleSender().sendMessage(
                    Util.color("&c[ChatColor] There was an error connecting to the MySQL Database")
            );
            e.printStackTrace();
        }


    }

    public boolean supportPlugin(String plugin){
        return supportedPlugins.contains(plugin);
    }

    public static ChatColorPlugin getInstance() {
        return INSTANCE;
    }

    public Connection getMysqlConnection() {
        return mysqlConnection;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public PatternManager getPatternManager() {
        return patternManager;
    }

    public void sendConsoleMessage(String message){
        getServer().getConsoleSender().sendMessage(prefix+" "+Util.color(message));
    }

    public String getPrefix() {
        return prefix;
    }

    public HashMap<UUID, CPlayer> getDataMap() {
        return dataMap;
    }
}
