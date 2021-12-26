package me.mattyhd0.ChatColor.Commands;

import me.mattyhd0.ChatColor.CPlayer;
import me.mattyhd0.ChatColor.ChatColor;
import me.mattyhd0.ChatColor.Configuration.Config;
import me.mattyhd0.ChatColor.GUI.ChatColorGUI;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatColorAdminCommand implements CommandExecutor, TabCompleter {

    private String[] completions = {"set", "disable", "reload", "gui", "help"};
    protected ChatColor plugin;

    public ChatColorAdminCommand(ChatColor plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arg) {



        if (arg.length > 0) {
            if (arg[0].equalsIgnoreCase("set")) {
                setPattern(sender, arg);
            }
            else if (arg[0].equalsIgnoreCase("disable")) {
                disable(sender, arg);
            }
            else if (arg[0].equalsIgnoreCase("reload")) {
                reload(sender, arg);
            }
            else if (arg[0].equalsIgnoreCase("gui")) {
                gui(sender, arg);
            }
            else if (arg[0].equalsIgnoreCase("help")) {
                help(sender, arg);
            }
            else {
                unknownCommand(sender);
            }
        }
        else {
            unknownCommand(sender);
        }
        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {

        if(strings.length == 1) {

            return Arrays.asList(completions);

        } else if (strings.length == 2 && (strings[0].equals("disable") || strings[0].equals("gui"))) {

            List<String> completionsList = new ArrayList<>();

            for(Player player: Bukkit.getOnlinePlayers()){
                completionsList.add(player.getName());
            }

            return completionsList;

        } else if (strings.length == 2 && strings[0].equals("set")){

            List<String> completionsList = new ArrayList<>();

            for(Player player: Bukkit.getOnlinePlayers()){
                completionsList.add(player.getName());
            }

            return completionsList;

        } else if (strings.length == 3 && strings[0].equals("set")){

            List<String> patternNames = new ArrayList<>();

            for(Pattern pattern: PatternLoader.getAllPatterns()){

                patternNames.add(pattern.getName(false));

            }

            return patternNames;

        }

        return new ArrayList<>();

    }
    
    public void setPattern(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.set")) {

            if (arg.length == 3) {

                Pattern pattern = PatternLoader.getPatternByName(arg[2]);
                Player player = Bukkit.getPlayer(arg[1]);
                CPlayer cPlayer = new CPlayer(player);

                if(player != null){

                    if (pattern != null) {

                        String patternName = pattern.getName(Config.getBoolean("config.show-pattern-on.list"));

                        if (cPlayer.getPattern() != pattern) {

                            cPlayer.setPattern(pattern);

                            sender.sendMessage(
                                    Config.getMessage("commands.chatcoloradmin.set.selected-pattern-sender")
                                            .replaceAll("%player%", player.getName())
                                            .replaceAll("%pattern%", patternName)

                            );

                            String messagePlayer = Config.getMessage("commands.chatcoloradmin.set.selected-pattern-target")
                                    .replaceAll("%sender%", sender.getName())
                                    .replaceAll("%pattern%", patternName);

                            if(!messagePlayer.equals("")) player.sendMessage(messagePlayer);

                        } else {

                            sender.sendMessage(
                                    Config.getMessage("commands.chatcoloradmin.set.already-in-use")
                                    .replaceAll("%pattern%", patternName)
                                    .replaceAll("%player%", player.getName()
                                    )
                            );

                        }

                    } else {

                        sender.sendMessage(
                                Config.getMessage("commands.chatcoloradmin.set.pattern-not-exist")
                                        .replaceAll("%pattern%", arg[2])
                        );

                    }

                } else {
                    sender.sendMessage(
                            Config.getMessage("other.unknown-player")
                                    .replaceAll("%player%", arg[1])
                    );
                }


            } else {
                sender.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin set <player> <pattern>")
                );
            }

        } else {
            noPermission(sender);
        }
    }
    
    public void disable(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.disable")) {

            if(arg.length == 2){

                Player player = Bukkit.getPlayer(arg[1]);

                if(player != null){

                    CPlayer cPlayer = new CPlayer(player);
                    Pattern pattern = cPlayer.getPattern();

                    if (pattern != null) {

                        String patternName = pattern.getName(Config.getBoolean("config.show-pattern-on.list"));

                        cPlayer.disablePattern();

                        sender.sendMessage(
                                Config.getMessage("commands.chatcoloradmin.disable.pattern-disabled-sender")
                                        .replaceAll("%pattern%", patternName)
                                        .replaceAll("%player%", player.getName())
                        );

                        String messagePlayer = Config.getMessage("commands.chatcoloradmin.disable.pattern-disabled-target")
                                .replaceAll("%pattern%", patternName)
                                .replaceAll("%sender%", sender.getName());

                        if(!messagePlayer.equals("")) player.sendMessage(messagePlayer);

                    } else {
                        sender.sendMessage(
                                Config.getMessage("commands.chatcoloradmin.disable.no-pattern-in-use")
                                        .replaceAll("%player%", player.getName())
                        );
                    }

                } else {
                    sender.sendMessage(
                            Config.getMessage("other.unknown-player")
                                    .replaceAll("%player%", arg[1])
                    );
                }

            } else {

                sender.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin disable <player>")
                );

            }

        } else {
            noPermission(sender);
        }
    }

    public void gui(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.gui")) {

            if(arg.length == 2){

                Player player = Bukkit.getPlayer(arg[1]);

                if(player != null){

                    sender.sendMessage(
                            Config.getMessage("commands.chatcoloradmin.gui.gui-opened-sender")
                                    .replaceAll("%player%", player.getName())
                    );

                    String messagePlayer = Config.getMessage("commands.chatcoloradmin.gui.gui-opened-target")
                            .replaceAll("%sender%", sender.getName());

                    if(!messagePlayer.equals("")) player.sendMessage(messagePlayer);

                    ChatColorGUI.openGui(player);

                } else {
                    sender.sendMessage(Config.getMessage("other.unknown-player").replaceAll("%player%", arg[1]));
                }

            } else {

                sender.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin gui <player>")
                );

            }

        } else {
            this.noPermission(sender);
        }
    }
    
    public void reload(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.reload")) {

            if(arg.length == 1) {

                sender.sendMessage(Config.getMessage("commands.chatcoloradmin.reload.reloading-plugin"));
                Config.loadConfiguration();
                PatternLoader.loadAllPatterns();
                sender.sendMessage(Config.getMessage("commands.chatcoloradmin.reload.plugin-reloaded"));

            } else {
                sender.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin reload")
                );
            }

        } else {
            this.noPermission(sender);
        }

    }
    public void help(CommandSender sender, String[] arg) {

        if(arg.length == 1) {

            for (String line : Config.getMessageList("commands.chatcoloradmin.help")) {
                sender.sendMessage(line);
            }

        } else {

            sender.sendMessage(
                    Config.getMessage("other.correct-usage")
                    .replaceAll("%command%", "/chatcoloradmin help")
            );

        }

    }
    
    public void noPermission(CommandSender sender) {
        sender.sendMessage(
                Config.getMessage("no-permission")
        );
    }

    public void unknownCommand(CommandSender sender) {
        sender.sendMessage(
                Config.getMessage("commands.chatcoloradmin.unknown-command")
        );
    }
}
