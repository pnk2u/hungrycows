package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;

public enum SuspiciousEntityComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            EntityAccessor accessor,
            IPluginConfig config) {
        Entity entity = accessor.getEntity();
        if (entity instanceof MushroomCow mooshroom && mooshroom.getVariant() == MushroomCow.Variant.BROWN) {
            if (!(mooshroom.isBaby())) {
                ItemStack suspiciousFlowerStack = ((IHungryCows) mooshroom).hungrycows$getSuspiciousFlowerStack();
                if (suspiciousFlowerStack != null && !suspiciousFlowerStack.isEmpty()) {
                    Element flowerIcon = JadeUI.item(suspiciousFlowerStack, 0.5f).size(11, 8).offset(1, -3);
                    tooltip.add(Component.translatable("hungrycows.suspiciously_milkable.flower"));
                    tooltip.append(flowerIcon);
                    tooltip.append(Component.translatable(suspiciousFlowerStack.getItem().getDescriptionId()));
                }
            }
        }
    }
    @Override
    public void appendServerData(CompoundTag nbtCompound, EntityAccessor entityAccessor) {
    }

    @Override
    public Identifier getUid(){
        return Identifier.fromNamespaceAndPath("hungrycows","suspiciously_milkable");
    }

}
