package xyz.benw.plugins.fouriermc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.benw.plugins.fouriermc.command.FourierCommands;
import xyz.benw.plugins.fouriermc.analysis.DescriptiveAnalyzer;
import xyz.benw.plugins.fouriermc.analysis.QuantitativeAnalyzer;
import xyz.benw.plugins.fouriermc.player.PlayerData;
import xyz.benw.plugins.fouriermc.violation.ViolationListener;

import java.util.*;

/**
 * FourierMC
 *
 * A Minecraft Bukkit plugin which provides techniques for detecting suspicious
 * clicking activity.
 *
 * @author bcbwilla
 */
public class FourierMC extends JavaPlugin {

    private Map<UUID, PlayerData> playerDataMap = new HashMap<UUID, PlayerData>();


    private long checkInterval; // How often to run an analysis on data
    private long samplePeriod;  // Time (in ticks) between each sample
    private int maxDataLength;  // The length of the data array to analyze
    private boolean debug;      // Log everything if true
    private boolean logSignals; // Log clicking signals to file


    @Override
    public void onEnable(){

        this.saveDefaultConfig();

        FileConfiguration config = getConfig();

        checkInterval = config.getLong("checkinterval");
        samplePeriod = config.getLong("sampleperiod");
        maxDataLength = config.getInt("clickdata.maxdatalength");
        debug = config.getBoolean("debug");
        logSignals = config.getBoolean("logsignals");


        getServer().getPluginManager().registerEvents(new ClickListener(this), this);
        getServer().getPluginManager().registerEvents(new ViolationListener(this), this);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        /* Counts clicks between every samplePeriod */
        scheduler.scheduleSyncRepeatingTask(this, new Collector(this), 0L, samplePeriod);

        /* Performs analysis every checkInterval */
        scheduler.scheduleSyncRepeatingTask(this, new QuantitativeAnalyzer(this), 0L, checkInterval);

        if(debug) {
            /* Logs some basic descriptive statistics */
            scheduler.scheduleSyncRepeatingTask(this, new DescriptiveAnalyzer(this), 0L, 500L);
        }

        this.getCommand("fmc").setExecutor(new FourierCommands(this));

        getLogger().info("Periodically awesome. [ALPHA - testing only?]");
    }

    @Override
    public void onDisable(){
    }

    public long getSamplePeriod() {
        return samplePeriod;
    }

    public int getMaxDataLength() {
        return maxDataLength;
    }

    public boolean getLogSignals() {
        return logSignals;
    }

    public boolean getDebug() {
        return debug;
    }

    public Map<UUID, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public PlayerData getPlayerData(UUID playerId) {
        if(playerDataMap.containsKey(playerId)) {
            return playerDataMap.get(playerId);
        } else {
            PlayerData pd = new PlayerData(this);
            this.playerDataMap.put(playerId, pd);
            return pd;
        }
    }
}
