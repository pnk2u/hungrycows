package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
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
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;

import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.MILKABLE_ENTITIES;

public enum MilkabilityEntityComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            EntityAccessor accessor,
            IPluginConfig config) {
        Entity entity = accessor.getEntity();
        EntityType<?> type = entity.getType();
        if (MILKABLE_ENTITIES.contains(type)) {
            if (!((Animal) entity).isBaby()) {
                boolean isMilkable = ((IHungryCows) entity).hungrycows$isMilkable();
                Element emptyBucketIcon = JadeUI.item(new ItemStack(Items.BUCKET), 0.5f).size(8, 8).offset(-6, -2);
                Element filledBucketIcon = JadeUI.item(new ItemStack(Items.MILK_BUCKET), 0.5f).size(new Vec2(9, 8)).translate(new Vec2(-1, -2));
                Element heartBucketIcon = JadeUI.sprite(HungryCows.withModId("heart_milk"), 8, 8).offset(-1, -1);
                Element mushroomStewIcon = JadeUI.item(new ItemStack(Items.MUSHROOM_STEW), 0.5f).size(new Vec2(9, 8)).translate(new Vec2(-1, -2));
                Element suspiciousStewIcon = JadeUI.item(new ItemStack(Items.SUSPICIOUS_STEW), 0.5f).size(new Vec2(9, 8)).translate(new Vec2(-1, -2));
                Element grassIcon = JadeUI.item(new ItemStack(Items.SHORT_GRASS), 0.5f).size(4, 8).offset(1, -2);
                tooltip.add(isMilkable ? heartBucketIcon : grassIcon);
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
        return ResourceLocation.fromNamespaceAndPath("hungrycows","milkable");
    }

}
