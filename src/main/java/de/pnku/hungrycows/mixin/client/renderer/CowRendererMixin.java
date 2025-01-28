package de.pnku.hungrycows.mixin.client.renderer;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.config.HungryCowsConfig;
import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowRenderer.class)
public abstract class CowRendererMixin extends MobRenderer<Cow, CowModel<Cow>> {
    public CowRendererMixin(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 0.7F);
    }

    @Unique
    private HungryCowsConfig config = HungryCowsConfig.getInstance();

    @Shadow @Final
    private static ResourceLocation COW_LOCATION;

    @Inject(method = "getTextureLocation*", at = @At("HEAD"), cancellable = true)
    public void injectedGetTextureLocation(Cow cow, CallbackInfoReturnable<ResourceLocation> cir) {
        if (((ICowEntity) cow).hungrycows$isMilkable() && !config.isMilkableModelHidden()){
            cir.setReturnValue(HungryCows.withModId("textures/cow/milkable_cow.png"));
        } else {
            cir.setReturnValue(COW_LOCATION);
        }
        return;
    }
}
