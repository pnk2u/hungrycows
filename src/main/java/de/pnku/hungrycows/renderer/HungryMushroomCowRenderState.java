package de.pnku.hungrycows.renderer;

import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Unique;

public class HungryMushroomCowRenderState extends MushroomCowRenderState implements IHungryCows {
    public MushroomCow.Variant variant;
    public float headAngle;
    public float neckAngle;
    public boolean isMilkable;

    @Override
    public float hungrycows$getNeckAngle(float delta) {
        return this.neckAngle;
    }

    @Override
    public float hungrycows$getNeckAngle() {
        return this.neckAngle;
    }

    @Override
    public float hungrycows$getHeadAngle(float delta) {
        return this.headAngle;
    }

    @Override
    public float hungrycows$getHeadAngle() {
        return this.headAngle;
    }

    @Override
    public void hungrycows$setHeadAngle(float angle) {
        this.headAngle = angle;
    }

    @Override
    public void hungrycows$setNeckAngle(float angle) {
        this.neckAngle = angle;
    }

    @Override
    public boolean hungrycows$isEating() {
        return false;
    }

    @Unique public boolean hungrycows$isMilkable() {
        return this.isMilkable;
    }

    @Override
    public void hungrycows$setMilkable(boolean milkable) {
        this.isMilkable = milkable;
    }

    @Override
    public boolean hungrycows$isMooshroom() {
        return true;
    }

    @Override
    public boolean hungrycows$isCow() {
        return false;
    }

    @Override
    public String hungrycows$getName() {
        return "mooshroom";
    }

    // Unimplemented

    @Override
    public boolean hungrycows$isMilked() {
        return false;
    }

    @Override
    public void hungrycows$setMilked(boolean milked) {
    }

    @Override
    public int hungrycows$getCowHasBeenFedManuallyTimer() {
        return 0;
    }

    @Override
    public void hungrycows$setCowHasBeenFedManuallyTimer(int time) {
    }

    @Override
    public int hungrycows$getSheepHasBeenFedManuallyTimer() {
        return 0;
    }

    @Override
    public void hungrycows$setSheepHasBeenFedManuallyTimer(int time) {
    }

    @Override
    public int hungrycows$getGoatHasBeenFedManuallyTimer() {
        return 0;
    }

    @Override
    public void hungrycows$setGoatHasBeenFedManuallyTimer(int time) {
    }

    @Override
    public int hungrycows$getFeedableHasBeenFedManuallyTimer() {
        return 0;
    }

    @Override
    public void hungrycows$setFeedableHasBeenFedManuallyTimer(int time) {
    }

    @Override
    public ItemStack hungrycows$getEdibleMilk() {
        return null;
    }

    @Override
    public ItemStack hungrycows$getSuspiciousFlowerStack() {
        return null;
    }

    @Override
    public void hungrycows$setSuspiciousFlowerStack(ItemStack stack) {
    }
}
