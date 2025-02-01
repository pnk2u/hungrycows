package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.item.PinkFoodComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {
    public MilkBucketItemMixin(Item.Properties properties) {super(properties);}

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    protected void injectedFinishUsingItem(ItemStack stack, Level level, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof Player player && Objects.requireNonNull(stack.get(DataComponents.FOOD)).equals(PinkFoodComponents.MILK_BUCKET)) {
            player.addItem(new ItemStack(Items.BUCKET));
            stack.consume(1, user);
            cir.setReturnValue(stack);
            return;
        }
    }
}
