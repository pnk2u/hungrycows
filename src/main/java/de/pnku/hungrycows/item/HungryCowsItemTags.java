package de.pnku.hungrycows.item;

import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import static de.pnku.hungrycows.HungryCows.withModId;

public class HungryCowsItemTags {
    public static final TagKey<Item> COW_FEEDABLE = TagKey.create(Registries.ITEM, withModId("cow_feedable"));
    public static final TagKey<Item> MOOSHROOM_FEEDABLE = TagKey.create(Registries.ITEM, withModId("mooshroom_feedable"));
    public static final TagKey<Item> GOAT_FEEDABLE = TagKey.create(Registries.ITEM, withModId("goat_feedable"));
    public static final TagKey<Item> SHEEP_FEEDABLE = TagKey.create(Registries.ITEM, withModId("sheep_feedable"));

    public static TagKey<Item> getFeedableTag(EntityType<?> type) {
        return type == EntityType.COW ? HungryCowsItemTags.COW_FEEDABLE
                : type == EntityType.MOOSHROOM ? HungryCowsItemTags.MOOSHROOM_FEEDABLE
                : type == EntityType.SHEEP ? HungryCowsItemTags.SHEEP_FEEDABLE
                : type == EntityType.GOAT ? HungryCowsItemTags.GOAT_FEEDABLE
                : null;
    }
}
