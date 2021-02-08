package ru.portal.seasons;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.portal.seasons.commands.Commands;
import ru.portal.seasons.files.SeasonFiles;
import ru.portal.seasons.player.PlayerInfo;
import ru.portal.seasons.player.PlayerListener;
import ru.portal.seasons.seasons.Season;

import java.io.File;
import java.util.logging.Logger;

public class SeasonPlugin  extends JavaPlugin {
    private static SeasonPlugin plugin;
    private static FileConfiguration configuration;
    private static Logger logger;
    private static File users;
    private static Season season;

    public void onEnable() {
        (SeasonPlugin.plugin = this).load();
        season = SeasonFiles.getSeasonsFiles();
        PlayerInfo.startPlayer();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand(season.getCommand()).setExecutor((CommandExecutor)new Commands());
        this.getCommand("season").setExecutor((CommandExecutor)new Commands());
    }

    public void onDisable(){
        PlayerInfo.unLoadPlayersInfo();
    }

    public static SeasonPlugin getSeasonPlugin(){
        if (SeasonPlugin.plugin == null) SeasonPlugin.plugin = new SeasonPlugin();
        return SeasonPlugin.plugin;
    }

    public static FileConfiguration getConfiguration() {
        return SeasonPlugin.configuration;
    }

    public void info(final String msg) {
        SeasonPlugin.logger.info("[SeasonPlugin] " + msg);
    }

    public static Season getSeason(){
        return season;
    }

    public void load() {
        this.info("Loading config..");
        final File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
            this.info("Created default config..");
        }

        SeasonPlugin.configuration = this.getConfig();
        this.info("Configuration loaded..");
        try {
            loadSeasonFile();
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSeasonFile() throws Exception{
        File seasonDir = new File(this.getDataFolder(), "/seasons");
        File seasonEx = new File(this.getDataFolder(), "/seasons/example.yml");
        if(!seasonDir.exists() || seasonDir.length() == 0){
            seasonDir.mkdir();
            seasonDir.createNewFile();
            seasonEx.createNewFile();
            YamlConfiguration season = YamlConfiguration.loadConfiguration(seasonEx);
            season.setDefaults(YamlConfiguration.loadConfiguration(this.getResource("example.yml")));
            season.options().copyDefaults(true);
            season.save(seasonEx);
            this.info("Created dir seasons..");
        }
    }

    public void loadUsers() throws Exception{
        users = new File(this.getDataFolder(), "users.yml");
        if(!users.exists()){
           users.createNewFile();
           YamlConfiguration userY = YamlConfiguration.loadConfiguration(users);
           userY.setDefaults(YamlConfiguration.loadConfiguration(this.getResource("users.yml")));
           userY.options().copyDefaults(true);
           userY.save(users);
        }
    }

    static {
        SeasonPlugin.plugin = null;
        logger = Logger.getLogger("Minecraft");
    }

    public static String replace(String s){
        return s.replace("&" , "§");
    }
}
