package de.pnku.hungrycows.mixin.entity;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.item.HungryCowsItemComponents;
import de.pnku.hungrycows.util.HungryCowsEntityInterface;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.*;

@Mixin(Goat.class)
public abstract class GoatMixin extends Animal implements Shearable, HungryCowsEntityInterface {
    @Unique
    Goat thisGoat = (Goat) (Object) this;
    @Unique
    protected int hasBeenFedManuallyTimer;

    public GoatMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void aiStep(CallbackInfo ci) {
        this.hungrycows$setGoatHasBeenFedManuallyTimer(!this.isBaby() ? Math.max(1, this.hungrycows$getGoatHasBeenFedManuallyTimer() - 1) : 0);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void injectedDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(HungryCows.IS_MILKED_GOAT, (byte)0);
        builder.define(HungryCows.FED_TIMER_GOAT, 0);
    }

    // Goats should not be sheared... yet.
    @Override
    public boolean readyForShearing() {
        return false;
    }

    @Unique
    public boolean hungrycows$isMilkable() { return this.isAlive() && !this.hungrycows$isMilked() && !this.isBaby();
    }
    @Unique
    public boolean hungrycows$isMilked() {
        return ((Byte)this.entityData.get(HungryCows.IS_MILKED_GOAT)) != 0;
    }
    @Unique
    public void hungrycows$setMilked(boolean isMilked) {
        byte isMilkedByte = isMilked ? (byte) 1 : (byte) 0;

        this.entityData.set(HungryCows.IS_MILKED_GOAT, isMilkedByte);
    }

    @Unique
    public int hungrycows$getGoatHasBeenFedManuallyTimer() {
        this.hasBeenFedManuallyTimer = this.getEntityData().get(HungryCows.FED_TIMER_GOAT);
        return !this.isBaby() ? this.hasBeenFedManuallyTimer : 0;
    }

    @Unique
    public void hungrycows$setGoatHasBeenFedManuallyTimer(int time) {
        this.hasBeenFedManuallyTimer = time;
        this.getEntityData().set(HungryCows.FED_TIMER_GOAT, time);
    }

    @Unique
    public ItemStack hungrycows$getEdibleMilk(){
        ItemStack edibleMilk = new ItemStack(Items.MILK_BUCKET);
        edibleMilk.set(DataComponents.FOOD, HungryCowsItemComponents.GOAT_MILK_BUCKET);
        edibleMilk.set(DataComponents.MAX_STACK_SIZE, 16);
        edibleMilk.set(DataComponents.ITEM_NAME, Component.translatable("item.hungrycows.milk_bucket.goat"));

        return edibleMilk;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        SoundEvent goatEatSound = thisGoat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_EAT : SoundEvents.GOAT_EAT;
        SoundEvent goatMilkSound = thisGoat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_MILK : SoundEvents.GOAT_MILK;
        if (checkFeedability(itemStack, thisGoat)) {
            int s = hungrycows$getGoatHasBeenFedManuallyTimer();
            boolean isMilked = hungrycows$isMilked();
            int feedabilityRegainTime = milkabilitySettings.secondsUntilFeedabilityRegain() * 20;

            if ((isMilked && s <= 1)) {
                hungrycows$setMilked(false);
                hungrycows$setGoatHasBeenFedManuallyTimer(feedabilityRegainTime);
                level().playSound(player, this, goatEatSound, SoundSource.NEUTRAL,0.95F, 1.35F);
                itemStack.consume(1, player);

                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
                level().playSound(player, this.getOnPos(), goatEatSound, SoundSource.NEUTRAL,0.95F, 1.44F);
                itemStack.consume(1, player);

                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
            if (this.level().isClientSide) {
                cir.setReturnValue(InteractionResult.CONSUME);
                return;
            }
        }
        if (itemStack.is(Items.BUCKET)){
            if (!this.isBaby() && this.hungrycows$isMilkable()) {
                this.hungrycows$setMilked(true);
                player.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 0.237F, 3.17F);
                player.playSound(goatMilkSound, 1.317F, 1.237F);
                ItemStack itemStackMilk = ItemUtils.createFilledResult(itemStack, player, hungrycows$getEdibleMilk());
                player.setItemInHand(hand, itemStackMilk);

                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            }
            else cir.setReturnValue(InteractionResult.PASS);
        }
    }


    static {
        HungryCows.IS_MILKED_GOAT = SynchedEntityData.defineId(GoatMixin.class, EntityDataSerializers.BYTE);
        HungryCows.FED_TIMER_GOAT = SynchedEntityData.defineId(GoatMixin.class, EntityDataSerializers.INT);
    }
}
