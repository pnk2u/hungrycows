package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.client.renderer.entity.state;

import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.client.renderer.entity.model.state.MoobloomRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoobloomRenderState.class)
public abstract class MoobloomRenderStateMixin implements IHungryCows {

    @Unique
    private float hungrycows$headAngle;

    @Unique
    private float hungrycows$neckAngle;

    @Unique public boolean hungrycows$isMilkable;

    @Unique
    public float hungrycows$getHeadAngle() {
        return hungrycows$headAngle;
    }

    @Unique
    public void hungrycows$setHeadAngle(float headAngle) {
        this.hungrycows$headAngle = headAngle;
    }

    @Unique
    public float hungrycows$getNeckAngle() {
        return hungrycows$neckAngle;
    }

    @Unique
    public void hungrycows$setNeckAngle(float neckAngle) {
        this.hungrycows$neckAngle = neckAngle;
    }

    @Unique
    public boolean hungrycows$isMilkable() {
        return hungrycows$isMilkable;
    }

    @Unique
    public void hungrycows$setMilkable(boolean milkable) {
        this.hungrycows$isMilkable = milkable;
    }
}
