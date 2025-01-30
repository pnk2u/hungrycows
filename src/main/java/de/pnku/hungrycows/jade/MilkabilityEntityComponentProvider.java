package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

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
        boolean m = entity.getType().equals(EntityType.COW) && !entity.getType().equals(EntityType.MOOSHROOM) && ((ICowEntity) entity).hungrycows$isMilkable();
        boolean mm = !entity.getType().equals(EntityType.COW) && entity.getType().equals(EntityType.MOOSHROOM) && ((ICowEntity) entity).hungrycows$isMilkable();
        int milkability = m || mm ? 0 : 1;
        tooltip.add(Component.translatable("hungrycows.milkable." + milkability));
    }
    @Override
    public void appendServerData(CompoundTag nbtCompound, EntityAccessor entityAccessor) {
    }

    @Override
    public ResourceLocation getUid(){
        return ResourceLocation.fromNamespaceAndPath("hungrycows","milkable");
    }

}
