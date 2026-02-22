package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.block.HungryCowsBlockTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;

import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.isVanillaBackportLoaded;

public enum EdibleBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockState block = blockAccessor.getBlock().defaultBlockState();
        boolean edibleForAny = block.is(Blocks.GRASS_BLOCK) || block.is(Blocks.MYCELIUM) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_TEMPERATE_COWS) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_COLD_COWS) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_WARM_COWS) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_MOOSHROOMS) || block.is(BlockTags.SMALL_FLOWERS) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_SHEEP);
        if (edibleForAny) {
            boolean edibleForCow = block.is(Blocks.GRASS_BLOCK) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_TEMPERATE_COWS);
            boolean edibleForColdCow = block.is(Blocks.GRASS_BLOCK) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_COLD_COWS);
            boolean edibleForWarmCow = block.is(Blocks.GRASS_BLOCK) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_WARM_COWS);
            boolean edibleForMooshroom = block.is(Blocks.MYCELIUM) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_MOOSHROOMS);
            boolean edibleForBrownMooshroom = block.is(BlockTags.SMALL_FLOWERS);
            boolean edibleForSheep = block.is(Blocks.GRASS_BLOCK) || block.is(HungryCowsBlockTags.EDIBLE_PLANTS_FOR_SHEEP);

            Element cowIcon = JadeUI.sprite(HungryCows.withModId("cow_icon"), 8, 8).offset(0, -1);
            Element coldCowIcon = JadeUI.sprite(HungryCows.withModId("cold_cow_icon"), 8, 8).offset(0, -1);
            Element warmCowIcon = JadeUI.sprite(HungryCows.withModId("warm_cow_icon"), 8, 8).offset(0, -1);
            Element mooshroomIcon = JadeUI.sprite(HungryCows.withModId("red_mooshroom_icon"), 8, 8).offset(0, -1);
            Element brownMooshroomIcon = JadeUI.sprite(HungryCows.withModId("brown_mooshroom_icon"), 8, 8).offset(0, -1);
            Element sheepIcon = JadeUI.sprite(HungryCows.withModId("sheep_icon"), 8, 8).offset(0, -1);
            Element spacer = JadeUI.spacer(2, 0);

            iTooltip.add(Component.translatable("hungrycows.edible_block.prefix"));
            if (edibleForCow) {iTooltip.append(cowIcon); iTooltip.append(spacer);}
            if (isVanillaBackportLoaded) {
                if (edibleForColdCow) {iTooltip.append(coldCowIcon); iTooltip.append(spacer);}
                if (edibleForWarmCow) {iTooltip.append(warmCowIcon); iTooltip.append(spacer);}
            }
            if (edibleForMooshroom) {iTooltip.append(mooshroomIcon); iTooltip.append(spacer);}
            if (edibleForBrownMooshroom||edibleForMooshroom) {iTooltip.append(brownMooshroomIcon); iTooltip.append(spacer);}
            if (edibleForSheep) {iTooltip.append(sheepIcon);}
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {

    }

    @Override
    public ResourceLocation getUid(){
        return ResourceLocation.fromNamespaceAndPath("hungrycows","edible_block");
    }


}
