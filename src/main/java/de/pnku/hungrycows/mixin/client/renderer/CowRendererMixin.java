package de.pnku.hungrycows.mixin.client.renderer;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.config.HungryCowsConfigHelper;
import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.cow.Cow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowRenderer.class)
public abstract class CowRendererMixin extends MobRenderer<Cow, HungryCowRenderState, CowModel> {
    public CowRendererMixin(EntityRendererProvider.Context context) {
        super(context, new CowModel(context.bakeLayer(ModelLayers.COW)), 0.7F);
    }

    @Inject(method = "createRenderState()Lnet/minecraft/client/renderer/entity/state/CowRenderState;", at = @At("HEAD"), cancellable = true)
    public void injectedCreateRenderState(CallbackInfoReturnable<HungryCowRenderState> cir) {
        cir.setReturnValue(new HungryCowRenderState());
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/cow/Cow;Lnet/minecraft/client/renderer/entity/state/CowRenderState;F)V", at = @At("HEAD"))
    public void injectedExtractRenderState(Cow cow, CowRenderState cowRenderState, float f, CallbackInfo ci) {
        HungryCowRenderState hungryCowRenderState = (HungryCowRenderState) cowRenderState;
        super.extractRenderState(cow, hungryCowRenderState, f);
        hungryCowRenderState.headAngle = ((IHungryCows) cow).hungrycows$getHeadAngle(f);
        hungryCowRenderState.neckAngle = ((IHungryCows) cow).hungrycows$getNeckAngle(f);
        hungryCowRenderState.isMilkable = ((IHungryCows) cow).hungrycows$isMilkable();
    }
}
