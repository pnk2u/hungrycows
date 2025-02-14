package de.pnku.hungrycows.mixin.client.model;

import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.getMilkableCowBodyWithUdderCubeListBuilder;

@Mixin(CowModel.class)
public abstract class CowModelMixin<Cow extends net.minecraft.world.entity.animal.Cow> extends QuadrupedModel<Cow> {
    public CowModelMixin(ModelPart root, boolean headScaled, float childHeadYOffset, float childHeadZOffset, float invertedChildHeadScale, float invertedChildBodyScale, int childBodyYOffset) {
        super(root, headScaled, childHeadYOffset, childHeadZOffset, invertedChildHeadScale, invertedChildBodyScale, childBodyYOffset);
    }

    @ModifyArg(method = "createBodyLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/PartDefinition;addOrReplaceChild(Ljava/lang/String;Lnet/minecraft/client/model/geom/builders/CubeListBuilder;Lnet/minecraft/client/model/geom/PartPose;)Lnet/minecraft/client/model/geom/builders/PartDefinition;"))
    private static CubeListBuilder injectedCreateBodyLayer(String name, CubeListBuilder cubeListBuilder, PartPose partPose) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder);
    }

    @Unique
    private float headAngle;

    @Override
    public void prepareMobModel(Cow cowEntity, float limbAngle, float limbDistance, float tickDelta) {
        super.prepareMobModel(cowEntity, limbAngle, limbDistance, tickDelta);
        this.head.y = 6.0F + ((IHungryCows) cowEntity).hungrycows$getNeckAngle(tickDelta) * 9.0F;
        this.headAngle = ((IHungryCows) cowEntity).hungrycows$getHeadAngle(tickDelta);
    }

    @Override
    public void setupAnim(Cow cowEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setupAnim(cowEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.head.xRot = this.headAngle;
    }
}
