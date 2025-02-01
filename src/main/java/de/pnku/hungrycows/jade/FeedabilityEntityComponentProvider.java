package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

public enum FeedabilityEntityComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            EntityAccessor accessor,
            IPluginConfig config) {
        Entity entity = accessor.getEntity();
        int hasBeenFedManuallyTimer;
        boolean isCow = entity.getType().equals(EntityType.COW);
        boolean isMooshroom = entity.getType().equals(EntityType.MOOSHROOM);
        boolean isSheep = entity.getType().equals(EntityType.SHEEP);
        if ((isCow || isMooshroom || (isSheep && HungryCows.sheepSettings.isSheepFeedToRegrowWool())) && HungryCows.milkabilitySettings.averageFoodForMilkabilityRegainAmount() > 0) {
            boolean isCowMilked = entity.getEntityData().get(HungryCows.IS_MILKED) > 0;
            boolean isMooshroomMilked = entity.getEntityData().get(HungryCows.IS_MILKED_MOOSHROOM) > 0;
            boolean isSheepSheared = ((Byte)entity.getEntityData().get(Sheep.DATA_WOOL_ID) & 16) != 0;
            hasBeenFedManuallyTimer = !entity.getType().equals(EntityType.SHEEP) ? ((ICowEntity) entity).hungrycows$getCowHasBeenFedManuallyTimer() : ((ICowEntity) entity).hungrycows$getSheepHasBeenFedManuallyTimer();
            boolean canBeHungry = (isCow ? isCowMilked : (isMooshroom ? isMooshroomMilked : isSheepSheared));
            if (hasBeenFedManuallyTimer > 0 && canBeHungry) {
                boolean canBeFed = (hasBeenFedManuallyTimer == 1);
                tooltip.add(Component.translatable("hungrycows.feedable." + canBeFed + (canBeFed ? (isCow ? ".cow" : (isMooshroom ? ".mooshroom" : ".sheep")) : ""), IThemeHelper.get().seconds(hasBeenFedManuallyTimer, accessor.tickRate())));
            }
        }
    }
    @Override
    public void appendServerData(CompoundTag nbtCompound, EntityAccessor entityAccessor) {
    }

    @Override
    public ResourceLocation getUid(){
        return ResourceLocation.fromNamespaceAndPath("hungrycows","feedable");
    }
}
