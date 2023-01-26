package me.mattyhd0.chatcolor;

import me.mattyhd0.chatcolor.command.ChatColorAdminCommand;
import me.mattyhd0.chatcolor.gui.GuiListener;
import me.mattyhd0.chatcolor.pattern.manager.PatternManager;
import me.mattyhd0.chatcolor.updatechecker.UpdateChecker;
import me.mattyhd0.chatcolor.util.Util;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.ConsoleCommandSender;
import me.mattyhd0.chatcolor.configuration.Config;
import me.mattyhd0.chatcolor.placeholderapi.ChatColorPlaceholders;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import me.mattyhd0.chatcolor.listener.StaffJoinListener;
import me.mattyhd0.chatcolor.listener.ChatListener;
import me.mattyhd0.chatcolor.command.ChatColorCommand;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatColor extends JavaPlugin {

    private static ChatColor INSTANCE;
    private PatternManager patternManager;
    private List<String> supportedPlugins = new ArrayList<>();

    private String prefix;
    private Metrics metrics;
    private Connection mysqlConnection;
    
    public void onEnable() {
        INSTANCE = this;
        prefix = Util.color("&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8]");
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Enabling ChatColor v" + this.getDescription().getVersion()));
        metrics = new Metrics(this, 11648);
        saySupport("PlaceholderAPI");
        reload();
        setupListeners();
        setupCommands();
        updateChecker(this, 93186);
        setupPlaceholderAPI();
    }

    public void reload(){
        Config.loadConfiguration();
        patternManager = new PatternManager();
        if(mysqlConnection != null){
            try {
                mysqlConnection.close();
                mysqlConnection = null;
            } catch (SQLException ignored){}
        }
        if(Config.getConfig().getBoolean("config.mysql.enable")) openMysqlConnection();
    }
    
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Disabling ChatColor v" + this.getDescription().getVersion()));
        if(mysqlConnection != null) {
            try {
                mysqlConnection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void setupListeners(){
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new StaffJoinListener(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    public void setupCommands(){
        getCommand("chatcolor").setExecutor(new ChatColorCommand(this));
        getCommand("chatcolor").setTabCompleter(new ChatColorCommand(this));
        getCommand("chatcoloradmin").setExecutor(new ChatColorAdminCommand(this));
        getCommand("chatcoloradmin").setTabCompleter(new ChatColorAdminCommand(this));
    }
    
    public void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChatColorPlaceholders().register();
        }
    }

    private void updateChecker(Plugin plugin, int spigotId) {
        if (Config.getBoolean("config.update-checker")) {
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

        FileConfiguration config = Config.getConfig();

        String host     = config.getString("config.mysql.host");
        String port     = config.getString("config.mysql.port");
        String username = config.getString("config.mysql.username");
        String password = config.getString("config.mysql.password");
        String database = config.getString("config.mysql.database");

        try{

            String urlConnection = "jdbc:mysql://{host}:{port}/{database}?user={username}&password={password}&autoReconnect=true"
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

    public static ChatColor getInstance() {
        return INSTANCE;
    }

    public Connection getMysqlConnection() {
        return mysqlConnection;
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

}
