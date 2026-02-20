package de.pnku.hungrycows.util;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.block.HungryCowsBlockTags;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.block.Block;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.pnku.hungrycows.HungryCows.*;

public class HungryCowsCompatibilityHelper {
    public static EntityDataAccessor<Boolean> IS_MILKED_MOOBLOOM;
    public static List<EntityType<?>> HUNGRY_ENTITIES = new ArrayList<>(); // Can eat blocks but wouldn't in vanilla
    public static List<EntityType<?>> MILKABLE_ENTITIES = new ArrayList<>();
    public static List<EntityType<?>> FEEDABLE_ENTITIES = new ArrayList<>();
    public static boolean isBnBLoaded = false;
    public static boolean isVanillaBackportLoaded = false;

    public static void init() {
        setHungryEntities();
        setMilkableEntities();
        setFeedableEntities();
    }

    public static void clientInit() {
        ResourcePackActivationType activationType;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (FabricLoader.getInstance().isModLoaded("entity_model_features")) {
                HungryCows.getLogger().info("Detected \"Entity Model Features\". Registering compatibility features.");
                String variableName = "is_eating";
                String description = "Set to true when the Cow/Mooshroom is currently eating a grass/mycelium block, so that Hungry Cows uses Fresh Animations' eating animation for Cows and Mooshrooms.";
                traben.entity_model_features.EMFAnimationApi.registerSingletonAnimationVariable(HungryCows.MOD_ID, variableName, description, () -> {
                        Optional<traben.entity_model_features.utils.EMFEntity> entity = Optional.ofNullable(traben.entity_model_features.EMFAnimationApi.getCurrentEntity());
                        if (entity.isPresent()) {
                            if (HUNGRY_ENTITIES.contains(entity.get().etf$getType())) {
                                    return ((IHungryCows) entity.get()).hungrycows$isEating();
                            }
                        }
                        return false;
                    }
                );
                if (isResourcePackEnabled("FreshAnimations")) {
                    HungryCows.getLogger().info("Detected \"FreshAnimations\" as a selected resource pack. Built-in compatibility resource pack has been auto-applied.");
                    activationType = ResourcePackActivationType.DEFAULT_ENABLED;
                } else {
                    activationType = ResourcePackActivationType.NORMAL;
                }
                ResourceManagerHelper.registerBuiltinResourcePack(
                        withModId("hungryandfreshcows"),
                        FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                        Component.translatable("resourcepack.hungrycows.hungryandfreshcows.title"),
                        activationType
                );
            }
        }
        if (FabricLoader.getInstance().isModLoaded("bovinesandbuttercups")) {
            isBnBLoaded = true;
        }
        if (isBnBLoaded) {initBnB();}
        if (FabricLoader.getInstance().isModLoaded("vanillabackport")) {
            isVanillaBackportLoaded = true;
        }
    }

    protected static void setHungryEntities() {
        HUNGRY_ENTITIES.add(EntityType.COW);
        HUNGRY_ENTITIES.add(EntityType.MOOSHROOM);
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
//        HUNGRY_ENTITIES.add(BovinesEntityTypes.MOOBLOOM);
//      MILKABLE_ENTITIES.add(BovinesEntityTypes.MOOBLOOM);
//      FEEDABLE_ENTITIES.add(BovinesEntityTypes.MOOBLOOM);
    }

    public static TagKey<Block> getEdiblePlantBlockTagForCowVariant(Entity entity) {
        try {
            Field variantIdDataKeyField = Cow.class.getDeclaredField("DATA_VARIANT_ID");
            variantIdDataKeyField.setAccessible(true);
            Object variantIdDataKey = variantIdDataKeyField.get(null);
            if (variantIdDataKey instanceof EntityDataAccessor<?> entityDataAccessor) {
                String variantId = (String) entity.getEntityData().get(entityDataAccessor);
                if (variantId.equals("minecraft:cold")) {
                    return HungryCowsBlockTags.EDIBLE_PLANTS_FOR_COLD_COWS;
                } else if (variantId.equals("minecraft:warm")) {
                    return HungryCowsBlockTags.EDIBLE_PLANTS_FOR_WARM_COWS;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
                HungryCows.getLogger().error("Failed to get cow variant data key. Defaulting to temperate cow edible plant tag.", e);
        }
        return HungryCowsBlockTags.EDIBLE_PLANTS_FOR_TEMPERATE_COWS;
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
