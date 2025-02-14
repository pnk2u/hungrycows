package de.pnku.hungrycows.mixin.client.renderer;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.showMilkableTexture;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin {

    @Shadow
    @Final
    private static Map<MushroomCow.MushroomType, ResourceLocation> TEXTURES;

    @Inject(method = "getTextureLocation*", at = @At("HEAD"), cancellable = true)
    public void injectedGetTextureLocation(MushroomCow mushroomCow, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation variantTextureId = (ResourceLocation)TEXTURES.get(mushroomCow.getVariant());
        if (((IHungryCows) mushroomCow).hungrycows$isMilkable() && showMilkableTexture()){
            String path = "textures/entity/cow/milkable_" + (variantTextureId.getPath().contains("brown") ? "brown" : "red" ) + "_mooshroom.png";
            cir.setReturnValue(HungryCows.withModId(path));
        } else {
            cir.setReturnValue(variantTextureId);
        }
        return;
    }

}
