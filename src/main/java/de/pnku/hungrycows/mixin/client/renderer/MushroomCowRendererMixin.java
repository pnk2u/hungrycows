package de.pnku.hungrycows.mixin.client.renderer;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.HungryCowsEntityInterface;
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

import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.milkabilitySettings;
import static de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO.HIDE_TEXTURE_AND_MODEL;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin {

    @Shadow
    @Final
    private static Map<MushroomCow.MushroomType, ResourceLocation> TEXTURES;

    @Inject(method = "getTextureLocation*", at = @At("HEAD"), cancellable = true)
    public void injectedGetTextureLocation(MushroomCow mushroomCow, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation variantTextureId = (ResourceLocation)TEXTURES.get(mushroomCow.getVariant());
        if (((HungryCowsEntityInterface) mushroomCow).hungrycows$isMilkable() && !milkabilitySettings.milkableCowDisplayType().equals(HIDE_TEXTURE_AND_MODEL)){
            String path = "textures/entity/cow/milkable_" + (variantTextureId.getPath().contains("brown") ? "brown" : "red" ) + "_mooshroom.png";
            cir.setReturnValue(HungryCows.withModId(path));
        } else {
            cir.setReturnValue(variantTextureId);
        }
        return;
    }

}
