package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity.model;

import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.MoobloomModel;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MoobloomModel.class)
public abstract class MoobloomModelMixin extends HierarchicalModel<Moobloom> {

    @Shadow @Final private CowModel<Moobloom> cowModel;

    public MoobloomModelMixin(ModelPart root) {
    }

    @Unique
    private float headAngle;

    @Unique
    private float neckAngle;

    @Redirect(method = "setupAnim(Lhouse/greenhouse/bovinesandbuttercups/content/entity/Moobloom;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/CowModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    public void redirectedSetupAnim(CowModel<Moobloom> instance, Entity moobloom, float limbSwing, float limbSwingAmount, float delta, float yRot, float xRot) {
        this.cowModel.setupAnim((Moobloom) moobloom, limbSwing, limbSwingAmount, delta, yRot, xRot);
        this.cowModel.getHead().xRot = this.headAngle;
        this.cowModel.getHead().y = this.neckAngle;
    }

    @Override
    public void prepareMobModel(@NotNull Moobloom moobloomEntity, float limbAngle, float limbDistance, float tickDelta) {
        this.cowModel.prepareMobModel(moobloomEntity, limbAngle, limbDistance, tickDelta);
        this.neckAngle = 6.0F + ((IHungryCows) moobloomEntity).hungrycows$getNeckAngle(tickDelta) * 9.0F;
        this.headAngle = ((IHungryCows) moobloomEntity).hungrycows$getHeadAngle(tickDelta);
    }
}
