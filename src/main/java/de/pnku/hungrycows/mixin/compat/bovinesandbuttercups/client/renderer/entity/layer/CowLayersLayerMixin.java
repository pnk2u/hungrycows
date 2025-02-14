package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity.layer;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import house.greenhouse.bovinesandbuttercups.api.CowConfiguration;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.layer.CowLayersLayer;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.showMilkableTexture;

@Mixin(CowLayersLayer.class)
public abstract class CowLayersLayerMixin <C extends CowConfiguration, T extends LivingEntityRenderState & CowVariantRenderState<LivingEntity, C, M>, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public CowLayersLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withPath(Ljava/util/function/UnaryOperator;)Lnet/minecraft/resources/ResourceLocation;",
                    shift = At.Shift.BY, by = 2)
    )
    private void modifyMappedTexture(
            PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntityRenderState renderState, float yRot, float xRot, CallbackInfo ci, @Local(ordinal = 0) LocalRef<ResourceLocation> mappedTextureLocationRef
    ) {
        ResourceLocation original = mappedTextureLocationRef.get();
        if (original.getPath().contains("sombercup") && renderState instanceof CowVariantRenderState<?,?,?> && showMilkableTexture()) {
            mappedTextureLocationRef.set(ResourceLocation.tryBuild(original.getNamespace(), original.getPath().replace("sombercup", "milkable_sombercup")));
        }
    }
}