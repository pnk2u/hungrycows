package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
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
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;", shift = At.Shift.BY, by = 3))
    public void injectedGetRenderType(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir, @Local LocalRef<ResourceLocation> modifiedResourceLocation) {
        String modifiedTexturePath = modifiedResourceLocation.get().getPath();
        EntityType<?> type = livingEntity.getType();
        if (type.equals(BovinesEntityTypes.MOOBLOOM) && modifiedTexturePath.contains("moobloom")) {
            if (((IHungryCows) livingEntity).hungrycows$isMilkable() && showMilkableTexture()) {
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
        } else {
            if ((type.equals(EntityType.MOOSHROOM) && modifiedTexturePath.contains("mooshroom")) || (type.equals(EntityType.COW) && modifiedTexturePath.contains("cow"))) {
                if (((IHungryCows) livingEntity).hungrycows$isMilkable() && showMilkableTexture()) {
                    modifiedResourceLocation.set(HungryCows.withModId("textures/entity/cow/milkable_" + (modifiedTexturePath.contains("mooshroom") ? (modifiedTexturePath.contains("brown") ? "brown" : "red") + "_mooshroom.png" : "cow.png")));
                }
            }
        }
    }
}
