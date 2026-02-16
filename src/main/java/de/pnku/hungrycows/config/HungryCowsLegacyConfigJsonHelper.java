package de.pnku.hungrycows.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import de.pnku.hungrycows.HungryCows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
//                    if (legacyConfigContent.has("milkNutritionValue")){
//                        JsonElement milkNutritionValue = legacyConfigContent.remove("milkNutritionValue");
//                        JsonObject cowMilkSettings = new JsonObject();
//                        cowMilkSettings.add("milkNutritionValue", milkNutritionValue);
//                        legacyConfigContent.add("cowMilkSettings", cowMilkSettings);
//                    }
//                    if (legacyConfigContent.has("milkSaturationModifier")){
//                        JsonElement milkSaturationModifier = legacyConfigContent.remove("milkSaturationModifier");
//                        JsonObject cowMilkSettings = new JsonObject();
//                        cowMilkSettings.add("milkSaturationModifier", milkSaturationModifier);
//                        legacyConfigContent.add("cowMilkSettings", cowMilkSettings);
//                    }
                    try (Writer writer = Files.newBufferedWriter(Paths.get(newConfig.getPath()))) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        gson.toJson(legacyConfigContent, writer);
                        legacyConfig.delete();
                        s = true;
                    } catch (IOException e) {
                        HungryCows.getLogger().info(e.getMessage() + " - Config file failed to be migrated to Hungry Cows v2.0.0+.");
                    }
                    finally {
                        if (s) {
                        HungryCows.getLogger().info("Config file has successfully been migrated from cloth-config/json (HC v1.4.3 and older) to owo-lib/json5 (HC v2.0.0+).");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
