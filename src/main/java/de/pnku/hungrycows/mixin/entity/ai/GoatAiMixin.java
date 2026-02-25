package de.pnku.hungrycows.mixin.entity.ai;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.pnku.hungrycows.item.HungryCowsItemTags;
import net.minecraft.world.entity.animal.goat.GoatAi;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GoatAi.class)
public abstract class GoatAiMixin {
    @ModifyReturnValue(method = "getTemptations", at = @At(value = "RETURN"))
    private static Ingredient wrappedGetTemptationsGetTemptations(Ingredient original) {
        return Ingredient.of(HungryCowsItemTags.GOAT_FEEDABLE);
    }
}
