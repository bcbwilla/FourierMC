package xyz.benw.plugins.fouriermc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.benw.plugins.fouriermc.DataAnalysis.DescriptiveAnalyzer;
import xyz.benw.plugins.fouriermc.DataAnalysis.QuantitativeAnalyzer;
import xyz.benw.plugins.fouriermc.Violations.Violation;

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

    /* Each player's clicking signal */
    public Map<UUID, IClickData> clickLogger = new HashMap<UUID, IClickData>();
    public Map<UUID, Violation> violationLogger = new HashMap<UUID, Violation>();

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

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        /* Counts clicks between every samplePeriod */
        scheduler.scheduleSyncRepeatingTask(this, new Collector(this), 0L, samplePeriod);

        /* Performs analysis every checkInterval */
        scheduler.scheduleSyncRepeatingTask(this, new QuantitativeAnalyzer(this), 0L, checkInterval);

        if(debug) {
            /* Logs some basic descriptive statistics */
            scheduler.scheduleSyncRepeatingTask(this, new DescriptiveAnalyzer(this), 0L, 500L);
        }

        getLogger().info("Periodically awesome. [ALPHA - testing only]");
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

}
