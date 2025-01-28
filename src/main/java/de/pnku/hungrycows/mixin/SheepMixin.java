package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.config.HungryCowsConfig;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @Unique
    Sheep thisSheep = (Sheep) (Object) this;
    @Unique
    private HungryCowsConfig config = HungryCowsConfig.getInstance();

    @Inject(method = "ate", at = @At("TAIL"))
    public void injectedAte(CallbackInfo ci) {
        if (thisSheep.getHealth() < thisSheep.getMaxHealth()){
            thisSheep.heal(config.getSheepBlockEatHealAmount());
        }
    }

}
