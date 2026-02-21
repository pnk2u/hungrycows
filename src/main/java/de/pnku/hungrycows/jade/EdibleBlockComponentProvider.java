package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.block.HungryCowsBlockTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.isVanillaBackportLoaded;
import static de.pnku.hungrycows.jade.HungryCowsJadeUIItems.*;

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
            
            IElementHelper elements = iTooltip.getElementHelper();
            IElement cowIcon = elements.item(new ItemStack(COW_ICON_UI_ITEM), 0.5f).translate(new net.minecraft.world.phys.Vec2(0, -1));
            IElement coldCowIcon = elements.item(new ItemStack(COLD_COW_ICON_UI_ITEM), 0.5f).translate(new net.minecraft.world.phys.Vec2(0, -1));
            IElement warmCowIcon = elements.item(new ItemStack(WARM_COW_ICON_UI_ITEM), 0.5f).translate(new net.minecraft.world.phys.Vec2(0, -1));
            IElement mooshroomIcon = elements.item(new ItemStack(MOOSHROOM_ICON_UI_ITEM), 0.5f).translate(new net.minecraft.world.phys.Vec2(0, -1));
            IElement brownMooshroomIcon = elements.item(new ItemStack(BROWN_MOOSHROOM_ICON_UI_ITEM), 0.5f).translate(new net.minecraft.world.phys.Vec2(0, -1));
            IElement sheepIcon = elements.item(new ItemStack(SHEEP_ICON_UI_ITEM), 0.5f).translate(new net.minecraft.world.phys.Vec2(0, -1));
            IElement spacer = elements.spacer(2, 0);

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
        return ResourceLocation.tryBuild("hungrycows","edible_block");
    }


}
