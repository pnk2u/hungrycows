package de.pnku.hungrycows.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static de.pnku.hungrycows.HungryCows.*;

public class HungryCowsCompatibilityHelper {
    public static EntityDataAccessor<Boolean> IS_MILKED_MOOBLOOM;
    public static List<EntityType<?>> MILKABLE_ENTITIES = new ArrayList<>();
    public static List<EntityType<?>> FEEDABLE_ENTITIES = new ArrayList<>();
    public static boolean isBnBLoaded = false;

    public static void init() {
        setMilkableEntities();
        setFeedableEntities();

    }

    public static void clientInit() {
        ResourcePackActivationType activationType;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (isResourcePackEnabled("FreshAnimations")){
                LOGGER.info("Detected \"FreshAnimations\" as a selected resource pack. Built-in compatibility resource pack has been auto-applied.");
                activationType = ResourcePackActivationType.DEFAULT_ENABLED;
            } else {activationType = ResourcePackActivationType.NORMAL;}
            ResourceManagerHelper.registerBuiltinResourcePack(
                    withModId("hungryandfreshcows"),
                    FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                    Component.translatable("resourcepack.hungrycows.hungryandfreshcows.title"),
                    activationType
            );
        }
        if (isBnBLoaded) {initBnB();}
    }

    protected static void setMilkableEntities() {
        MILKABLE_ENTITIES.add(EntityType.COW);
        MILKABLE_ENTITIES.add(EntityType.MOOSHROOM);
        MILKABLE_ENTITIES.add(EntityType.GOAT);
    }

    protected static void setFeedableEntities() {
        FEEDABLE_ENTITIES.add(EntityType.COW);
        FEEDABLE_ENTITIES.add(EntityType.MOOSHROOM);
        FEEDABLE_ENTITIES.add(EntityType.SHEEP);
        FEEDABLE_ENTITIES.add(EntityType.GOAT);
    }

    protected static void initBnB() {
//      BovinesEntityTypes.registerAll();
//      MILKABLE_ENTITIES.add(BovinesEntityTypes.MOOBLOOM);
//      FEEDABLE_ENTITIES.add(BovinesEntityTypes.MOOBLOOM);
    }

    public static boolean isResourcePackEnabled(String packName) {
        File optionsFile = new File(Minecraft.getInstance().gameDirectory, "options.txt");
        if (optionsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(optionsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("resourcePacks:")) {
                        return line.contains(packName);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
