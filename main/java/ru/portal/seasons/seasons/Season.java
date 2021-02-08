package ru.portal.seasons.seasons;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private String startDate;
    private String stopDate;
    private String command;
    private String seasonName;
    private String title;
    private boolean dsVerify;
    private int joinTime;
    private int playTime;
    private int daysCount;
    private List<String> days;
    private YamlConfiguration file;


    public Season(YamlConfiguration season){
        this.file = season;
        this.startDate = season.getString("Season.seasonStart");
        this.stopDate = season.getString("Season.seasonClosed");
        this.command = season.getString("Season.executecommand");
        this.title = season.getString("Season.title");
        this.seasonName = season.getString("Season.name");
        this.dsVerify = season.getBoolean("Season.discord-verify");
        this.joinTime = season.getInt("Season.joincount");
        this.playTime = season.getInt("Season.playertime");
        this.daysCount = season.getConfigurationSection("days").getKeys(false).size();
        this.days = getConfDays(season);
    }

    public String getStartDate(){
        return this.startDate;
    }

    public String getTitle() {
        return title;
    }

    public String getStopDate() {
        return stopDate;
    }

    public String getCommand() {
        return command;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public boolean isDsVerify() {
        return dsVerify;
    }

    public int getJoinTime() {
        return joinTime;
    }

    public int getPlayTime() {
        return playTime;
    }

    public YamlConfiguration getFile() { return file; }

    public int getDaysCount() {
        return daysCount;
    }

    public List<String> getDays() {
        return days;
    }

    private List<String> getConfDays(YamlConfiguration season){
        ConfigurationSection daySec = season.getConfigurationSection("days");
        List<String> days = new ArrayList<>();
        for(String day : daySec.getKeys(false)){
            StringBuilder dayInfo = new StringBuilder();
            dayInfo.append(day + ",");
            dayInfo.append(daySec.getString(day + ".enabled"));
            for(String com : daySec.getStringList(day + ".commands")){
                dayInfo.append("." + com);
            }
            days.add(dayInfo.toString());
        }
        return days;
    }
}
