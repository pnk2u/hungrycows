package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.item.PinkFoodComponents;
import de.pnku.hungrycows.util.ICowEntity;
import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
    @Unique
    protected int hasBeenFedManuallyTimer;

    public CowMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    public boolean hungrycows$isMooshroom(){
     return false;
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    protected void injectedRegisterGoals(CallbackInfo info) {
        this.cowEatGrassGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal((int) Math.pow(2, 4 - blockEatSettings.grassEatProbability()), this.cowEatGrassGoal);
    }

    protected void customServerAiStep() {
        if (!thisCow.getType().equals(EntityType.MOOSHROOM)) {
            this.eatGrassTimer = this.cowEatGrassGoal.getEatAnimationTick();
            this.hungrycows$setCowHasBeenFedManuallyTimer(this.getEntityData().get(FED_TIMER));

        }
        super.customServerAiStep();
    }
    public void aiStep() {
        if (!thisCow.getType().equals(EntityType.MOOSHROOM)) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
            this.hungrycows$setCowHasBeenFedManuallyTimer(!this.isBaby() ? Math.max(1, this.hungrycows$getCowHasBeenFedManuallyTimer() - 1) : 0);
        }

        super.aiStep();
    }
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HungryCows.IS_MILKED, (byte)0);
        builder.define(HungryCows.FED_TIMER, 0);
        builder.define(UNMILK_FLAG, false);
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

    // Cows should not be sheared.
    @Override
    public boolean readyForShearing() {
        return false;
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
        return ((Byte)this.entityData.get(HungryCows.IS_MILKED)) != 0;
    }
    @Unique
    public void hungrycows$setMilked(boolean isMilked) {
        byte isMilkedByte = isMilked ? (byte) 1 : (byte) 0;

        this.entityData.set(HungryCows.IS_MILKED, isMilkedByte);
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
        HungryCows.IS_MILKED = SynchedEntityData.defineId(CowMixin.class, EntityDataSerializers.BYTE);
        HungryCows.UNMILK_FLAG = SynchedEntityData.defineId(CowMixin.class, EntityDataSerializers.BOOLEAN);
        HungryCows.FED_TIMER = SynchedEntityData.defineId(CowMixin.class, EntityDataSerializers.INT);
    }

    @Unique
    public ItemStack getEdibleMilk(){
        ItemStack edibleMilk = new ItemStack(Items.MILK_BUCKET);
        edibleMilk.set(DataComponents.FOOD, PinkFoodComponents.MILK_BUCKET);
        edibleMilk.set(DataComponents.MAX_STACK_SIZE, 16);

        return edibleMilk;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemTags.COW_FOOD)) {
            boolean ate = false;
            float f = milkabilitySettings.averageFoodForMilkabilityRegainAmount();
            Random rand = new Random(); int n = (f < 1) ?
                                                        (
                                                                (f == 0) ? 11 : rand.nextInt(10) + 1
                                                        ) : (
                                                                rand.nextInt((int)(f * 10F)) + 1
                                                        );
            boolean isMooshroom = hungrycows$isMooshroom();
            int s = hungrycows$getCowHasBeenFedManuallyTimer();
            EntityDataAccessor<Boolean> unmilkFlagAccessor = isMooshroom ? UNMILK_MOOSHROOM_FLAG : UNMILK_FLAG;
            boolean unmilkFlag = getEntityData().get(unmilkFlagAccessor);
            boolean isMilked = hungrycows$isMilked();
            boolean isClientSide = this.level().isClientSide;
            boolean isServerLevel = this.level() instanceof ServerLevel;
            int feedabilityRegainTime = milkabilitySettings.secondsUntilFeedabilityRegain() * 20;

            if ((n <= 10 && isMilked && s <= 1 && isClientSide) || (unmilkFlag && isServerLevel)) {
                hungrycows$setMilked(false);
                hungrycows$setCowHasBeenFedManuallyTimer(feedabilityRegainTime);
                if (!unmilkFlag) {
                    ate = true;
                    getEntityData().set(unmilkFlagAccessor, true);
                } else {
                    getEntityData().set(unmilkFlagAccessor, false);
                }
            }

            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
                ate = true;
            }
            if (ate && !(this.isFood(itemStack) && !this.level().isClientSide && this.getAge() == 0 && this.canFallInLove())) { // Checks for if the stack was used AND if it won't later be used to setInLove()
                itemStack.consume(1, player);
                this.playSound(SoundEvents.MOOSHROOM_EAT, 1.15F, 0.85F);
            }
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