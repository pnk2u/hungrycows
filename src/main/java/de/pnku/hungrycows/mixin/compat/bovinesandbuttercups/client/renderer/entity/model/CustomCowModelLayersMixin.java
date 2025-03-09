package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity.model;

import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.CustomCowModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.*;

@Mixin(CustomCowModelLayers.class)
public class CustomCowModelLayersMixin {

    @ModifyArg(method = "createWarm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/PartDefinition;addOrReplaceChild(Ljava/lang/String;Lnet/minecraft/client/model/geom/builders/CubeListBuilder;Lnet/minecraft/client/model/geom/PartPose;)Lnet/minecraft/client/model/geom/builders/PartDefinition;"))
    private static CubeListBuilder modifiedCreateBuffaloArgCubeListBuilder(String name, CubeListBuilder cubeListBuilder, PartPose partPose) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder);
    }
    @ModifyArg(method = "createLush", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/PartDefinition;addOrReplaceChild(Ljava/lang/String;Lnet/minecraft/client/model/geom/builders/CubeListBuilder;Lnet/minecraft/client/model/geom/PartPose;)Lnet/minecraft/client/model/geom/builders/PartDefinition;"))
    private static CubeListBuilder modifiedCreateOxArgCubeListBuilder(String name, CubeListBuilder cubeListBuilder, PartPose partPose) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder);
    }
    @ModifyArg(method = "createSculk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/PartDefinition;addOrReplaceChild(Ljava/lang/String;Lnet/minecraft/client/model/geom/builders/CubeListBuilder;Lnet/minecraft/client/model/geom/PartPose;)Lnet/minecraft/client/model/geom/builders/PartDefinition;"))
    private static CubeListBuilder modifiedCreateFlatArgCubeListBuilder(String name, CubeListBuilder cubeListBuilder, PartPose partPose) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder, "sculk");
    }
    @ModifyArg(method = "createCold", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/PartDefinition;addOrReplaceChild(Ljava/lang/String;Lnet/minecraft/client/model/geom/builders/CubeListBuilder;Lnet/minecraft/client/model/geom/PartPose;)Lnet/minecraft/client/model/geom/builders/PartDefinition;"))
    private static CubeListBuilder modifiedCreateHighlandArgCubeListBuilder(String name, CubeListBuilder cubeListBuilder, PartPose partPose) {
        return getMilkableCowBodyWithUdderCubeListBuilder(name, cubeListBuilder);
    }

}
