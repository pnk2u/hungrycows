package de.pnku.hungrycows.config;

import de.pnku.hungrycows.config.HungryCowsOwoConfig;
import de.pnku.hungrycows.config.HungryCowsOwoConfig.*;
import de.pnku.hungrycows.item.HungryCowsItemTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.*;
import java.util.stream.Stream;

import static de.pnku.hungrycows.item.HungryCowsItemTags.*;

public class HungryCowsConfigAccessor {
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

    public static ItemLike[] getFeedableItemsFromConfig(EntityType<?> type) {
        List<String> configItems = type == EntityType.COW ? feedSettings.cowFeedableItems()
                : type == EntityType.MOOSHROOM ? feedSettings.mushroomCowFeedableItems()
                : type == EntityType.SHEEP ? feedSettings.sheepFeedableItems()
                : type == EntityType.GOAT ? feedSettings.goatFeedableItems()
                : null;

        if (configItems == null) return new ItemLike[0];

        ItemLike[] itemsFromConfig = configItems.stream()
                .map(feedableItem -> BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(feedableItem)))
                .filter(item -> item != Items.AIR) // Filter out invalid items
                .toArray(ItemLike[]::new);

        TagKey<Item> tagKey = HungryCowsItemTags.getFeedableTag(type);
        ItemLike[] itemsFromTag = BuiltInRegistries.ITEM.stream()
                .filter(item -> item.builtInRegistryHolder().is(tagKey))
                .toArray(ItemLike[]::new);

        return Stream.concat(Arrays.stream(itemsFromConfig), Arrays.stream(itemsFromTag))
                .toArray(ItemLike[]::new);
    }

}
