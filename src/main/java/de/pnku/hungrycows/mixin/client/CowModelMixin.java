package de.pnku.hungrycows.mixin.client;

import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(CowModel.class)
public abstract class CowModelMixin extends QuadrupedModel<LivingEntityRenderState> {
    public CowModelMixin(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(LivingEntityRenderState livingEntityRenderState) {
        if (!(livingEntityRenderState instanceof MushroomCowRenderState)) {
            HungryCowRenderState hungryCowRenderState = (HungryCowRenderState) livingEntityRenderState;
            super.setupAnim(hungryCowRenderState);
            this.head.y = this.head.y + hungryCowRenderState.neckAngle * 9.0F * hungryCowRenderState.ageScale;
            this.head.xRot = hungryCowRenderState.headAngle;
        } else {
            super.setupAnim(livingEntityRenderState);
            this.head.xRot = livingEntityRenderState.xRot * ((float)Math.PI / 180F);
            this.head.yRot = livingEntityRenderState.yRot * ((float)Math.PI / 180F);
            float f = livingEntityRenderState.walkAnimationPos;
            float g = livingEntityRenderState.walkAnimationSpeed;
            this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
            this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
            this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
            this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
        }
    }
}
