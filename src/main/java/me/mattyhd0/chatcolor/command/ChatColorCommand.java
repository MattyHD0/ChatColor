package me.mattyhd0.chatcolor.command;

import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.gui.ChatColorGUI;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.util.Util;
import me.mattyhd0.chatcolor.configuration.Config;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.mattyhd0.chatcolor.ChatColor;
import org.bukkit.command.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

public class ChatColorCommand implements CommandExecutor, TabCompleter {

    private ChatColor plugin;
    
    public ChatColorCommand(ChatColor plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arg) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Config.getMessage("other.bad-executor"));
            return true;
        }

        Player player = (Player) sender;

        if (!(arg.length > 0)) {
            unknownCommand(player);
            return true;
        }

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

        return true;
    }
    
    public void setPattern(Player player, String[] arg) {

        if (player.hasPermission("chatcolor.set")) {

            if (arg.length == 2) {

                BasePattern pattern = ChatColor.getInstance().getPatternManager().getPatternByName(arg[1]);
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

                    for (BasePattern pattern: ChatColor.getInstance().getPatternManager().getAllPatterns()) {

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

        BasePattern pattern = new CPlayer(player).getPattern();

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

        List<String> completions = new ArrayList<>();

        if(strings.length == 1) {


            completions.add("set");
            completions.add("disable");
            completions.add("gui");
            completions.add("list");
            completions.add("help");

            return completions;

        } else if (strings.length == 2 && strings[0].equals("set")) {

            for (BasePattern pattern : ChatColor.getInstance().getPatternManager().getAllPatterns()) {

                if (new CPlayer((Player) sender).canUsePattern(pattern)) completions.add(pattern.getName(false));

            }

            return completions;

        } else {

            return completions;

        }
    }
}
