package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum SuspiciousEntityComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            EntityAccessor accessor,
            IPluginConfig config) {
        Entity entity = accessor.getEntity();
        if (entity instanceof MushroomCow mooshroom && mooshroom.getVariant() == MushroomCow.MushroomType.BROWN) {
            if (!(mooshroom.isBaby())) {
                ItemStack suspiciousFlowerStack = ((IHungryCows) mooshroom).hungrycows$getSuspiciousFlowerStack();
                if (suspiciousFlowerStack != null && !suspiciousFlowerStack.isEmpty()) {
                    IElement flowerIcon = IElementHelper.get().item(suspiciousFlowerStack, 0.5f).size(new Vec2(11, 8)).translate(new Vec2(1, -2));
                    tooltip.add(Component.translatable("hungrycows.suspiciously_milkable.flower"));
                    tooltip.append(flowerIcon);
                    tooltip.append(Component.translatable(suspiciousFlowerStack.getDescriptionId()));
                }
            }
        }
    }
    @Override
    public void appendServerData(CompoundTag nbtCompound, EntityAccessor entityAccessor) {
    }

    @Override
    public ResourceLocation getUid(){
        return ResourceLocation.fromNamespaceAndPath("hungrycows","suspiciously_milkable");
    }

}
