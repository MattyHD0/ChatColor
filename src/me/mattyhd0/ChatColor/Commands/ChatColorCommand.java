package me.mattyhd0.ChatColor.Commands;

import me.mattyhd0.ChatColor.CPlayer;
import me.mattyhd0.ChatColor.GUI.ChatColorGUI;
import me.mattyhd0.ChatColor.PatternAPI.Pattern;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import me.mattyhd0.ChatColor.Utility.Util;
import me.mattyhd0.ChatColor.Configuration.Config;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.mattyhd0.ChatColor.ChatColor;
import org.bukkit.command.CommandExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatColorCommand implements CommandExecutor, TabCompleter {

    private String[] completions = {"set", "list", "disable", "gui", "help"};
    private ChatColor plugin;
    
    public ChatColorCommand(ChatColor plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arg) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (arg.length > 0) {
                if (arg[0].equalsIgnoreCase("set")) {
                    setPattern(player, arg);
                } else if (arg[0].equalsIgnoreCase("list")) {
                    list(player, arg);
                } else if (arg[0].equalsIgnoreCase("disable")) {
                    disable(player, arg);
                } else if (arg[0].equalsIgnoreCase("gui")) {
                    gui(player, arg);
                } else if (arg[0].equalsIgnoreCase("help")) {
                    help(player, arg);
                } else {
                    unknownCommand(player);
                }
            } else {
                unknownCommand(player);
            }

        } else {
            sender.sendMessage(Config.getMessage("other.bad-executor"));
        }
        return true;
    }
    
    public void setPattern(Player player, String[] arg) {

        if (player.hasPermission("chatcolor.set")) {

            if (arg.length == 2) {

                Pattern pattern = PatternLoader.getPatternByName(arg[1]);
                CPlayer cPlayer = new CPlayer(player);

                if (pattern != null) {

                    String patternPermission = pattern.getPermission();
                    String patternName = pattern.getName(Config.getBoolean("config.show-pattern-on.list"));

                    if (patternPermission != null) {

                        if (player.hasPermission(patternPermission)) {

                            if (cPlayer.getPattern() != pattern) {

                                cPlayer.setPattern(pattern);

                                player.sendMessage(
                                        Config.getMessage("commands.chatcolor.set.selected-pattern")
                                                .replaceAll("%pattern%", patternName)

                                );

                            } else {

                                player.sendMessage(
                                        Config.getMessage("commands.chatcolor.set.already-in-use")
                                                .replaceAll("%pattern%", patternName)
                                );

                            }

                        } else {
                            noPermission(player);
                        }

                    } else {

                        cPlayer.setPattern(pattern);

                        player.sendMessage(
                                Config.getMessage("commands.chatcolor.set.selected-pattern")
                                        .replaceAll("%pattern%", patternName)

                        );

                    }

                } else {

                    player.sendMessage(
                            Config.getMessage("commands.chatcolor.set.pattern-not-exist")
                                    .replaceAll("%pattern%", arg[1])
                    );

                }
            } else {
                player.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor set <pattern>")
                );
            }

        } else {
            noPermission(player);
        }
    }
    
    public void list(Player player, String[] arg) {

        if (player.hasPermission("chatcolor.list")) {

            if(arg.length == 1) {

                if (Config.getBoolean("config.custom-pattern-list.enable")) {

                    for (String line : Config.get().getStringList("config.custom-pattern-list.list")) {
                        player.sendMessage(Util.color(line));
                    }

                } else {

                    String header = Config.getMessage("commands.chatcolor.list.header");
                    String footer = Config.getMessage("commands.chatcolor.list.footer");

                    player.sendMessage(header);

                    for (Pattern pattern: PatternLoader.getAllPatterns()) {

                        String patternName = pattern.getName(Config.getBoolean("config.show-pattern-on.list"));
                        String patternPermission = pattern.getPermission();

                        String string = Config.getMessage("commands.chatcolor.list.pattern");
                        string = string.replaceAll("%pattern%", patternName);

                        if (Config.getBoolean("config.show-pattern-in-list-only-if-has-permissions")) {

                            if (patternPermission != null) {

                                if (player.hasPermission(patternPermission)) {
                                    player.sendMessage(string);
                                }

                            } else {
                                player.sendMessage(string);
                            }

                        } else {
                            player.sendMessage(string);
                        }
                    }

                    player.sendMessage(footer);

                }
            } else {
                player.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor list")
                );
            }
        } else {
            noPermission(player);
        }
    }
    
    public void disable(Player player, String[] arg) {

        Pattern pattern = new CPlayer(player).getPattern();

        if (player.hasPermission("chatcolor.disable")) {

            if(arg.length == 1) {

                if (pattern != null) {

                    String patternName = pattern.getName(Config.getBoolean("config.show-pattern-on.list"));

                    player.sendMessage(
                            Config.getMessage("commands.chatcolor.disable.pattern-disabled")
                                    .replaceAll("%pattern%", patternName)
                    );

                    new CPlayer(player).disablePattern();

                } else {
                    player.sendMessage(Config.getMessage("commands.chatcolor.disable.no-pattern-in-use"));
                }

            } else {
                player.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor disable")
                );
            }
        } else {
            noPermission(player);
        }
    }

    public void gui(Player player, String[] arg) {

        if (player.hasPermission("chatcolor.gui")) {

            if(arg.length == 1) {

                player.sendMessage(Config.getMessage("commands.chatcolor.gui.gui-opened"));
                ChatColorGUI.openGui(player);

            } else {
                player.sendMessage(
                        Config.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor gui")
                );
            }

        } else {
            this.noPermission(player);
        }
    }

    
    public void help(Player player, String[] arg) {
        if(arg.length == 1) {

            for (String line : Config.getMessageList("commands.chatcolor.help")) {
                player.sendMessage(line);
            }

        } else {

            player.sendMessage(
                    Config.getMessage("other.correct-usage")
                            .replaceAll("%command%", "/chatcolor help")
            );

        }
    }
    
    public void noPermission(Player player) {
        player.sendMessage(Config.getMessage("other.no-permission"));
    }
    
    public void unknownCommand(Player player) {
        player.sendMessage(Config.getMessage("commands.chatcolor.unknown-command"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {

        if(strings.length == 1) {

            return Arrays.asList(completions);

        } else if (strings.length == 2 && strings[0].equals("set")) {

            List<String> patternNames = new ArrayList<>();

            if(sender instanceof Player) {

                for (Pattern pattern : PatternLoader.getAllPatterns()) {

                    if (new CPlayer((Player) sender).canUsePattern(pattern)) patternNames.add(pattern.getName(false));

                }

            }

            return patternNames;

        } else {

            return new ArrayList<>();

        }
    }
}
