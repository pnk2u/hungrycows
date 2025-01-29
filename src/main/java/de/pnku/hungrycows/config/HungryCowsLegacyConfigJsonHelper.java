package de.pnku.hungrycows.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static de.pnku.hungrycows.HungryCows.LOGGER;

public class HungryCowsLegacyConfigJsonHelper {
    private static final File folder = new File("config");

    public static void init() {
        migrateLegacyConfigToJson5();
    }


    public static void migrateLegacyConfigToJson5() {
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (folder.isDirectory()) {
            File legacyConfig = new File(folder, "hungrycows.json");
            File newConfig = new File(folder, "hungrycows.json5");
            if (legacyConfig.exists() ) {
                try {
                    boolean s = false;
                    JsonObject legacyConfigContent = new Gson().fromJson(new JsonReader(Files.newBufferedReader(Paths.get(legacyConfig.getPath()))), JsonObject.class);
                    if (legacyConfigContent.has("grassEatProbability")){
                        JsonElement grassEatProbability = legacyConfigContent.remove("grassEatProbability");
                        JsonObject blockEatSettings = new JsonObject();
                        blockEatSettings.add("grassEatProbability", grassEatProbability);
                        legacyConfigContent.add("blockEatSettings", blockEatSettings);
                    }
                    try (Writer writer = Files.newBufferedWriter(Paths.get(newConfig.getPath()))) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        gson.toJson(legacyConfigContent, writer);
                        legacyConfig.delete();
                        s = true;
                    } catch (IOException e) {
                        LOGGER.info(e.getMessage() + " - Config file failed to be migrated to Hungry Cows v2.0.0+. Manually renaming hungrycows.json to hungrycows.json5");
                    }
                    finally {
                        if (s) {
                        LOGGER.info("Config file has successfully been migrated from cloth-config (HC v1.4.2 and older) to owo-lib (HC v2.0.0+).");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
