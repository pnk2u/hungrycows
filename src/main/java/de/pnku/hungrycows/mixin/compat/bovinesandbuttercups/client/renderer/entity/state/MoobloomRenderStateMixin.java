package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity.state;

import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.MoobloomRenderState;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoobloomRenderState.class)
public abstract class MoobloomRenderStateMixin implements IHungryCows {

    @Unique public boolean hungrycows$isMilkable;

    @Inject(method = "extractDefaultRenderStates(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("TAIL"))
    public void injectedExtractDefaultRenderStates(LivingEntity par1, CallbackInfo ci) {
        this.hungrycows$isMilkable = ((IHungryCows)par1).hungrycows$isMilkable();
    }

}
