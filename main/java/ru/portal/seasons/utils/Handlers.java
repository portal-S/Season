package ru.portal.seasons.utils;

import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.portal.seasons.SeasonPlugin;
import ru.portal.seasons.files.SeasonFiles;
import ru.portal.seasons.interfaces.PlayersMapsInterface;
import ru.portal.seasons.player.PlayerInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Handlers {
    public static List<String> getCmdsDay(int day){
        return SeasonPlugin.getSeason().getFile().getConfigurationSection("days").getStringList(day + ".commands");
    }

    public static void useCMDs(int day, Player player){
        Server server = SeasonPlugin.getSeasonPlugin().getServer();
        for(String s : getCmdsDay(day)){
            server.dispatchCommand(server.getConsoleSender(), s.replace("%player%", player.getName()));
        }
    }

    public static List<String> replaceList(List<String> list){
        for(int i = 0; i < list.size(); i++){
            list.set(i, SeasonPlugin.replace(list.get(i)));
        }
        return list;
    }

    public static void removeUser(String nick){
        PlayersMapsInterface pl = new PlayerInfo();
        pl.getPlayersJoinTime().put(nick, 0);
        pl.getPlayersPlayTime().put(nick, 1);
        pl.getPlayersDayInfo().put(nick,0);
        pl.getPlayersDaysList().put(nick,new ArrayList<String>());
        pl.getPlayersLastPlay().put(nick, (long) 1);
    }
}
