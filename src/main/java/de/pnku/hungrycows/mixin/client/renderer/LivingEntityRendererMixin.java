package de.pnku.hungrycows.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.config.HungryCowsConfigHelper;
import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.renderer.HungryMushroomCowRenderState;
import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @WrapOperation(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation wrappedGetRenderTypeAtGetTextureLocation(LivingEntityRenderer<T, S, M> instance, S livingEntityRenderState, Operation<ResourceLocation> original) {
        ResourceLocation originalTexture = original.call(instance, livingEntityRenderState);
        if ((instance instanceof CowRenderer || instance instanceof MushroomCowRenderer) && HungryCowsConfigHelper.showMilkableTexture() && ((livingEntityRenderState instanceof HungryCowRenderState hungryCowRenderState && hungryCowRenderState.hungrycows$isMilkable()) || (livingEntityRenderState instanceof HungryMushroomCowRenderState hungryMushroomCowRenderState && hungryMushroomCowRenderState.hungrycows$isMilkable()))) {
            String textureFileName = originalTexture.getPath().split("/")[originalTexture.getPath().split("/").length - 1];
            return HungryCows.withModId("textures/entity/cow/milkable_" + textureFileName);
        }
        return originalTexture;
    }

}
