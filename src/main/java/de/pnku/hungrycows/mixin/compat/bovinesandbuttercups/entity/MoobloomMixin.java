package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.entity;

import com.llamalad7.mixinextras.sugar.Local;
import de.pnku.hungrycows.item.HungryCowsItemComponents;
import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.blockEatSettings;
import static de.pnku.hungrycows.config.HungryCowsConfigHelper.checkFeedability;
import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.*;

@Mixin(Moobloom.class)
public abstract class MoobloomMixin extends Cow implements Shearable, IHungryCows {

    @Unique
    private EatBlockGoal moobloomEatGrassGoal;

    @Unique
    private int moobloomEatGrassTimer;

    public MoobloomMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public Cow getBreedOffspring(ServerLevel level, AgeableMob otherParent){
        return null;
    }

    @Unique public boolean hungrycows$isMooshroom(){
        return false;
    }
    @Unique public boolean hungrycows$isCow(){
        return false;
    }
    @Unique public String hungrycows$getName(){
        return "moobloom";
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    protected void injectedRegisterGoals(CallbackInfo info) {
        this.goalSelector.removeAllGoals(goal -> goal instanceof EatBlockGoal);
        this.moobloomEatGrassGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal((int) Math.pow(2, 4 - blockEatSettings.grassEatProbability()), this.moobloomEatGrassGoal);
    }

    @Override
    protected void customServerAiStep() {
        this.moobloomEatGrassTimer = this.moobloomEatGrassGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        this.moobloomEatGrassTimer = Math.max(0, this.moobloomEatGrassTimer - 1);

        super.aiStep();
    }

    @Override public void thunderHit(ServerLevel world, LightningBolt bolt){
        this.setRemainingFireTicks(this.getRemainingFireTicks() + 1);
        if (this.getRemainingFireTicks() == 0) {
            this.igniteForSeconds(8.0F);
        }

        this.hurt(this.damageSources().lightningBolt(), 5.0F);
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == 10) {
            this.moobloomEatGrassTimer = 40;
        } else {
            super.handleEntityEvent(status);
        }
    }

    @Unique
    public float hungrycows$getNeckAngle(float delta) {
        float babyNeckMultiplier = this.isBaby() ? 0.125F : 1.0F;
        float neckAngle;
        if (this.moobloomEatGrassTimer <= 0) {
            neckAngle = 0.0F;
        } else if (this.moobloomEatGrassTimer >= 4 && this.moobloomEatGrassTimer <= 36) {
            neckAngle = 1.0F;
        } else {
            neckAngle = this.moobloomEatGrassTimer < 4 ? ((float)this.moobloomEatGrassTimer - delta) / 4.0F : -((float)(this.moobloomEatGrassTimer - 40) - delta) / 4.0F;
        }
        return neckAngle * babyNeckMultiplier;
    }

    @Unique
    public float hungrycows$getHeadAngle(float delta) {
        if (this.moobloomEatGrassTimer > 4 && this.moobloomEatGrassTimer <= 36) {
            float f = ((float)(this.moobloomEatGrassTimer - 4) - delta) / 32.0F;
            return 0.62831855F + 0.21991149F * Mth.sin(f * 28.7F);
        } else {
            return this.moobloomEatGrassTimer > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectedDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(IS_MILKED_MOOBLOOM, false);
    }

    @Unique
    public boolean hungrycows$isMilked() {
        return this.entityData.get(IS_MILKED_MOOBLOOM);
    }
    @Unique
    public void hungrycows$setMilked(boolean isMilked) {
        this.entityData.set(IS_MILKED_MOOBLOOM, isMilked);
    }

    @Unique
    public boolean hungrycows$isMilkable() { return this.isAlive() && !this.hungrycows$isMilked() && !this.isBaby();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectedAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("Milked",((IHungryCows) this).hungrycows$isMilked());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectedReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        ((IHungryCows) this).hungrycows$setMilked(nbt.getBoolean("Milked"));
    }

    @Unique
    public ItemStack hungrycows$getEdibleMilk(){
        ItemStack edibleMilk = new ItemStack(Items.MILK_BUCKET);
        edibleMilk.set(DataComponents.FOOD, HungryCowsItemComponents.COW_MILK_BUCKET);
        edibleMilk.set(DataComponents.MAX_STACK_SIZE, 16);
        edibleMilk.set(DataComponents.ITEM_NAME, Component.translatable("item.hungrycows.milk_bucket.cow"));

        return edibleMilk;
    }

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER), cancellable = true)
    public void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 1) ItemStack nectarBowl) {
        if (this.hungrycows$isMilkable()) {
            nectarBowl.set(DataComponents.MAX_STACK_SIZE, 16);
            nectarBowl.set(DataComponents.FOOD, HungryCowsItemComponents.COW_MILK_BUCKET);
            this.hungrycows$setMilked(true);
        } else {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }
    }

    static {
        IS_MILKED_MOOBLOOM = SynchedEntityData.defineId(MoobloomMixin.class, EntityDataSerializers.BOOLEAN);
    }

}
