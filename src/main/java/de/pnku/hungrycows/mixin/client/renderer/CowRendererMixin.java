package de.pnku.hungrycows.mixin.client.renderer;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.config.HungryCowsConfigHelper;
import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowRenderer.class)
public abstract class CowRendererMixin extends AgeableMobRenderer<Cow, HungryCowRenderState, CowModel> {
    public CowRendererMixin(EntityRendererProvider.Context context) {
        super(context, new CowModel(context.bakeLayer(ModelLayers.COW)), new CowModel(context.bakeLayer(ModelLayers.COW_BABY)), 0.7F);
    }

    @Shadow @Final
    private static ResourceLocation COW_LOCATION;

    @Inject(method = "createRenderState()Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;", at = @At("HEAD"), cancellable = true)
    public void injectedCreateRenderState(CallbackInfoReturnable<HungryCowRenderState> cir) {
        cir.setReturnValue(new HungryCowRenderState());
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/Cow;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    public void injectedExtractRenderState(Cow cow, LivingEntityRenderState livingEntityRenderState, float f, CallbackInfo ci) {
        HungryCowRenderState hungryCowRenderState = (HungryCowRenderState) livingEntityRenderState;
        super.extractRenderState(cow, hungryCowRenderState, f);
        hungryCowRenderState.headAngle = ((IHungryCows) cow).hungrycows$getHeadAngle(f);
        hungryCowRenderState.neckAngle = ((IHungryCows) cow).hungrycows$getNeckAngle(f);
        hungryCowRenderState.isMilkable = ((IHungryCows) cow).hungrycows$isMilkable();
    }
}
