package me.mattyhd0.ChatColor;

import me.mattyhd0.ChatColor.Commands.ChatColorAdminCommand;
import me.mattyhd0.ChatColor.ConfigUpdater.ConfigVersion;
import me.mattyhd0.ChatColor.ConfigUpdater.ConfigVersionUpdater;
import me.mattyhd0.ChatColor.Configuration.YMLFile;
import me.mattyhd0.ChatColor.GUI.GuiListener;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import me.mattyhd0.ChatColor.UpdateChecker.UpdateChecker;
import me.mattyhd0.ChatColor.Utility.Util;
import me.mattyhd0.ChatColor.bStats.Metrics;
import org.bukkit.command.ConsoleCommandSender;
import me.mattyhd0.ChatColor.Configuration.Config;
import me.mattyhd0.ChatColor.PlaceholderAPI.ChatColorPlaceholders;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import me.mattyhd0.ChatColor.Listeners.StaffJoinListener;
import me.mattyhd0.ChatColor.Listeners.ChatListener;
import me.mattyhd0.ChatColor.Commands.ChatColorCommand;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatColor extends JavaPlugin {

    private static List<String> supportedPlugins = new ArrayList<>();
    private static Plugin plugin;
    private static String prefix;
    public static Connection MYSQL_CONNECTION;
    
    public void onEnable() {
        setPlugin(this);
        prefix = Util.color("&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8]");
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Enabling ChatColor v" + this.getDescription().getVersion()));
        Metrics metrics = new Metrics(this, 11648);
        saySupport("PlaceholderAPI");
        reload();
        updateConfiguration();
        setupListeners();
        setupCommands();
        updateChecker(this, 93186);
        setupPlaceholderAPI();
    }

    public static void reload(){
        Config.loadConfiguration();
        PatternLoader.loadAllPatterns();
        if(MYSQL_CONNECTION != null){
            try {
                MYSQL_CONNECTION.close();
                MYSQL_CONNECTION = null;
            } catch (SQLException ignored){}
        }
        if(Config.getConfig().getBoolean("config.mysql.enable")) mysqlConnection();
    }
    
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Disabling ChatColor v" + this.getDescription().getVersion()));
        if(MYSQL_CONNECTION != null) {
            try {
                MYSQL_CONNECTION.close();
            } catch (SQLException ignored){

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

    private static void setPlugin(Plugin pl) {
        plugin = pl;
    }
    
    public static Plugin get() {
        return plugin;
    }
    
    public void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChatColorPlaceholders().register();
        }
    }

    public void updateConfiguration(){
        ConfigVersionUpdater configVersionUpdater = new ConfigVersionUpdater(new YMLFile("config.yml"));

        configVersionUpdater.addConfigVersion(
                        new ConfigVersion()
                                .set("config.mysql.enable", false)
                                .set("config.mysql.database", "chatcolor")
                                .set("config.mysql.host", "localhost")
                                .set("config.mysql.port", "3306")
                                .set("config.mysql.username", "root")
                                .set("config.mysql.password", "")
        );

        configVersionUpdater.update();


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

    public static void mysqlConnection(){

        FileConfiguration config = Config.getConfig();

        String host     = config.getString("config.mysql.host");
        String port     = config.getString("config.mysql.port");
        String username = config.getString("config.mysql.username");
        String password = config.getString("config.mysql.password");
        String database = config.getString("config.mysql.database");

        try{

            String urlConnection = "jdbc:mysql://{host}:{port}/{database}?user={username}&password={password}"
                    .replaceAll("\\{host}", host)
                    .replaceAll("\\{port}", port)
                    .replaceAll("\\{username}", username)
                    .replaceAll("\\{password}", password)
                    .replaceAll("\\{database}", database);

            MYSQL_CONNECTION = DriverManager.getConnection(urlConnection);

            if(MYSQL_CONNECTION == null) Bukkit.getServer().getConsoleSender().sendMessage(
                    Util.color("&c[ChatColor] There was an error connecting to the MySQL Database")
            );

            Statement statement = MYSQL_CONNECTION.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS playerdata ( uuid varchar(36) NOT NULL, pattern varchar(45) NOT NULL, PRIMARY KEY (uuid) );");

        } catch (SQLException e){
            Bukkit.getServer().getConsoleSender().sendMessage(
                    Util.color("&c[ChatColor] There was an error connecting to the MySQL Database")
            );
            e.printStackTrace();
        }


    }

    public static void main(String[] arg){
        mysqlConnection();
    }

    public static boolean supportPlugin(String plugin){
        return supportedPlugins.contains(plugin);
    }
}
