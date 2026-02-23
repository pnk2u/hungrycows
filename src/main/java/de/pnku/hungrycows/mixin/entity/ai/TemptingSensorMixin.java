package de.pnku.hungrycows.mixin.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiPredicate;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.checkFeedability;

@Mixin(TemptingSensor.class)
public abstract class TemptingSensorMixin {

    @ModifyArg(method = "forAnimal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/sensing/TemptingSensor;<init>(Ljava/util/function/BiPredicate;)V"))
    private static BiPredicate<PathfinderMob, ItemStack> isFood(BiPredicate<PathfinderMob, ItemStack> temptations) {
        return temptations.or((mob, stack) -> mob instanceof Goat goat && checkFeedability(stack, goat));
    }

}
