package de.pnku.hungrycows.entity;

import de.pnku.hungrycows.HungryCows;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class HungryCowsEntityTypeTags {
    public static final TagKey<EntityType<?>> HUNGRY_COWS = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, HungryCows.withModId("hungry_cows"));
    public static final TagKey<EntityType<?>> HUNGRY_MOOSHROOMS = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, HungryCows.withModId("hungry_mooshrooms"));
    public static final TagKey<EntityType<?>> HUNGRY_SHEEP = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, HungryCows.withModId("hungry_sheep"));
    // Cows + Sheep eat Grass Blocks
    public static final TagKey<EntityType<?>> HUNGRY_GRAZERS = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, HungryCows.withModId("hungry_grazers"));
    // Mooshrooms eat Mycelium
    public static final TagKey<EntityType<?>> HUNGRY_MYCOPHAGES = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, HungryCows.withModId("hungry_mycophages"));
}
