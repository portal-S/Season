package ru.portal.seasons.player;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import portal.authBot.bot.config.BotConfig;
import portal.authBot.bot.handler.BotHandler;
import ru.portal.seasons.SeasonPlugin;
import ru.portal.seasons.interfaces.PlayerInfoInterface;
import ru.portal.seasons.interfaces.PlayersMapsInterface;
import ru.portal.seasons.seasons.Season;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerInfo implements PlayersMapsInterface, PlayerInfoInterface {
    private static Map<String, Integer> playersPlayTime = new HashMap<>();
    private static Map<String, Integer> playersJoinTime = new HashMap<>();
    private static Map<String, Integer> playersDayInfo = new HashMap<>();
    private static Map<String, List<String>> playersDaysList = new HashMap<>();

    private static Map<String, Long> playersLastPlay = new HashMap<>();
    private static File file = new File(SeasonPlugin.getSeasonPlugin().getDataFolder(), "users.yml");
    private static YamlConfiguration users = YamlConfiguration.loadConfiguration(file);
    private static ConfigurationSection section = users.getConfigurationSection("users");

    public static void startPlayer(){
        loadPlayersInfo();
        startSchedulerPlay();
        startSchedulerJoin();
        syncInfo();
    }

    private static Player[] getPlayers(){
        return SeasonPlugin.getSeasonPlugin().getServer().getOnlinePlayers();
    }

    private static Set<String> getConfigPlayers(){
        return YamlConfiguration.loadConfiguration(file)
                .getConfigurationSection("users")
                .getKeys(false);
    }

    private static void loadPlayersInfo(){    //загружаем в мапы инфу об игроках
        for(String user : section.getKeys(false)){
            playersDaysList.put(user, section.getStringList(user + ".season." + SeasonPlugin.getSeason().getTitle() + ".rewardDays"));
            playersPlayTime.put(user, section.getInt(user + ".season." + SeasonPlugin.getSeason().getTitle() + ".playTime"));
            playersJoinTime.put(user, section.getInt(user + ".season." + SeasonPlugin.getSeason().getTitle() + ".leaveTime"));
            playersDayInfo.put(user, section.getInt(user + ".season." + SeasonPlugin.getSeason().getTitle() + ".day"));
            playersLastPlay.put(user, section.getLong(user + ".season." + SeasonPlugin.getSeason().getTitle() + ".LastplayMil"));
        }
    }

    public static void unLoadPlayersInfo(){  //загружаем в файл игру об игроках
        for (Map.Entry<String, Integer> entry : playersJoinTime.entrySet()) {
            if(!section.getKeys(false).contains(entry.getKey())){
                section.set(entry.getKey() + ".dsId" , 0);
            }
            section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".rewardDays" , playersDaysList.get(entry.getKey()));
            section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".day" , playersDayInfo.get(entry.getKey()));
            section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".leaveTime" , playersJoinTime.get(entry.getKey()));
            section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".playTime" , playersPlayTime.get(entry.getKey()));
            section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".LastplayMil" , playersLastPlay.get(entry.getKey()));
        }
        try {
            users.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startSchedulerPlay(){  //считаем сыгранные игроком минуты
        Runnable r = new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                for(Player p : getPlayers()){
                    String nick = p.getName();
                    System.out.println(nick);
                    if(hasTimeDay(playersLastPlay.get(p.getName()))){         // если прошли 24 часа с предыдущего засчитанного дня
                        System.out.println(nick + " verify1");
                        if(SeasonPlugin.getSeason().isDsVerify()){
                            if(!BotConfig.getGuardList().contains(p.getName())){ return;}
                        }
                        System.out.println(nick + " verify2");
                        playersPlayTime.put(p.getName(), playersPlayTime.get(p.getName()) + 1);
                        System.out.println(nick + " " + playersPlayTime.get(p.getName()));
                        if(SeasonPlugin.getSeason().getPlayTime() <= playersPlayTime.get(p.getName())){    // если минуты в дне набрались
                            playersDayInfo.put(p.getName(), playersDayInfo.get(p.getName()) + 1);
                            List<String> days = playersDaysList.get(p.getName());
                            days.add(String.valueOf(playersDayInfo.get(p.getName())));
                            playersDaysList.put(p.getName(), days);
                            playersPlayTime.put(p.getName(), 0);
                            playersLastPlay.put(p.getName(), time);
                        }
                    }
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SeasonPlugin.getSeasonPlugin(), r, 1200, 1200);
    }

    private static void syncInfo(){  //синхронизация инфы в файл
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Integer> entry : playersJoinTime.entrySet()) {
                    section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".rewardDays" , playersDaysList.get(entry.getKey()));
                    section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".day" , playersDayInfo.get(entry.getKey()));
                    section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".leaveTime" , playersJoinTime.get(entry.getKey()));
                    section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".playTime" , playersPlayTime.get(entry.getKey()));
                    section.set(entry.getKey() + ".season." + SeasonPlugin.getSeason().getTitle() + ".LastplayMil" , playersLastPlay.get(entry.getKey()));
                }
                try {
                    users.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SeasonPlugin.getSeasonPlugin(), r, 12000, 12000);
    }

    private static void startSchedulerJoin(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<String> players = new ArrayList<>();
                for(Player p : getPlayers()) players.add(p.getName());
                for (Map.Entry<String, Integer> entry : playersJoinTime.entrySet()) {
                    if(!players.contains(entry.getKey())){                            //прибавляем часы, которые чел не играл
                        playersJoinTime.put(entry.getKey(), entry.getValue() + 1);
                    }
                    if(entry.getValue() > SeasonPlugin.getSeason().getJoinTime()){    //обнуляем данные если время прошло
                        playersPlayTime.put(entry.getKey(), 0);
                        playersDayInfo.put(entry.getKey(), 0);
                    }
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SeasonPlugin.getSeasonPlugin(), r, 72000, 72000);
    }

    private static boolean hasTimeDay(long time){
        if(((System.currentTimeMillis() / 1000) - (time / 1000)) > 86400){ return true;} else return false;
    }

    public Map<String, List<String>> getPlayersDaysList() {
        return playersDaysList;
    }

    public static List<String> getDaysList(String name) {
        return section.getStringList(name + ".season." + SeasonPlugin.getSeason().getTitle() + ".rewardDays");
    }

    public Map<String, Integer> getPlayersPlayTime() {
        return playersPlayTime;
    }

    public void setPlayersPlayTime(Map<String, Integer> playersPlayTime) {
        PlayerInfo.playersPlayTime = playersPlayTime;
    }

    public Map<String, Long> getPlayersLastPlay() {
        return playersLastPlay;
    }

    public Map<String, Integer> getPlayersJoinTime() {
        return playersJoinTime;
    }

    public void setPlayersJoinTime(Map<String, Integer> playersJoinTime) {
        PlayerInfo.playersJoinTime = playersJoinTime;
    }

    public Map<String, Integer> getPlayersDayInfo() {
        return playersDayInfo;
    }

    public void setPlayersDayInfo(Map<String, Integer> playersDayInfo) {
        PlayerInfo.playersDayInfo = playersDayInfo;
    }


    public int getPlayDays(String name) { return getPlayersDayInfo().get(name); }

    public int[] getMinHour(String name) {
        int min = (int) ((86400 - ( (System.currentTimeMillis() / 1000) - (playersLastPlay.get(name) / 1000))) / 60);
        int[] time = new int[2];
        if (!hasTimeDay(playersLastPlay.get(name))){
            if(min < 60){
                time[0] = min;
                time[1] = 0;
            } else {
                time[0] = min % 60;
                time[1] = min / 60;
            }
        } else {
            time[0] = 0;
            time[1] = 0;
        }
        return time;
    }

    public static boolean hasWhiteList(String nick){
        if(SeasonPlugin.getConfiguration().getStringList("whitelist").contains(nick))return true;
        else return false;
    }

    public static void addWhiteList(String nick){
        List<String> list = SeasonPlugin.getConfiguration().getStringList("whitelist");
        list.add(nick);
        SeasonPlugin.getConfiguration().set("whitelist", list);
        SeasonPlugin.getSeasonPlugin().saveConfig();
    }

    public int getPlayTime(String name) { return getPlayersPlayTime().get(name); }


}
