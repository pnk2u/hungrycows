package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.util.HungryCowsCompatibilityHelper;
import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.*;

public enum FeedabilityEntityComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            EntityAccessor accessor,
            IPluginConfig config) {
        Entity entity = accessor.getEntity();
        Player player = accessor.getPlayer();
        ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        int hasBeenFedManuallyTimer;
        boolean isCow = entity.getType().equals(EntityType.COW);
        boolean isMooshroom = entity.getType().equals(EntityType.MOOSHROOM);
        boolean isSheep = entity.getType().equals(EntityType.SHEEP);
        boolean isGoat = entity.getType().equals(EntityType.GOAT);
        boolean isFeedableType = HungryCowsCompatibilityHelper.FEEDABLE_ENTITIES.contains(entity.getType());
        if (isSheep ? sheepSettings.isSheepFeedToRegrowWool() : isFeedableType) {
            hasBeenFedManuallyTimer = isCow || isMooshroom ? ((IHungryCows) entity).hungrycows$getCowHasBeenFedManuallyTimer() : isSheep ? ((IHungryCows) entity).hungrycows$getSheepHasBeenFedManuallyTimer() : isGoat ? ((IHungryCows) entity).hungrycows$getGoatHasBeenFedManuallyTimer() : ((IHungryCows) entity).hungrycows$getCowHasBeenFedManuallyTimer();
            boolean canBeHungry = isSheep ? ((Byte)entity.getEntityData().get(Sheep.DATA_WOOL_ID) & 16) != 0 : ((IHungryCows)entity).hungrycows$isMilked();
            if (hasBeenFedManuallyTimer > 0 && canBeHungry) {
                boolean canBeFed = (hasBeenFedManuallyTimer == 1);
                if (canBeFed) {
                    Component feedableTrueComponent = Component.translatable("hungrycows.feedable.true." + ((IHungryCows) entity).hungrycows$getName() + (!handStack.isEmpty() && checkFeedability(handStack, entity) ? ".item" : ""), Component.translatable(handStack.getItem().getDescriptionId()));
                    if (!handStack.isEmpty() && checkFeedability(handStack, entity)) {
                        int horizantalIconShift =
                        Minecraft.getInstance().getLanguageManager().getSelected().startsWith("en") ? 102 :
                        Minecraft.getInstance().getLanguageManager().getSelected().equals("pt_br") ? 150 :
                        Minecraft.getInstance().getLanguageManager().getSelected().equals("de_de") ?
                                isCow ? 164
                              : isMooshroom ? 200
                              : isSheep ? 179
                              : isGoat ? 172
                              : 191 // if Moobloom
                              : 102;
                        Element icon = JadeUI.item(new ItemStack(handStack.getItem()), 0.5f).size(10, 10).offset(horizantalIconShift, -2);
                        tooltip.add(icon);
                        Element moveToLeftSpace = JadeUI.spacer(-10, 0);
                        tooltip.append(moveToLeftSpace);
                        tooltip.append(feedableTrueComponent);
                    } else {
                        tooltip.add(feedableTrueComponent);
                    }
                } else {
                    tooltip.add(Component.translatable("hungrycows.feedable.false", IThemeHelper.get().seconds(hasBeenFedManuallyTimer, accessor.tickRate())));
                }
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
