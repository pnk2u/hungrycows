package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.util.IHungryCows;
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
                boolean isMilkable = ((IHungryCows) entity).hungrycows$isMilkable();
                IElementHelper elements = tooltip.getElementHelper();
                IElement emptyBucketIcon = elements.item(new ItemStack(Items.BUCKET), 0.5f).size(new Vec2(8, 8)).translate(new Vec2(-6, -1));
                IElement filledBucketIcon = elements.item(new ItemStack(Items.MILK_BUCKET), 0.5f).size(new Vec2(9, 8)).translate(new Vec2(-1, -2));
                IElement heartBucketIcon = elements.item(new ItemStack(HungryCowsJadeUIItems.HEART_MILK_UI_ITEM), 0.5f).size(new Vec2(8, 8)).translate(new Vec2(-1, -1));
                        // elements.sprite(HungryCows.withModId("heart_milk"), 8, 8).translate(new Vec2(-1, -1));
                IElement mushroomStewIcon = elements.item(new ItemStack(Items.MUSHROOM_STEW), 0.5f).size(new Vec2(9, 8)).translate(new Vec2(-1, -2));
                IElement suspiciousStewIcon = elements.item(new ItemStack(Items.SUSPICIOUS_STEW), 0.5f).size(new Vec2(9, 8)).translate(new Vec2(-1, -2));
                IElement grassIcon = elements.item(new ItemStack(Items.GRASS), 0.5f).size(new Vec2(4, 8)).translate(new Vec2(1, -2));
                tooltip.add(Component.translatable("hungrycows.milkable.prefix"));
                tooltip.append(isMilkable ? heartBucketIcon : grassIcon);
                if (!isMilkable) {tooltip.append(emptyBucketIcon);}
                tooltip.append(Component.translatable("hungrycows.milkable." + isMilkable));
                if (isMilkable) {
                    tooltip.append(Component.literal(" ("));
                    tooltip.append(filledBucketIcon);
                    if (!type.equals(EntityType.COW) && !type.equals(EntityType.GOAT)) {
                        if (type.equals(EntityType.MOOSHROOM)) {
                            boolean hasSuspicious = !((IHungryCows) entity).hungrycows$getSuspiciousFlowerStack().isEmpty();
                            tooltip.append(hasSuspicious ? suspiciousStewIcon : mushroomStewIcon);
                        } else {
                            tooltip.append(mushroomStewIcon);
                        }
                    }
                    tooltip.append(Component.literal(")"));
                }
            }
        }
    }
    @Override
    public void appendServerData(CompoundTag nbtCompound, EntityAccessor entityAccessor) {
    }

    @Override
    public ResourceLocation getUid(){
        return new ResourceLocation("hungrycows","milkable");
    }
}
