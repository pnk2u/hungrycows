package de.pnku.hungrycows.mixin.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {
    public MilkBucketItemMixin(Item.Properties properties) {super(properties);}

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void injectedFinishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir){
        if (!level.isClientSide()) {
            super.finishUsingItem(stack, level, livingEntity);
        }
    }

    @Redirect(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemUtils;createFilledResult(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack redirectedFinishUsingItemCreateFilledResult(ItemStack stack, Player player, ItemStack filledResult, boolean bl){
        if (!stack.isEmpty()) {
            if (player instanceof Player) {
                if (!player.hasInfiniteMaterials()) {
                    ItemStack itemStack = new ItemStack(Items.BUCKET);
                    if (stack.getCount() > 1) {
                        if (!player.getInventory().add(itemStack)) {
                            player.drop(itemStack, false);
                        }
                        return stack;
                    } else if (!player.level.isClientSide()) {
                        player.getInventory().add(itemStack);
                        return stack;
                    }
                }
            }
            return ItemStack.EMPTY;
        }
        return new ItemStack(Items.BUCKET);
    }
}
