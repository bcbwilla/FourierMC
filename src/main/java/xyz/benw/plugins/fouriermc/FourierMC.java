package xyz.benw.plugins.fouriermc;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.benw.plugins.fouriermc.DataAnalysis.DescriptiveAnalyzer;
import xyz.benw.plugins.fouriermc.DataAnalysis.QuantitativeAnalyzer;

import java.util.*;

/**
 * FourierMC
 *
 * A Minecraft Bukkit plugin which provides techniques for
 * detecting suspicious clicking activity.
 *
 */
public class FourierMC extends JavaPlugin implements Listener {

    /* Each player's clicking signal */
    public HashMap<UUID, ClickData> clickLogger = new HashMap<UUID, ClickData>();

    private long checkInterval; // How often to run an analysis on data
    private long samplePeriod;  // Time (in ticks) between each sample.

    @Override
    public void onEnable(){

        checkInterval = getConfig().getLong("checkinterval");
        samplePeriod = getConfig().getLong("sampleperiod");

        getServer().getPluginManager().registerEvents(new ClickListener(this), this);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        /* Counts clicks between every samplePeriod */
        scheduler.scheduleSyncRepeatingTask(this, new Collector(this), 0L, samplePeriod);

        /* Logs some basic descriptive statistics */
        scheduler.scheduleSyncRepeatingTask(this, new DescriptiveAnalyzer(this), 0L, 500L);

        /* Performs analysis every checkInterval */
        scheduler.scheduleSyncRepeatingTask(this, new QuantitativeAnalyzer(this), 0L, checkInterval);

        getLogger().info("Periodically awesome.");
    }

    @Override
    public void onDisable(){
        getLogger().info("Goodbye.");
    }

    public long getSamplePeriod() {
        return samplePeriod;
    }

}
