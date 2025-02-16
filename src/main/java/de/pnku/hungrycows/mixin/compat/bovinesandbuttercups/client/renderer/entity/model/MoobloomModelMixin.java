package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity.model;

import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.MoobloomModel;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.MoobloomRenderState;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoobloomModel.class)
public abstract class MoobloomModelMixin<T extends LivingEntityRenderState> extends QuadrupedModel<T> {

    public MoobloomModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lhouse/greenhouse/bovinesandbuttercups/client/renderer/entity/model/state/MoobloomRenderState;)V", at = @At(value = "HEAD"), remap = false)
    public void injectedSetupAnim(MoobloomRenderState moobloomRenderState, CallbackInfo ci) {
        super.setupAnim((T) moobloomRenderState);
        this.head.y = this.head.y + (((IHungryCows) moobloomRenderState).hungrycows$getNeckAngle()) * 9.0F * (moobloomRenderState.ageScale);
        this.head.xRot = ((IHungryCows) moobloomRenderState).hungrycows$getHeadAngle();
        float f = moobloomRenderState.walkAnimationPos;
        float g = moobloomRenderState.walkAnimationSpeed;
        this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
        this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
        this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
        this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
    }

    @Inject(method = "setupAnim(Lhouse/greenhouse/bovinesandbuttercups/client/renderer/entity/model/state/MoobloomRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/QuadrupedModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)V", shift = At.Shift.AFTER))
    public void injectedSetupAnimSetupAnimAfter(MoobloomRenderState moobloomRenderState, CallbackInfo ci) {
        this.head.y = this.head.y + (((IHungryCows) moobloomRenderState).hungrycows$getNeckAngle()) * 9.0F * (moobloomRenderState.ageScale);
        this.head.xRot = ((IHungryCows) moobloomRenderState).hungrycows$getHeadAngle();
    }
}
