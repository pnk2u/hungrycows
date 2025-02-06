package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.HungryCowsEntityInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import static de.pnku.hungrycows.HungryCows.IS_MILKED;
import static de.pnku.hungrycows.HungryCows.IS_MILKED_MOOSHROOM;

public enum MilkabilityEntityComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            EntityAccessor accessor,
            IPluginConfig config) {
        Entity entity = accessor.getEntity();
        EntityType<?> type = entity.getType();
        if (type.equals(EntityType.COW) || type.equals(EntityType.MOOSHROOM) || type.equals(EntityType.GOAT)) {
            if (!((Animal) entity).isBaby()) {
                boolean isMilkable = ((HungryCowsEntityInterface) entity).hungrycows$isMilkable();
                IElement emptyBucketIcon = IElementHelper.get().item(new ItemStack(Items.BUCKET), 0.5f).size(new Vec2(8, 8)).translate(new Vec2(-6, -2));
                IElement heartBucketIcon = IElementHelper.get().sprite(HungryCows.withModId("heart_milk"), 8, 8).translate(new Vec2(-1, -1));
                IElement grassIcon = IElementHelper.get().item(new ItemStack(Items.SHORT_GRASS), 0.5f).size(new Vec2(4, 8)).translate(new Vec2(1, -2));
                tooltip.add(isMilkable ? heartBucketIcon : grassIcon);
                if (!isMilkable) {tooltip.append(emptyBucketIcon);}
                tooltip.append(Component.translatable("hungrycows.milkable." + isMilkable));
            }
        }
    }
    @Override
    public void appendServerData(CompoundTag nbtCompound, EntityAccessor entityAccessor) {
    }

    @Override
    public ResourceLocation getUid(){
        return ResourceLocation.fromNamespaceAndPath("hungrycows","milkable");
    }

}
