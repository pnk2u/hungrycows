package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity;

import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.MoobloomRenderer;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.MoobloomRenderState;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoobloomRenderer.class)
public class MoobloomRendererMixin {

    @Inject(method = "extractRenderState(Lhouse/greenhouse/bovinesandbuttercups/content/entity/Moobloom;Lhouse/greenhouse/bovinesandbuttercups/client/renderer/entity/model/state/MoobloomRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/AgeableMobRenderer;extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", shift = At.Shift.AFTER), remap = false)
    public void extractRenderState(Moobloom moobloom, MoobloomRenderState moobloomRenderState, float partialTicks, CallbackInfo ci) {
        ((IHungryCows) moobloomRenderState).hungrycows$setHeadAngle(((IHungryCows) moobloom).hungrycows$getHeadAngle(partialTicks));
        ((IHungryCows) moobloomRenderState).hungrycows$setNeckAngle(((IHungryCows) moobloom).hungrycows$getNeckAngle(partialTicks));
        ((IHungryCows) moobloomRenderState).hungrycows$setMilkable(((IHungryCows) moobloom).hungrycows$isMilkable());
    }
}
