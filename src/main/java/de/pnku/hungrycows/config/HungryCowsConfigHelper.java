package de.pnku.hungrycows.config;

import de.pnku.hungrycows.config.HungryCowsOwoConfig;
import de.pnku.hungrycows.config.HungryCowsOwoConfig.*;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO.*;
import static de.pnku.hungrycows.item.HungryCowsItemTags.*;
import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.FEEDABLE_ENTITIES;

public class HungryCowsConfigHelper {
    public static final HungryCowsOwoConfig CONFIG = HungryCowsOwoConfig.createAndLoad();
    public static final MilkabilitySettings_ milkabilitySettings = CONFIG.milkabilitySettings;
    public static final MilkabilitySettings_.FeedSettings_ feedSettings = milkabilitySettings.feedSettings;
    public static final BlockEatSettings_ blockEatSettings = CONFIG.blockEatSettings;
    public static final SheepSettings_ sheepSettings = CONFIG.sheepSettings;
    public static final CowMilkSettings_ cowMilkSettings = CONFIG.cowMilkSettings;
    public static final MushroomCowMilkSettings_ mushroomCowMilkSettings = CONFIG.mushroomCowMilkSettings;
    public static final GoatMilkSettings_ goatMilkSettings = CONFIG.goatMilkSettings;

    public static boolean checkFeedability(ItemStack feedStack, Entity fedEntity) {
        EntityType<?> type = fedEntity.getType();
        if (fedEntity instanceof MushroomCow mooshroom && mooshroom.getVariant() == MushroomCow.Variant.BROWN) {
            if (feedStack.is(ItemTags.SMALL_FLOWERS)) return true;
        }
        return checkFeedability(feedStack, type);
    }

    public static boolean checkFeedability(ItemStack feedStack, EntityType<?> type) {
        List<String> feedableItemList;
        TagKey<Item> feedableItemTag;

        if (!feedStack.isEmpty()){
            String feedItemId = BuiltInRegistries.ITEM.getKey(feedStack.getItem()).toString();
            if (type.equals(EntityType.COW)) {
                feedableItemList = feedSettings.cowFeedableItems();
                feedableItemTag = COW_FEEDABLE;
            } else if (type.equals(EntityType.MOOSHROOM)) {
                feedableItemList = feedSettings.mushroomCowFeedableItems();
                feedableItemTag = MOOSHROOM_FEEDABLE;
            } else if (type.equals(EntityType.GOAT)) {
                feedableItemList = feedSettings.goatFeedableItems();
                feedableItemTag = GOAT_FEEDABLE;
            } else if (type.equals(EntityType.SHEEP)) {
                feedableItemList = feedSettings.sheepFeedableItems();
                feedableItemTag = SHEEP_FEEDABLE;
            } else if (FEEDABLE_ENTITIES.contains(type)) {
                feedableItemList = feedSettings.cowFeedableItems();
                feedableItemTag = COW_FEEDABLE;
            } else {
                feedableItemList = null;
                feedableItemTag = null;
            }
            if (feedableItemList != null && !feedableItemList.isEmpty()) {
                if (feedStack.is(feedableItemTag)) {
                    return true;
                }
                for (String feedableItem : feedableItemList) {
                    if (feedItemId.equals(feedableItem)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static CubeListBuilder getMilkableCowBodyWithUdderCubeListBuilder(String name, CubeListBuilder cubeListBuilder) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder, "default");
    }

    public static CubeListBuilder getMilkableCowBodyWithUdderCubeListBuilder(String name, CubeListBuilder cubeListBuilder, String type) {
        if (name.equals("body") && showMilkableModel()) {
            if (type.equals("default")) {
                return CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                        .texOffs(52, 0).addBox("udder1", -2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F)
                        // Any following boxes will only get "rendered" when isMilkable() is true as defined in LivingEntityRendererMixin.wrappedGetTextureLocation by nature of the used pixels being transparent in the original texture
                        .texOffs(52, 7).addBox("udder1.5", -2.0F, 2.0F, -8.55F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0, 0, -0.45F))
                        .texOffs(60, 10).addBox("udder_cover", -0.5F, 2.0F, -8.5525F, 1.0F, 1.0F, 1.0F, new CubeDeformation(1.499F, 0, -0.4515F))
                        .texOffs(52, 8).addBox("udder2", -2.0F, 3.0F, -8.625F - 0.1F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F, 0, -0.375F))
                        .texOffs(60, 12).addBox("right_front_teat", -2.025F, 3.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                        .texOffs(60, 12).addBox("left_front_teat", 1.075F, 3.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                        .texOffs(60, 12).addBox("right_hind_teat", -2.025F, 6.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                        .texOffs(60, 12).addBox("left_hind_teat", 1.075F, 6.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F));
            } else if (type.equals("sculk")) { // Sombercup (bovinesandbuttercups)
                return CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                        .texOffs(52, 0).addBox("udder1", -2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F)
                        // Any following boxes will only get "rendered" when isMilkable() is true as defined in LivingEntityRendererMixin.wrappedGetTextureLocation by nature of the used pixels being transparent in the original texture
                        .texOffs(55, 12).addBox("right_front_teat", -2.025F, 3.0F, -8.45F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                        .texOffs(55, 12).addBox("left_front_teat", 1.075F, 3.0F, -8.45F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                        .texOffs(55, 12).addBox("right_hind_teat", -2.025F, 6.0F, -8.45F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                        .texOffs(55, 12).addBox("left_hind_teat", 1.075F, 6.0F, -8.45F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F));
            } else {
                return cubeListBuilder;
            }
        } else {
            return cubeListBuilder;
        }
    }

    public static boolean showMilkableTexture() {
        return !milkabilitySettings.milkableCowDisplayType().equals(HIDE_TEXTURE_AND_MODEL);
    }

    public static boolean showMilkableModel() {
        return milkabilitySettings.milkableCowDisplayType().equals(HIDE_NONE);
    }

    public static Vec3 relParticlePos(Vec3 cowPos, float yRot, String type) {
        double xOff;
        double zOff;
        double yOff;
        switch (type) {
            case "cow_udder" -> {
                xOff = 0.615; zOff = 0.615;
                yOff = 0.65;
            }
            case "cow_body" -> {
                xOff = 0.2; zOff = 0.2;
                yOff = 0.8;
            }
            case "udder_heart" -> {
                xOff = 0.7; zOff = 0.7;
                yOff = 0.6;
            }
            case "goat_udder" -> {
                xOff = 0.415; zOff = 0.415;
                yOff = 0.2;
            }
            case "goat_body" -> {
                xOff = 0.15; zOff = 0.15;
                yOff = 0.8;
            }
            case "sheep_body" -> {
                xOff = 0.15; zOff = 0.15;
                yOff = 0.75;
            }
            case null, default -> {
                xOff = 0; zOff = 0;
                yOff = 0;
            }
        }
        double relX = cowPos.x() + Math.sin(Math.toRadians(yRot)) * xOff;
        double relZ = cowPos.z() - Math.cos(Math.toRadians(yRot)) * zOff;
        double relY = cowPos.y() + yOff;
        return new Vec3(relX, relY, relZ);
    }
}
