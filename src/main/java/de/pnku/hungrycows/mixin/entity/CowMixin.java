package de.pnku.hungrycows.mixin.entity;

import de.pnku.hungrycows.util.IHungryCows;
import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.HungryCows.*;
import static de.pnku.hungrycows.config.HungryCowsConfigHelper.*;

@Mixin(Cow.class)
public abstract class CowMixin extends Animal implements Shearable, IHungryCows {
    @Unique
    private EatBlockGoal cowEatGrassGoal;
    @Unique
    private int eatGrassTimer;
    @Unique
    Cow thisCow = (Cow) (Object) this;
    @Unique
    protected int hasBeenFedManuallyTimer;

    public CowMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Unique public boolean hungrycows$isMooshroom(){
     return false;
    }
    @Unique public boolean hungrycows$isCow(){
        return true;
    }
    @Unique public String hungrycows$getName(){
        return "cow";
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    protected void injectedRegisterGoals(CallbackInfo info) {
        this.goalSelector.removeAllGoals(goal -> goal instanceof TemptGoal);
        TemptGoal cowFeedTemptGoal = new TemptGoal(this, 1.25F, Ingredient.of(getFeedableItemsFromConfig(EntityType.COW)), false);
        this.cowEatGrassGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(3, cowFeedTemptGoal);
        this.goalSelector.addGoal((int) Math.pow(2, 4 - blockEatSettings.grassEatProbability()), this.cowEatGrassGoal);
    }

    protected void customServerAiStep() {
        if (thisCow.getType().equals(EntityType.COW)) {
            this.eatGrassTimer = this.cowEatGrassGoal.getEatAnimationTick();
            this.hungrycows$setCowHasBeenFedManuallyTimer(this.getEntityData().get(FED_TIMER));

        }
        super.customServerAiStep();
    }
    public void aiStep() {
        if (thisCow.getType().equals(EntityType.COW)) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
            this.hungrycows$setCowHasBeenFedManuallyTimer(!this.isBaby() ? Math.max(1, this.hungrycows$getCowHasBeenFedManuallyTimer() - 1) : 0);
        }

        super.aiStep();
    }

    @Override public void thunderHit(ServerLevel world, LightningBolt bolt){
        this.setRemainingFireTicks(this.getRemainingFireTicks() + 1);
        if (this.getRemainingFireTicks() == 0) {
            this.igniteForSeconds(8.0F);
        }

        this.hurt(this.damageSources().lightningBolt(), 5.0F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_MILKED, false);
        this.entityData.define(HungryCows.FED_TIMER, 0);
    }

    public void handleEntityEvent(byte status) {
        if (status == 10) {
            this.eatGrassTimer = 40;
        } else {
            super.handleEntityEvent(status);
        }
    }

    @Unique
    public float hungrycows$getNeckAngle(float delta) {
        float babyNeckMultiplier = this.isBaby() ? 0.125F : 1.0F;
        float neckAngle;
        if (this.eatGrassTimer <= 0) {
            neckAngle = 0.0F;
        } else if (this.eatGrassTimer >= 4 && this.eatGrassTimer <= 36) {
            neckAngle = 1.0F;
        } else {
            neckAngle = this.eatGrassTimer < 4 ? ((float)this.eatGrassTimer - delta) / 4.0F : -((float)(this.eatGrassTimer - 40) - delta) / 4.0F;
        }
        return neckAngle * babyNeckMultiplier;
    }

    @Unique
    public float hungrycows$getHeadAngle(float delta) {
        if (this.eatGrassTimer > 4 && this.eatGrassTimer <= 36) {
            float f = ((float)(this.eatGrassTimer - 4) - delta) / 32.0F;
            return 0.62831855F + 0.21991149F * Mth.sin(f * 28.7F);
        } else {
            return this.eatGrassTimer > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
        }
    }

    @Unique
    public boolean hungrycows$isMilkable() { return this.isAlive() && !this.hungrycows$isMilked() && !this.isBaby();
    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Milked", this.hungrycows$isMilked());
        nbt.putInt("HasBeenFed", this.hungrycows$getCowHasBeenFedManuallyTimer());
    }
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.hungrycows$setMilked(nbt.getBoolean("Milked"));
        this.hungrycows$setCowHasBeenFedManuallyTimer(nbt.getInt("HasBeenFed"));
    }

    @Unique
    public int hungrycows$getCowHasBeenFedManuallyTimer() {
        this.hasBeenFedManuallyTimer = this.getEntityData().get(HungryCows.FED_TIMER);
        return !this.isBaby() ? this.hasBeenFedManuallyTimer : 0;
    }

    @Unique
    public void hungrycows$setCowHasBeenFedManuallyTimer(int time) {
        this.hasBeenFedManuallyTimer = time;
        this.getEntityData().set(HungryCows.FED_TIMER, time);
    }

    @Unique
    public boolean hungrycows$isMilked() {
        return this.entityData.get(HungryCows.IS_MILKED);
    }
    @Unique
    public void hungrycows$setMilked(boolean isMilked) {
        this.entityData.set(HungryCows.IS_MILKED, isMilked);
    }

    public void ate() {
        super.ate();
        if (this.isBaby()) {
            this.ageUp(blockEatSettings.cowBlockEatGrowthAmount());
        } else if (this.hungrycows$isMilked()) {
            this.hungrycows$setMilked(false);
            if (!this.level().isClientSide()) {
                Vec3 udderPos = relParticlePos(this.position(), this.getYRot(), "cow_udder");
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, udderPos.x, udderPos.y, udderPos.z, 6, 0.2F, 0.1F, 0.2F, 0.25F);
            }
        }

        int healthDiff = (int) thisCow.getMaxHealth() - (int) thisCow.getHealth();
        if (healthDiff > 0){
            int i = blockEatSettings.cowBlockEatHealAmount();
            thisCow.heal(i);
            if (!this.level().isClientSide()) {
                Vec3 bodyPos = relParticlePos(this.position(), this.getYRot(), "cow_body");
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, bodyPos.x, bodyPos.y, bodyPos.z, i, 0.375F, 0.625F, 0.375F, 0.2F);
            }
        }
    }

    static {
        HungryCows.IS_MILKED = SynchedEntityData.defineId(CowMixin.class, EntityDataSerializers.BOOLEAN);
        HungryCows.FED_TIMER = SynchedEntityData.defineId(CowMixin.class, EntityDataSerializers.INT);
    }

    @Unique
    public ItemStack hungrycows$getEdibleMilk(){
        ItemStack edibleMilk = new ItemStack(Items.MILK_BUCKET);

        return edibleMilk;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        Animal thisCowLike = hungrycows$isMooshroom() ? thisCow : this;
        if (checkFeedability(itemStack, thisCowLike)) {
            boolean isMooshroom = hungrycows$isMooshroom();
            int s = hungrycows$getCowHasBeenFedManuallyTimer();
            float eatSoundPitch = isMooshroom ? 1.35F : 0.65F;
            boolean isMilked = hungrycows$isMilked();
            int feedabilityRegainTime = milkabilitySettings.secondsUntilFeedabilityRegain() * 20;

            if ((isMilked && s <= 1)) {
                hungrycows$setMilked(false);
                hungrycows$setCowHasBeenFedManuallyTimer(feedabilityRegainTime);
                level().playSound(player, this, SoundEvents.MOOSHROOM_EAT, SoundSource.NEUTRAL,0.95F, eatSoundPitch*0.8F);
                itemStack.shrink(player.getAbilities().instabuild ? 0 : 1);
                if (!this.level().isClientSide()) {
                    Vec3 udderPos = relParticlePos(this.position(), this.getYRot(), "cow_udder");
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, udderPos.x, udderPos.y, udderPos.z, 6, 0.2F, 0.1F, 0.2F, 0.25F);
                }
                    cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
                level().playSound(player, this.getOnPos(), SoundEvents.MOOSHROOM_EAT, SoundSource.NEUTRAL,0.95F, eatSoundPitch*1.1F);
                itemStack.shrink(player.getAbilities().instabuild ? 0 : 1);
                if (!this.level().isClientSide()) {
                    Vec3 bodyPos = relParticlePos(this.position(), this.getYRot(), "cow_body");
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, bodyPos.x, bodyPos.y, bodyPos.z, 2, 0.375F, 0.625F, 0.375F, 0.2F);
                }
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
                player.playSound(SoundEvents.COW_MILK, 1.317F, 1.237F);
                if (!this.level().isClientSide()) {
                    Vec3 heartPos = relParticlePos(this.position(), this.getYRot(), "udder_heart");
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.HEART, heartPos.x, heartPos.y, heartPos.z, 1, 0.05F, 0.05F, 0.05F, 0.05F);
                }
                ItemStack itemStackMilk = ItemUtils.createFilledResult(itemStack, player, hungrycows$getEdibleMilk());
                player.setItemInHand(hand, itemStackMilk);

                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            }
            else cir.setReturnValue(InteractionResult.PASS);
        }
    }
}