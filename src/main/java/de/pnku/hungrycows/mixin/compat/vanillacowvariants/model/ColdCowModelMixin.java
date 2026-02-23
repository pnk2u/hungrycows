package de.pnku.hungrycows.mixin.compat.vanillacowvariants.model;

import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.getMilkableCowBodyWithUdderCubeListBuilder;

@Mixin(ColdCowModel.class)
public abstract class ColdCowModelMixin {
    @ModifyArg(method = "createBodyLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/PartDefinition;addOrReplaceChild(Ljava/lang/String;Lnet/minecraft/client/model/geom/builders/CubeListBuilder;Lnet/minecraft/client/model/geom/PartPose;)Lnet/minecraft/client/model/geom/builders/PartDefinition;"))
    private static CubeListBuilder modifiedCreateBaseCowModel(String name, CubeListBuilder cubeListBuilder, PartPose partPose) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder);
    }
}