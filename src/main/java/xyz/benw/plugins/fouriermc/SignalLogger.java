package xyz.benw.plugins.fouriermc;

import java.io.*;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author bcbwilla
 */
public class SignalLogger {

    public static void log(FourierMC plugin, double[] signal, UUID playerId) {

        final String FILENAME = "FourierMC.csv";

        try
        {
            File dataFolder = plugin.getDataFolder();
            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }

            File file = new File(dataFolder, FILENAME);

            if (!file.exists())
            {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, true);
                writer.append("timestamp,playerId,signal\n");
            } else {
                FileWriter writer = new FileWriter(file, true);
                writer.append(Long.toString(System.currentTimeMillis()) + ",");
                writer.append(playerId.toString() + ",");
                writer.append(Arrays.toString(signal) + "\n");
                writer.flush();
                writer.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
