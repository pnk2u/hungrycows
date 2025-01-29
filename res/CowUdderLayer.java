package de.pnku.hungrycows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.pnku.hungrycows.util.ICowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

@Environment(EnvType.CLIENT)
public class CowUdderLayer extends RenderLayer<Cow, CowModel<Cow>> {
    private static final ResourceLocation SHEEP_FUR_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sheep/sheep_fur.png");
    private final CowUdderModel<Cow> model;

    public CowUdderLayer(RenderLayerParent<Cow, CowModel<Cow>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new CowUdderModel<>(modelSet.bakeLayer(ModelLayers.SHEEP_FUR));
    }

    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            Cow cowEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (((ICowEntity)cowEntity).hungrycows$isMilkable()) {
            if (cowEntity.isInvisible()) {
                Minecraft minecraft = Minecraft.getInstance();
                boolean bl = minecraft.shouldEntityAppearGlowing(cowEntity);
                if (bl) {
                    this.getParentModel().copyPropertiesTo(this.model);
                    this.model.prepareMobModel(cowEntity, limbSwing, limbSwingAmount, partialTicks);
                    this.model.setupAnim(cowEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.outline(SHEEP_FUR_LOCATION));
                    this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(cowEntity, 0.0F), -16777216);
                }
            }
            coloredCutoutModelCopyLayerRender(
                    this.getParentModel(),
                    this.model,
                    SHEEP_FUR_LOCATION,
                    poseStack,
                    buffer,
                    packedLight,
                    cowEntity,
                    limbSwing,
                    limbSwingAmount,
                    ageInTicks,
                    netHeadYaw,
                    headPitch,
                    partialTicks,
                    16777215
            );
        }
    }
}

