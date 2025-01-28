package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.config.HungryCowsOwoConfig;
import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static de.pnku.hungrycows.HungryCows.*;

@Mixin(Cow.class)
public abstract class CowMixin extends Animal implements Shearable, ICowEntity {
    @Unique
    private EatBlockGoal cowEatGrassGoal;
    @Unique
    private int eatGrassTimer;
    @Unique
    Cow thisCow = (Cow) (Object) this;

    public CowMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }
    @Inject(method = "registerGoals", at = @At("HEAD"))
    protected void injectedRegisterGoals(CallbackInfo info) {
        this.cowEatGrassGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal((int) Math.pow(2, 4 - blockEatSettings.grassEatProbability()), this.cowEatGrassGoal);
    }

    protected void customServerAiStep() {
        if (!thisCow.getType().equals(EntityType.MOOSHROOM)) {
            this.eatGrassTimer = this.cowEatGrassGoal.getEatAnimationTick();
        }
        super.customServerAiStep();
    }
    public void aiStep() {
        if (this.level().isClientSide && !thisCow.getType().equals(EntityType.MOOSHROOM)) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
        }

        super.aiStep();
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_MILKED, (byte)0);
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
        if (this.eatGrassTimer <= 0) {
            return 0.0F;
        } else if (this.eatGrassTimer >= 4 && this.eatGrassTimer <= 36) {
            return 1.0F;
        } else {
            return this.eatGrassTimer < 4 ? ((float)this.eatGrassTimer - delta) / 4.0F : -((float)(this.eatGrassTimer - 40) - delta) / 4.0F;
        }
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
    }
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.hungrycows$setMilked(nbt.getBoolean("Milked"));
    }
    @Unique
    public boolean hungrycows$isMilked() {
        return ((Byte)this.entityData.get(IS_MILKED)) != 0;
    }
    @Unique
    public void hungrycows$setMilked(boolean isMilked) {
        byte isMilkedByte = isMilked ? (byte) 1 : (byte) 0;

        this.entityData.set(IS_MILKED, isMilkedByte);
    }

    public void ate() {
        super.ate();
        this.hungrycows$setMilked(false);
        if (this.isBaby()) {
            this.ageUp(blockEatSettings.cowBlockEatGrowthAmount());
        }
        if (this.getHealth() < this.getMaxHealth()){
            this.heal(blockEatSettings.cowBlockEatHealAmount());
        }
    }

    static {
        IS_MILKED = SynchedEntityData.defineId(CowMixin.class, EntityDataSerializers.BYTE);
    }

    @Unique
    public ItemStack getEdibleMilk(){
        ItemStack edibleMilk = new ItemStack(Items.MILK_BUCKET);

        return edibleMilk;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemTags.COW_FOOD) && this.hungrycows$isMilked() && !this.getType().equals(EntityType.MOOSHROOM)) {
            Random rand = new Random(); int n = rand.nextInt((int)(milkabilitySettings.averageFoodForMilkabilityRegainAmount() * 10F)) + 1;
            if (n <= 10) {
                ((ICowEntity) this).hungrycows$setMilked(false);
            }
            itemStack.consume(1, player);
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
            }
            this.playSound(SoundEvents.MOOSHROOM_EAT, 1.15F, 0.85F);
        }
        if (itemStack.is(Items.BUCKET)){
            if (!this.isBaby() && this.hungrycows$isMilkable()) {
                this.hungrycows$setMilked(true);
                player.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 0.237F, 3.17F);
                player.playSound(SoundEvents.COW_MILK, 1.317F, 1.237F);
                ItemStack itemStackMilk = ItemUtils.createFilledResult(itemStack, player, getEdibleMilk());
                player.setItemInHand(hand, itemStackMilk);

                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            }
            else cir.setReturnValue(InteractionResult.PASS);
        }
    }
}