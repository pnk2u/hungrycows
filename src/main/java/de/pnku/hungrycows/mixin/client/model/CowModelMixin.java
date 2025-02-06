package de.pnku.hungrycows.mixin.client.model;

import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.util.HungryCowsEntityInterface;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.milkabilitySettings;
import static de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO.HIDE_NONE;

@Mixin(CowModel.class)
public abstract class CowModelMixin<Cow extends net.minecraft.world.entity.animal.Cow> extends QuadrupedModel<LivingEntityRenderState> {
    public CowModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "createBodyLayer", at = @At("HEAD"), cancellable = true)
    private static void injectedCreateBodyLayer(CallbackInfoReturnable<LayerDefinition> cir) {
        if (milkabilitySettings.milkableCowDisplayType().equals(HIDE_NONE)) {
            MeshDefinition meshDefinition = new MeshDefinition();
            PartDefinition partDefinition = meshDefinition.getRoot();
            partDefinition.addOrReplaceChild(
                    "head",
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                            .texOffs(22, 0)
                            .addBox("right_horn", -5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F)
                            .texOffs(22, 0)
                            .addBox("left_horn", 4.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F),
                    PartPose.offset(0.0F, 4.0F, -8.0F)
            );
            partDefinition.addOrReplaceChild(
                    "body",
                    CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                            .texOffs(52, 0).addBox("udder1", -2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F)
                            // Any following boxes will only get "rendered" when isMilkable() is true as defined in CowRendererMixin.injectedGetTextureLocation by nature of the used pixels being transparent in the original texture
                            .texOffs(52, 7).addBox("udder1.5", -2.0F, 2.0F, -8.55F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0, 0, -0.45F))
                            .texOffs(52, 8).addBox("udder2", -2.0F, 3.0F, -8.625F - 0.1F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F, 0, -0.375F))
                            .texOffs(60, 12).addBox("right_front_teat", -2.025F, 3.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                            .texOffs(60, 12).addBox("left_front_teat", 1.075F, 3.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                            .texOffs(60, 12).addBox("right_hind_teat", -2.025F, 6.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F))
                            .texOffs(60, 12).addBox("left_hind_teat", 1.075F, 6.0F, -8.8115F - 0.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.45F)),
                    PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
            );
            CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
            partDefinition.addOrReplaceChild("right_hind_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, 7.0F));
            partDefinition.addOrReplaceChild("left_hind_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, 7.0F));
            partDefinition.addOrReplaceChild("right_front_leg", cubeListBuilder, PartPose.offset(-4.0F, 12.0F, -6.0F));
            partDefinition.addOrReplaceChild("left_front_leg", cubeListBuilder, PartPose.offset(4.0F, 12.0F, -6.0F));
            cir.setReturnValue(LayerDefinition.create(meshDefinition, 64, 32));
            return;
        }
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
