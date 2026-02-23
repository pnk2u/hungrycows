package de.pnku.hungrycows.mixin.client.renderer;

import de.pnku.hungrycows.util.IHungryCows;
import de.pnku.hungrycows.renderer.HungryMushroomCowRenderState;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends AgeableMobRenderer<MushroomCow, HungryMushroomCowRenderState, CowModel> {

    @Shadow
    @Final
    private static Map<MushroomCow.Variant, Identifier> TEXTURES;

    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel adultModel, CowModel babyModel, float scale) {
        super(context, new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM)), new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM_BABY)), 0.7F);
    }

    @Inject(method = "createRenderState()Lnet/minecraft/client/renderer/entity/state/MushroomCowRenderState;", at = @At("HEAD"), cancellable = true)
    public void injectedCreateRenderState(CallbackInfoReturnable<HungryMushroomCowRenderState> cir) {
        cir.setReturnValue(new HungryMushroomCowRenderState());
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/cow/MushroomCow;Lnet/minecraft/client/renderer/entity/state/MushroomCowRenderState;F)V", at = @At("HEAD"))
    public void injectedExtractRenderState(MushroomCow mushroomCow, MushroomCowRenderState mushroomCowRenderState, float f, CallbackInfo ci) {
        HungryMushroomCowRenderState hungryMushroomCowRenderState = (HungryMushroomCowRenderState) mushroomCowRenderState;
        super.extractRenderState(mushroomCow, hungryMushroomCowRenderState, f);
        hungryMushroomCowRenderState.headAngle = ((IHungryCows) mushroomCow).hungrycows$getHeadAngle(f);
        hungryMushroomCowRenderState.neckAngle = ((IHungryCows) mushroomCow).hungrycows$getNeckAngle(f);
        hungryMushroomCowRenderState.variant = mushroomCow.getVariant();
        hungryMushroomCowRenderState.isMilkable = ((IHungryCows) mushroomCow).hungrycows$isMilkable();
    }

}