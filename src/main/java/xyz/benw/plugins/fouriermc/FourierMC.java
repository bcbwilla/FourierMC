package xyz.benw.plugins.fouriermc;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.benw.plugins.fouriermc.DataAnalysis.DescriptiveAnalyzer;
import xyz.benw.plugins.fouriermc.DataAnalysis.QuantitativeAnalyzer;

import java.util.*;

/**
 * Created by ben on 15/01/15.
 */
public class FourierMC extends JavaPlugin implements Listener {

    public HashMap<UUID, ClickData> clickLogger = new HashMap<UUID, ClickData>();

    private long checkInterval; // How often to run an analysis on data
    private long samplePeriod; // How often (in ticks) between counting clicks

    @Override
    public void onEnable(){

        checkInterval = getConfig().getLong("checkinterval");
        samplePeriod = getConfig().getLong("sampleperiod");

        getServer().getPluginManager().registerEvents(new ClickListener(this), this);
        getLogger().info("Periodically awesome. Sample Period:" + samplePeriod);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        // Counts clicks between every samplePeriod
        scheduler.scheduleSyncRepeatingTask(this, new Collector(this), 0L, samplePeriod);

        // Performs analysis every checkInterval
        scheduler.scheduleSyncRepeatingTask(this, new DescriptiveAnalyzer(this), 0L, 500L);

        // Performs analysis every checkInterval
        scheduler.scheduleSyncRepeatingTask(this, new QuantitativeAnalyzer(this), 0L, checkInterval);
    }

    @Override
    public void onDisable(){
        getLogger().info("Goodbye.");
    }

    public long getSamplePeriod() {
        return samplePeriod;
    }

}
