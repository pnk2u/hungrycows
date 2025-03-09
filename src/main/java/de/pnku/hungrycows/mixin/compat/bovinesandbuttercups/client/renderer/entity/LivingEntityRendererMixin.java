package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.renderer.HungryCowRenderState;
import de.pnku.hungrycows.renderer.HungryMushroomCowRenderState;
import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.api.CowConfiguration;
import house.greenhouse.bovinesandbuttercups.client.api.CowVariantRenderState;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.MoobloomRenderState;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.showMilkableTexture;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    protected LivingEntityRendererMixin() {
    }

    @Inject(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/ResourceLocation;", shift = At.Shift.BY, by = 3))
    public void injectedGetRenderType(S renderState, boolean isVisible, boolean renderTranslucent, boolean appearsGlowing, CallbackInfoReturnable<RenderType> cir, @Local LocalRef<ResourceLocation> modifiedResourceLocation) {
        String modifiedTexturePath = modifiedResourceLocation.get().getPath();
        if (renderState instanceof MoobloomRenderState moobloomRenderState && modifiedTexturePath.contains("moobloom")) {
            if (((IHungryCows) moobloomRenderState).hungrycows$isMilkable() && showMilkableTexture()) {
                String modifier = "";
                if (!modifiedTexturePath.contains("sombercup")) {
                    for (Pack resourcePack : ((Minecraft.getInstance())).getResourcePackRepository().getSelectedPacks()) {
                        if (resourcePack.getId().equals("bovinesandbuttercups:mojang")) {
                            modifier = "mojang_";
                            break;
                        }
                    }
                }
                modifiedResourceLocation.set(ResourceLocation.tryParse(modifiedResourceLocation.get().toString().replace("moobloom/", "moobloom/" + modifier + "milkable_")));
            }
        } else if ((renderState instanceof HungryMushroomCowRenderState && modifiedResourceLocation.get().getPath().contains("mooshroom")) || (renderState instanceof HungryCowRenderState && modifiedResourceLocation.get().getPath().contains("cow"))) {
            if (((HungryMushroomCowRenderState) renderState).isMilkable && showMilkableTexture()) {
                modifiedResourceLocation.set(HungryCows.withModId("textures/entity/cow/milkable_" + (modifiedResourceLocation.get().getPath().contains("brown") ? "brown" : "red") + "_mooshroom.png"));
            }
        }
    }
}
