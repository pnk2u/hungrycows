package de.pnku.hungrycows.mixin.entity.ai;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.goat.GoatAi;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.*;

@Mixin(GoatAi.class)
public abstract class GoatAiMixin {

    @Inject(method = "getTemptations", at = @At("HEAD"), cancellable = true)
    private static void injectedGetTemptations(CallbackInfoReturnable<Ingredient> cir){
        cir.setReturnValue(Ingredient.of(getFeedableItemsFromConfig(EntityType.GOAT)));
    }
}
