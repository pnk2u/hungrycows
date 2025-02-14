package de.pnku.hungrycows.util;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class HungryCowsCompatibilityHelper {
    public static EntityDataAccessor<Boolean> IS_MILKED_MOOBLOOM;
    public static List<EntityType<?>> MILKABLE_ENTITIES = new ArrayList<>();
    public static List<EntityType<?>> CUSTOM_MILKABLE_ENTITIES = new ArrayList<>();
    public static List<EntityType<?>> FEEDABLE_ENTITIES = new ArrayList<>();
    public static List<EntityType<?>> CUSTOM_FEEDABLE_ENTITIES = new ArrayList<>();
    public static boolean isBnBLoaded = false;

    public static void init() {
        setMilkableEntities();
        setFeedableEntities();
    }

    protected static void setMilkableEntities() {
        MILKABLE_ENTITIES.add(EntityType.COW);
        MILKABLE_ENTITIES.add(EntityType.MOOSHROOM);
        MILKABLE_ENTITIES.add(EntityType.GOAT);
        MILKABLE_ENTITIES.addAll(CUSTOM_MILKABLE_ENTITIES);
    }

    protected static void setFeedableEntities() {
        FEEDABLE_ENTITIES.add(EntityType.COW);
        FEEDABLE_ENTITIES.add(EntityType.MOOSHROOM);
        FEEDABLE_ENTITIES.add(EntityType.SHEEP);
        FEEDABLE_ENTITIES.add(EntityType.GOAT);
        FEEDABLE_ENTITIES.addAll(CUSTOM_FEEDABLE_ENTITIES);
    }

}
