package de.pnku.hungrycows.mixin.entity.ai;

import de.pnku.hungrycows.config.HungryCowsConfigHelper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Pseudo
@Mixin(targets = "net.minecraft.class_6054", remap = false)
public abstract class LegacyGoatAiMixin {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(
            method = "method_35181",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void injectedGetTemptations(CallbackInfoReturnable<Predicate<?>> cir) {
        cir.setReturnValue((Predicate<ItemStack>) HungryCowsConfigHelper::checkFeedabilityLegacyGoat);
    }
}