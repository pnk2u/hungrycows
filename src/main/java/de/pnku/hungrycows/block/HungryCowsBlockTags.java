package de.pnku.hungrycows.block;

import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class HungryCowsBlockTags {
    public final static TagKey<Block> EDIBLE_PLANTS_FOR_COWS = TagKey.create(Registries.BLOCK, HungryCows.withModId("edible_for_cows"));
        public final static TagKey<Block> EDIBLE_PLANTS_FOR_TEMPERATE_COWS = TagKey.create(Registries.BLOCK, HungryCows.withModId("edible_for_temperate_cows"));
        public final static TagKey<Block> EDIBLE_PLANTS_FOR_COLD_COWS = TagKey.create(Registries.BLOCK, HungryCows.withModId("edible_for_cold_cows"));
        public final static TagKey<Block> EDIBLE_PLANTS_FOR_WARM_COWS = TagKey.create(Registries.BLOCK, HungryCows.withModId("edible_for_warm_cows"));
    public final static TagKey<Block> EDIBLE_PLANTS_FOR_MOOSHROOMS = TagKey.create(Registries.BLOCK, HungryCows.withModId("edible_for_mooshrooms"));
    public final static TagKey<Block> EDIBLE_PLANTS_FOR_SHEEP = TagKey.create(Registries.BLOCK, HungryCows.withModId("edible_for_sheep"));
}
