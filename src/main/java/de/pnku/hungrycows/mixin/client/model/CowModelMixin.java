package de.pnku.hungrycows.mixin.client.model;

import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.renderer.HungryMushroomCowRenderState;
import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.model.animal.cow.CowModel;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.getMilkableCowBodyWithUdderCubeListBuilder;

@Mixin(CowModel.class)
public abstract class CowModelMixin<Cow extends net.minecraft.world.entity.animal.cow.Cow> extends QuadrupedModel<LivingEntityRenderState> {
    public CowModelMixin(ModelPart root) {
        super(root);
    }

    @ModifyArg(method = "createBaseCowModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/PartDefinition;addOrReplaceChild(Ljava/lang/String;Lnet/minecraft/client/model/geom/builders/CubeListBuilder;Lnet/minecraft/client/model/geom/PartPose;)Lnet/minecraft/client/model/geom/builders/PartDefinition;"))
    private static CubeListBuilder injectedCreateBaseCowModel(String name, CubeListBuilder cubeListBuilder, PartPose partPose) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder);
    }

    @Override
    public void setupAnim(LivingEntityRenderState livingEntityRenderState) {
        boolean isMoo = ((livingEntityRenderState instanceof MushroomCowRenderState));
        HungryMushroomCowRenderState mooState;
        HungryCowRenderState cowState;
        if (isMoo) {mooState = (HungryMushroomCowRenderState) livingEntityRenderState; cowState = null;}
        else {cowState = (HungryCowRenderState) livingEntityRenderState; mooState = null;}
        super.setupAnim(isMoo ? mooState : cowState);
        this.head.y = this.head.y + (float) Math.pow((isMoo ? mooState.neckAngle : cowState.neckAngle), 1.05) * 10.625F * (isMoo ? mooState.ageScale : cowState.ageScale);
        this.head.xRot = (isMoo ? mooState.headAngle : cowState.headAngle);
        float f = livingEntityRenderState.walkAnimationPos;
        float g = livingEntityRenderState.walkAnimationSpeed;
        this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
        this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
        this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
        this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g;
    }
}
