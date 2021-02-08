package ru.portal.seasons.files;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.portal.seasons.SeasonPlugin;
import ru.portal.seasons.seasons.Season;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SeasonFiles {

    public static Season getSeasonsFiles(){
        File dir = new File (SeasonPlugin.getSeasonPlugin().getDataFolder(), "/seasons");
        File[] files = dir.listFiles();
        YamlConfiguration fileY;
        for(File file : files){
            fileY = YamlConfiguration.loadConfiguration(file);
            if(checkSeasonDate(fileY.getString("Season.seasonStart"), fileY.getString("Season.seasonClosed"))){
                SeasonPlugin.getSeasonPlugin().info("Season from file " + file.getName() + " activated");
                return new Season(fileY);
            }
        }
        SeasonPlugin.getSeasonPlugin().info("Season file not found");
        return new Season(YamlConfiguration.loadConfiguration(new File (SeasonPlugin.getSeasonPlugin().getDataFolder(), "/seasons/example.yml")));
    }

    public static boolean checkSeasonDate(String start, String stop) {
        String[] startDate = start.split("\\.");
        String[] stopDate = stop.split("\\.");
        Calendar date = new GregorianCalendar();
        Date d = new Date(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH));
        if(d.after(new Date(Integer.parseInt(startDate[2]), Integer.parseInt(startDate[1]) ,Integer.parseInt(startDate[0]) - 1))
                && d.before(new Date(Integer.parseInt(stopDate[2]), Integer.parseInt(stopDate[1]) ,Integer.parseInt(stopDate[0]) + 1))){
            return true;
        } else return false;
    }



}
