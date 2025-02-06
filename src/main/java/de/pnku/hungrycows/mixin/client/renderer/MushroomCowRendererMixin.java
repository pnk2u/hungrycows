package de.pnku.hungrycows.mixin.client.renderer;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.renderer.HungryMushroomCowRenderState;
import de.pnku.hungrycows.util.HungryCowsEntityInterface;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.milkabilitySettings;
import static de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO.HIDE_TEXTURE_AND_MODEL;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends AgeableMobRenderer<MushroomCow, HungryMushroomCowRenderState, CowModel> {

    @Shadow
    @Final
    private static Map<MushroomCow.Variant, ResourceLocation> TEXTURES;

    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel adultModel, CowModel babyModel, float scale) {
        super(context, new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM)), new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM_BABY)), 0.7F);
    }

    @Inject(method = "createRenderState()Lnet/minecraft/client/renderer/entity/state/MushroomCowRenderState;", at = @At("HEAD"), cancellable = true)
    public void injectedCreateRenderState(CallbackInfoReturnable<HungryMushroomCowRenderState> cir) {
        cir.setReturnValue(new HungryMushroomCowRenderState());
        return;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/MushroomCow;Lnet/minecraft/client/renderer/entity/state/MushroomCowRenderState;F)V", at = @At("HEAD"))
    public void injectedExtractRenderState(MushroomCow mushroomCow, MushroomCowRenderState mushroomCowRenderState, float f, CallbackInfo ci) {
        HungryMushroomCowRenderState hungryMushroomCowRenderState = (HungryMushroomCowRenderState) mushroomCowRenderState;
        super.extractRenderState((MushroomCow) mushroomCow, hungryMushroomCowRenderState, f);
        hungryMushroomCowRenderState.headAngle = ((HungryCowsEntityInterface) mushroomCow).hungrycows$getHeadAngle(f);
        hungryMushroomCowRenderState.neckAngle = ((HungryCowsEntityInterface) mushroomCow).hungrycows$getNeckAngle(f);
        hungryMushroomCowRenderState.variant = ((MushroomCow) mushroomCow).getVariant();
        hungryMushroomCowRenderState.isMilkable = ((HungryCowsEntityInterface) mushroomCow).hungrycows$isMilkable();
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void injectedGetTextureLocation(LivingEntityRenderState livingEntityRenderState, CallbackInfoReturnable<ResourceLocation> cir) {
        HungryMushroomCowRenderState hungryMushroomCowRenderState = (HungryMushroomCowRenderState) livingEntityRenderState;
        ResourceLocation variantTextureId = (ResourceLocation)TEXTURES.get(hungryMushroomCowRenderState.variant != null ? hungryMushroomCowRenderState.variant : MushroomCow.Variant.BROWN);
        if (hungryMushroomCowRenderState.isMilkable && !milkabilitySettings.milkableCowDisplayType().equals(HIDE_TEXTURE_AND_MODEL)){
            cir.setReturnValue(variantTextureId.getPath().contains("brown") ? HungryCows.withModId("textures/cow/milkable_brown_mooshroom.png") : HungryCows.withModId("textures/cow/milkable_red_mooshroom.png"));
        } else {
            cir.setReturnValue(variantTextureId);
        }
        return;
    }

}
