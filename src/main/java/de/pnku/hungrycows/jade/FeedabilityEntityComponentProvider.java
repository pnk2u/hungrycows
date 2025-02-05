package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.HungryCowsEntityInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ElementHelper;

import java.awt.*;

import static de.pnku.hungrycows.HungryCows.LOGGER;
import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.*;

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
        if ((isCow || isMooshroom || isGoat || (isSheep && sheepSettings.isSheepFeedToRegrowWool()))) {
            hasBeenFedManuallyTimer = isCow || isMooshroom ? ((HungryCowsEntityInterface) entity).hungrycows$getCowHasBeenFedManuallyTimer() : isSheep ? ((HungryCowsEntityInterface) entity).hungrycows$getSheepHasBeenFedManuallyTimer() : ((HungryCowsEntityInterface) entity).hungrycows$getGoatHasBeenFedManuallyTimer();
            boolean canBeHungry = (isCow ? entity.getEntityData().get(HungryCows.IS_MILKED) > 0 : (isMooshroom ? entity.getEntityData().get(HungryCows.IS_MILKED_MOOSHROOM) > 0 : isSheep ? ((Byte)entity.getEntityData().get(Sheep.DATA_WOOL_ID) & 16) != 0 : entity.getEntityData().get(HungryCows.IS_MILKED_GOAT) > 0));
            if (hasBeenFedManuallyTimer > 0 && canBeHungry) {
                boolean canBeFed = (hasBeenFedManuallyTimer == 1);
                if (canBeFed) {
                    Component feedableTrueComponent = Component.translatable("hungrycows.feedable.true" + (isCow ? ".cow" : (isMooshroom ? ".mooshroom" : (isSheep ? ".sheep" : ".goat"))) + (!handStack.isEmpty() && checkFeedability(handStack, entity) ? ".item" : ""), Component.translatable(handStack.getDescriptionId()));
                    if (!handStack.isEmpty() && checkFeedability(handStack, entity)) {
                        int horizantalIconShift = Minecraft.getInstance().getLanguageManager().getSelected().equals("de_de") ?
                                isCow ? 164
                              : isMooshroom ? 200
                              : isSheep ? 179
                              : 172 // if Goat
                              : 102; // if !de_de
                        IElement icon = IElementHelper.get().item(new ItemStack(handStack.getItem()), 0.5f).size(new Vec2(10, 10)).translate(new Vec2(horizantalIconShift, -2));
                        tooltip.add(icon);
                        IElement moveToLeftSpace = IElementHelper.get().spacer(-10, 0);
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
