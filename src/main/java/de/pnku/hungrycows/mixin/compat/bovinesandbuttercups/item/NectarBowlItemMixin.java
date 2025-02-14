package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.item;

import house.greenhouse.bovinesandbuttercups.content.component.BovinesDataComponents;
import house.greenhouse.bovinesandbuttercups.content.component.ItemNectar;
import house.greenhouse.bovinesandbuttercups.content.data.nectar.Nectar;
import house.greenhouse.bovinesandbuttercups.content.item.NectarBowlItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NectarBowlItem.class)
public abstract class NectarBowlItemMixin extends Item {
    public NectarBowlItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    public void injectedFinishUsingItem(ItemStack nectarBowlStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir){
        super.finishUsingItem(nectarBowlStack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, nectarBowlStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            ItemNectar effects = (ItemNectar)nectarBowlStack.getOrDefault(BovinesDataComponents.NECTAR, ItemNectar.EMPTY);
            ((Nectar)effects.holder().value()).effects().applyEffectInstance(livingEntity);
        }

        if (nectarBowlStack.isEmpty()) {
            cir.setReturnValue(new ItemStack(Items.BOWL));
            return;
        } else {
            if (livingEntity instanceof Player player && !player.hasInfiniteMaterials()) {
                ItemStack itemStack = new ItemStack(Items.BOWL);
                if (!player.getInventory().add(itemStack)) {
                    player.drop(itemStack, false);
                }
            }

            cir.setReturnValue(nectarBowlStack);
        }
        cir.cancel();
    }

}
