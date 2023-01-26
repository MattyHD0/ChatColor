package me.mattyhd0.chatcolor.command;

import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.ChatColor;
import me.mattyhd0.chatcolor.configuration.ConfigurationManager;
import me.mattyhd0.chatcolor.configuration.MessagesYMLFile;
import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.gui.ChatColorGUI;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatColorAdminCommand implements CommandExecutor, TabCompleter {

    protected ChatColor plugin;

    public ChatColorAdminCommand(ChatColor plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arg) {


        if (!(arg.length > 0)) {
            unknownCommand(sender);
            return true;
        }

        if (arg[0].equalsIgnoreCase("set")) {
            setPattern(sender, arg);
        } else if (arg[0].equalsIgnoreCase("disable")) {
            disable(sender, arg);
        } else if (arg[0].equalsIgnoreCase("reload")) {
            reload(sender, arg);
        } else if (arg[0].equalsIgnoreCase("gui")) {
            gui(sender, arg);
        } else if (arg[0].equalsIgnoreCase("help")) {
            help(sender, arg);
        } else {
            unknownCommand(sender);
        }

        return true;

    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {

        List<String> completions = new ArrayList<>();

        if(strings.length == 1) {

            completions.add("set");
            completions.add("disable");
            completions.add("reload");
            completions.add("gui");
            completions.add("help");

            return completions;

        } else if (strings.length == 2 && (strings[0].equals("disable") || strings[0].equals("gui"))) {

            for(Player player: Bukkit.getOnlinePlayers()){
                completions.add(player.getName());
            }

            return completions;

        } else if (strings.length == 2 && strings[0].equals("set")){

            for(Player player: Bukkit.getOnlinePlayers()){
                completions.add(player.getName());
            }

            return completions;

        } else if (strings.length == 3 && strings[0].equals("set")){

            for(BasePattern pattern: ChatColor.getInstance().getPatternManager().getAllPatterns()){

                completions.add(pattern.getName(false));

            }

            return completions;

        }

        return completions;

    }
    
    public void setPattern(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.set")) {

            ConfigurationManager configurationManager = ChatColor.getInstance().getConfigurationManager();
            SimpleYMLConfiguration config = configurationManager.getConfig();
            MessagesYMLFile messagesYMLFile = configurationManager.getMessages();

            if (arg.length == 3) {

                BasePattern pattern = ChatColor.getInstance().getPatternManager().getPatternByName(arg[2]);
                Player player = Bukkit.getPlayer(arg[1]);
                CPlayer cPlayer = new CPlayer(player);

                if(player != null){

                    if (pattern != null) {

                        String patternName = pattern.getName(config.getBoolean("config.show-pattern-on.list"));

                        if (cPlayer.getPattern() != pattern) {

                            cPlayer.setPattern(pattern);

                            sender.sendMessage(
                                    messagesYMLFile.getMessage("commands.chatcoloradmin.set.selected-pattern-sender")
                                            .replaceAll("%player%", player.getName())
                                            .replaceAll("%pattern%", patternName)

                            );

                            String messagePlayer = messagesYMLFile.getMessage("commands.chatcoloradmin.set.selected-pattern-target")
                                    .replaceAll("%sender%", sender.getName())
                                    .replaceAll("%pattern%", patternName);

                            if(!messagePlayer.equals("")) player.sendMessage(messagePlayer);

                        } else {

                            sender.sendMessage(
                                    messagesYMLFile.getMessage("commands.chatcoloradmin.set.already-in-use")
                                    .replaceAll("%pattern%", patternName)
                                    .replaceAll("%player%", player.getName()
                                    )
                            );

                        }

                    } else {

                        sender.sendMessage(
                                messagesYMLFile.getMessage("commands.chatcoloradmin.set.pattern-not-exist")
                                        .replaceAll("%pattern%", arg[2])
                        );

                    }

                } else {
                    sender.sendMessage(
                            messagesYMLFile.getMessage("other.unknown-player")
                                    .replaceAll("%player%", arg[1])
                    );
                }


            } else {
                sender.sendMessage(
                        messagesYMLFile.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin set <player> <pattern>")
                );
            }

        } else {
            noPermission(sender);
        }
    }
    
    public void disable(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.disable")) {

            ConfigurationManager configurationManager = ChatColor.getInstance().getConfigurationManager();
            SimpleYMLConfiguration config = configurationManager.getConfig();
            MessagesYMLFile messagesYMLFile = configurationManager.getMessages();

            if(arg.length == 2){

                Player player = Bukkit.getPlayer(arg[1]);

                if(player != null){

                    CPlayer cPlayer = new CPlayer(player);
                    BasePattern pattern = cPlayer.getPattern();

                    if (pattern != null) {

                        String patternName = pattern.getName(config.getBoolean("config.show-pattern-on.list"));

                        cPlayer.disablePattern();

                        sender.sendMessage(
                                messagesYMLFile.getMessage("commands.chatcoloradmin.disable.pattern-disabled-sender")
                                        .replaceAll("%pattern%", patternName)
                                        .replaceAll("%player%", player.getName())
                        );

                        String messagePlayer = messagesYMLFile.getMessage("commands.chatcoloradmin.disable.pattern-disabled-target")
                                .replaceAll("%pattern%", patternName)
                                .replaceAll("%sender%", sender.getName());

                        if(!messagePlayer.equals("")) player.sendMessage(messagePlayer);

                    } else {
                        sender.sendMessage(
                                messagesYMLFile.getMessage("commands.chatcoloradmin.disable.no-pattern-in-use")
                                        .replaceAll("%player%", player.getName())
                        );
                    }

                } else {
                    sender.sendMessage(
                            messagesYMLFile.getMessage("other.unknown-player")
                                    .replaceAll("%player%", arg[1])
                    );
                }

            } else {

                sender.sendMessage(
                        messagesYMLFile.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin disable <player>")
                );

            }

        } else {
            noPermission(sender);
        }
    }

    public void gui(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.gui")) {

            MessagesYMLFile messagesYMLFile = ChatColor.getInstance().getConfigurationManager().getMessages();

            if(arg.length == 2){

                Player player = Bukkit.getPlayer(arg[1]);

                if(player != null){

                    sender.sendMessage(
                            messagesYMLFile.getMessage("commands.chatcoloradmin.gui.gui-opened-sender")
                                    .replaceAll("%player%", player.getName())
                    );

                    String messagePlayer = messagesYMLFile.getMessage("commands.chatcoloradmin.gui.gui-opened-target")
                            .replaceAll("%sender%", sender.getName());

                    if(!messagePlayer.equals("")) player.sendMessage(messagePlayer);

                    ChatColorGUI.openGui(player);

                } else {
                    sender.sendMessage(messagesYMLFile.getMessage("other.unknown-player").replaceAll("%player%", arg[1]));
                }

            } else {

                sender.sendMessage(
                        messagesYMLFile.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin gui <player>")
                );

            }

        } else {
            this.noPermission(sender);
        }
    }
    
    public void reload(CommandSender sender, String[] arg) {

        if (sender.hasPermission("chatcolor.admin.reload")) {

            ConfigurationManager configurationManager = ChatColor.getInstance().getConfigurationManager();
            MessagesYMLFile messagesYMLFile = configurationManager.getMessages();

            if(arg.length == 1) {

                sender.sendMessage(messagesYMLFile.getMessage("commands.chatcoloradmin.reload.reloading-plugin"));
                ChatColor.getInstance().reload();
                sender.sendMessage(messagesYMLFile.getMessage("commands.chatcoloradmin.reload.plugin-reloaded"));

            } else {
                sender.sendMessage(
                        messagesYMLFile.getMessage("other.correct-usage")
                                .replaceAll("%command%", "/chatcoloradmin reload")
                );
            }

        } else {
            this.noPermission(sender);
        }

    }
    public void help(CommandSender sender, String[] arg) {

        ConfigurationManager configurationManager = ChatColor.getInstance().getConfigurationManager();
        MessagesYMLFile messagesYMLFile = configurationManager.getMessages();

        if(arg.length == 1) {

            for (String line : messagesYMLFile.getMessageList("commands.chatcoloradmin.help")) {
                sender.sendMessage(line);
            }

        } else {

            sender.sendMessage(
                    messagesYMLFile.getMessage("other.correct-usage")
                    .replaceAll("%command%", "/chatcoloradmin help")
            );

        }

    }
    
    public void noPermission(CommandSender sender) {
        sender.sendMessage(
                ChatColor.getInstance().getConfigurationManager().getMessages().getMessage("no-permission")
        );
    }

    public void unknownCommand(CommandSender sender) {
        sender.sendMessage(
                ChatColor.getInstance().getConfigurationManager().getMessages().getMessage("commands.chatcoloradmin.unknown-command")
        );
    }
}
