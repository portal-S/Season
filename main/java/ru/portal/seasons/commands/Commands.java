package ru.portal.seasons.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import portal.authBot.bot.config.BotConfig;
import ru.portal.seasons.SeasonPlugin;
import ru.portal.seasons.gui.GuiHandler;
import ru.portal.seasons.player.PlayerInfo;
import ru.portal.seasons.utils.Handlers;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("You a not a Player");
            return false;
        }
        Player player = (Player) sender;
        if(label.equals(SeasonPlugin.getSeason().getCommand())){
            if(SeasonPlugin.getSeason().isDsVerify() && !(BotConfig.getGuardList().contains(sender.getName()))) {
                player.sendMessage(SeasonPlugin.replace(SeasonPlugin.getConfiguration().getString("no_verify_msg")));
                return false;
            }
            player.openInventory(GuiHandler.newGui(player, 1, player.getInventory()));
        }
        else if(label.equals("season") && (player.isOp()) || PlayerInfo.hasWhiteList(player.getName())){
            if(args.length >= 1){
                if(args[0].equals("reload")){
                    SeasonPlugin.getSeasonPlugin().reloadConfig();
                    SeasonPlugin.getSeasonPlugin().saveConfig();
                    SeasonPlugin.getSeasonPlugin().load();
                    sender.sendMessage(SeasonPlugin.replace("&3Конфигурация перезагружена"));
                    return true;
                } else if (args[0].equals("refresh") && args.length == 2){
                    Handlers.removeUser(args[1]);
                    player.sendMessage(SeasonPlugin.replace("&3Данные игрока " + args[1] + " очищены"));
                }/* else if (args[0].equalsIgnoreCase("whiteAdd") && args.length == 2){
                    PlayerInfo.addWhiteList(args[1]);
                    player.sendMessage(SeasonPlugin.replace("&3Игрок " + args[1] + " добавлен в белый список"));
                }*/
                else {
                    player.sendMessage(SeasonPlugin.replace("&3/season reload -> Перезагружает конфигурацию плагина"));
                    player.sendMessage(SeasonPlugin.replace("&3/season refresh nick -> Обнулить игрока"));
                }
            } else {
                player.sendMessage(SeasonPlugin.replace("&3/season reload -> Перезагружает конфигурацию плагина"));
                player.sendMessage(SeasonPlugin.replace("&3/season refresh nick -> Обнулить игрока"));
            }
        }
        return false;
    }
}
