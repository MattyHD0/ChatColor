package me.mattyhd0.chatcolor.command;

import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.configuration.ConfigurationManager;
import me.mattyhd0.chatcolor.configuration.MessagesYMLFile;
import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.gui.ChatColorGUI;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.util.Util;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.mattyhd0.chatcolor.ChatColorPlugin;
import org.bukkit.command.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

public class ChatColorCommand implements CommandExecutor, TabCompleter {

    private ChatColorPlugin plugin;
    
    public ChatColorCommand(ChatColorPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arg) {

        MessagesYMLFile messagesYMLFile = ChatColorPlugin.getInstance().getConfigurationManager().getMessages();

        if (!(sender instanceof Player)) {
            sender.sendMessage(messagesYMLFile.getMessage("other.bad-executor"));
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

        ConfigurationManager configurationManager = ChatColorPlugin.getInstance().getConfigurationManager();
        MessagesYMLFile messagesYMLFile = configurationManager.getMessages();

        if (player.hasPermission("chatcolor.set")) {

            if (arg.length == 2) {

                BasePattern pattern = ChatColorPlugin.getInstance().getPatternManager().getPatternByName(arg[1]);
                CPlayer cPlayer = new CPlayer(player);

                if (pattern != null) {

                    String patternPermission = pattern.getPermission();
                    String patternName = pattern.getName(configurationManager.getConfig().getBoolean("config.show-pattern-on.list"));

                    if (patternPermission != null) {
                        if (player.hasPermission(patternPermission)) {
                            if (cPlayer.getPattern() != pattern) {
                                cPlayer.setPattern(pattern);
                                player.sendMessage(
                                        messagesYMLFile.getMessage("commands.chatcolor.set.selected-pattern")
                                                .replaceAll("%pattern%", patternName)

                                );
                            } else {
                                player.sendMessage(
                                        messagesYMLFile.getMessage("commands.chatcolor.set.already-in-use")
                                                .replaceAll("%pattern%", patternName)
                                );
                            }
                        } else {
                            noPermission(player);
                        }
                    } else {
                        cPlayer.setPattern(pattern);
                        player.sendMessage(
                                messagesYMLFile.getMessage("commands.chatcolor.set.selected-pattern")
                                        .replaceAll("%pattern%", patternName)

                        );
                    }
                } else {
                    player.sendMessage(
                            messagesYMLFile.getMessage("commands.chatcolor.set.pattern-not-exist")
                                    .replaceAll("%pattern%", arg[1])
                    );
                }
            } else {
                player.sendMessage(
                        messagesYMLFile.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor set <pattern>")
                );
            }
        } else {
            noPermission(player);
        }
    }
    
    public void list(Player player, String[] arg) {

        ConfigurationManager configurationManager = ChatColorPlugin.getInstance().getConfigurationManager();
        SimpleYMLConfiguration config = configurationManager.getConfig();
        MessagesYMLFile messagesConfig = configurationManager.getMessages();


        if (player.hasPermission("chatcolor.list")) {

            if(arg.length == 1) {

                if (config.getBoolean("config.custom-pattern-list.enable")) {

                    for (String line : config.getStringList("config.custom-pattern-list.list")) {
                        player.sendMessage(Util.color(line));
                    }

                } else {

                    String header = messagesConfig.getMessage("commands.chatcolor.list.header");
                    String footer = messagesConfig.getMessage("commands.chatcolor.list.footer");

                    player.sendMessage(header);

                    for (BasePattern pattern: ChatColorPlugin.getInstance().getPatternManager().getAllPatterns()) {

                        String patternName = pattern.getName(config.getBoolean("config.show-pattern-on.list"));
                        String patternPermission = pattern.getPermission();

                        String string = messagesConfig.getMessage("commands.chatcolor.list.pattern");
                        string = string.replaceAll("%pattern%", patternName);

                        if (config.getBoolean("config.show-pattern-in-list-only-if-has-permissions")) {

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
                        messagesConfig.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor list")
                );
            }
        } else {
            noPermission(player);
        }
    }
    
    public void disable(Player player, String[] arg) {

        BasePattern pattern = new CPlayer(player).getPattern();
        SimpleYMLConfiguration config = ChatColorPlugin.getInstance().getConfigurationManager().getConfig();
        MessagesYMLFile messagesYMLFile = ChatColorPlugin.getInstance().getConfigurationManager().getMessages();

        if (player.hasPermission("chatcolor.disable")) {

            if(arg.length == 1) {

                if (pattern != null) {

                    String patternName = pattern.getName(config.getBoolean("config.show-pattern-on.list"));

                    player.sendMessage(
                            messagesYMLFile.getMessage("commands.chatcolor.disable.pattern-disabled")
                                    .replaceAll("%pattern%", patternName)
                    );

                    new CPlayer(player).disablePattern();

                } else {
                    player.sendMessage(messagesYMLFile.getMessage("commands.chatcolor.disable.no-pattern-in-use"));
                }

            } else {
                player.sendMessage(
                        messagesYMLFile.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor disable")
                );
            }
        } else {
            noPermission(player);
        }
    }

    public void gui(Player player, String[] arg) {

        MessagesYMLFile messagesYMLFile = ChatColorPlugin.getInstance().getConfigurationManager().getMessages();

        if (player.hasPermission("chatcolor.gui")) {

            if(arg.length == 1) {

                player.sendMessage(messagesYMLFile.getMessage("commands.chatcolor.gui.gui-opened"));
                ChatColorGUI.openGui(player);

            } else {
                player.sendMessage(
                        messagesYMLFile.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcolor gui")
                );
            }

        } else {
            this.noPermission(player);
        }
    }

    
    public void help(Player player, String[] arg) {

        MessagesYMLFile messagesYMLFile = ChatColorPlugin.getInstance().getConfigurationManager().getMessages();

        if(arg.length == 1) {

            for (String line : messagesYMLFile.getMessageList("commands.chatcolor.help")) {
                player.sendMessage(line);
            }

        } else {

            player.sendMessage(
                    messagesYMLFile.getMessage("other.correct-usage")
                            .replaceAll("%command%", "/chatcolor help")
            );

        }
    }
    
    public void noPermission(Player player) {
        player.sendMessage(ChatColorPlugin.getInstance().getConfigurationManager().getMessages().getMessage("other.no-permission"));
    }
    
    public void unknownCommand(Player player) {
        player.sendMessage(ChatColorPlugin.getInstance().getConfigurationManager().getMessages().getMessage("commands.chatcolor.unknown-command"));
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

            for (BasePattern pattern : ChatColorPlugin.getInstance().getPatternManager().getAllPatterns()) {

                if (new CPlayer((Player) sender).canUsePattern(pattern)) completions.add(pattern.getName(false));

            }

            return completions;

        } else {

            return completions;

        }
    }
}
