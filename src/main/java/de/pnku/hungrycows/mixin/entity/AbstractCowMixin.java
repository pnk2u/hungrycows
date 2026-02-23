package de.pnku.hungrycows.mixin.entity;

import de.pnku.hungrycows.item.HungryCowsItemComponents;
import de.pnku.hungrycows.sound.HungryCowsSoundEvents;
import de.pnku.hungrycows.util.IHungryCows;
import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.HungryCows.*;
import static de.pnku.hungrycows.config.HungryCowsConfigHelper.*;

@Mixin(AbstractCow.class)
public abstract class AbstractCowMixin extends Animal implements Shearable, IHungryCows {
    @Unique
    private EatBlockGoal cowEatGrassGoal;
    @Unique
    private int eatGrassTimer;
    @Unique
    AbstractCow thisAbstractCow = (AbstractCow) (Object) this;
    @Unique
    protected int hasBeenFedManuallyTimer;

    public AbstractCowMixin(EntityType<? extends AbstractCow> entityType, Level level) {
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
        TemptGoal cowFeedTemptGoal = new TemptGoal(this, 1.25F, itemStack -> checkFeedability(itemStack, this), false);
        this.cowEatGrassGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(3, cowFeedTemptGoal);
        this.goalSelector.addGoal((int) Math.pow(2, 4 - blockEatSettings.grassEatProbability()), this.cowEatGrassGoal);
    }

    protected void customServerAiStep(ServerLevel level) {
        if (!thisAbstractCow.getType().equals(EntityType.MOOSHROOM)) {
            this.eatGrassTimer = this.cowEatGrassGoal.getEatAnimationTick();
            this.hungrycows$setCowHasBeenFedManuallyTimer(this.getEntityData().get(FED_TIMER));

        }
        super.customServerAiStep(level);
    }
    public void aiStep() {
        if (!thisAbstractCow.getType().equals(EntityType.MOOSHROOM)) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
            this.hungrycows$setCowHasBeenFedManuallyTimer(!this.isBaby() ? Math.max(1, this.hungrycows$getCowHasBeenFedManuallyTimer() - 1) : 0);
        }

        super.aiStep();
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HungryCows.IS_MILKED, false);
        builder.define(HungryCows.FED_TIMER, 0);
    }

    public void handleEntityEvent(byte status) {
        if (status == 10) {
            this.eatGrassTimer = 40;
        } else {
            super.handleEntityEvent(status);
        }
    }

    @Unique
    public boolean hungrycows$isEating() {
        return this.eatGrassTimer > 0;
    }

    @Unique
    public float hungrycows$getNeckAngle(float delta) {
        float babyNeckMultiplier = this.isBaby() ? 0.25F : 1.0F; // 0.25F being double from 0.125F from 1.21.1 as in 1.21.4 we also use the Animal class' getAgeScale (0.5F if isBaby())
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

    // Cows should not be sheared.
    @Override
    public boolean readyForShearing() {
        return false;
    }

    @Unique
    public float hungrycows$getHeadAngle(float delta) {
        if (this.eatGrassTimer > 4 && this.eatGrassTimer <= 36) {
            float f = ((float)(this.eatGrassTimer - 4) - delta) / 32.0F;
            return 0.62831855F + 0.21991149F * (float) Math.sin(f * 28.7F);
        } else {
            return this.eatGrassTimer > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
        }
    }

    @Unique
    public boolean hungrycows$isMilkable() { return this.isAlive() && !this.hungrycows$isMilked() && !this.isBaby();
    }

    @Unique
    public ItemStack hungrycows$getSuspiciousFlowerStack() {return ItemStack.EMPTY;}

    @Unique
    public void hungrycows$setSuspiciousFlowerStack(ItemStack stack) {}

    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Milked", this.hungrycows$isMilked());
        nbt.putInt("HasBeenFed", this.hungrycows$getCowHasBeenFedManuallyTimer());
    }
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        this.hungrycows$setMilked(nbt.getBooleanOr("Milked", false));
        this.hungrycows$setCowHasBeenFedManuallyTimer(nbt.getIntOr("HasBeenFed", 0));
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
            if (!this.level.isClientSide()) {
                Vec3 udderPos = relParticlePos(this.position, this.getYRot(), "cow_udder");
                ((ServerLevel) this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, udderPos.x, udderPos.y, udderPos.z, 6, 0.2F, 0.1F, 0.2F, 0.25F);
            }
        }

        int healthDiff = (int) thisAbstractCow.getMaxHealth() - (int) thisAbstractCow.getHealth();
        if (healthDiff > 0){
            int i = blockEatSettings.cowBlockEatHealAmount();
            thisAbstractCow.heal(i);
            if (!this.level.isClientSide()) {
                Vec3 bodyPos = relParticlePos(this.position, this.getYRot(), "cow_body");
                ((ServerLevel) this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, bodyPos.x, bodyPos.y, bodyPos.z, i, 0.375F, 0.625F, 0.375F, 0.2F);
            }
        }
    }

    static {
        HungryCows.IS_MILKED = SynchedEntityData.defineId(AbstractCowMixin.class, EntityDataSerializers.BOOLEAN);
        HungryCows.FED_TIMER = SynchedEntityData.defineId(AbstractCowMixin.class, EntityDataSerializers.INT);
    }

    @Unique
    public ItemStack hungrycows$getEdibleMilk(){
        ItemStack edibleMilk = new ItemStack(Items.MILK_BUCKET);
        edibleMilk.set(DataComponents.FOOD, hungrycows$isCow() ? HungryCowsItemComponents.COW_MILK_BUCKET : HungryCowsItemComponents.MOOSHROOM_MILK_BUCKET);
        edibleMilk.set(DataComponents.MAX_STACK_SIZE, 16);
        edibleMilk.set(DataComponents.ITEM_NAME, Component.translatable("item.hungrycows.milk_bucket." + hungrycows$getName()));

        return edibleMilk;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        Animal thisCowLike = hungrycows$isMooshroom() ? thisAbstractCow : this;
        boolean isMooshroom = hungrycows$isMooshroom();
        if (checkFeedability(itemStack, thisCowLike)) {
            int s = hungrycows$getCowHasBeenFedManuallyTimer();
            float eatSoundPitch = isMooshroom ? 1.35F : 0.65F;
            boolean isMilked = hungrycows$isMilked();
            int feedabilityRegainTime = milkabilitySettings.secondsUntilFeedabilityRegain() * 20;

            if ((isMilked && s <= 1)) {
                hungrycows$setMilked(false);
                hungrycows$setCowHasBeenFedManuallyTimer(feedabilityRegainTime);
                this.level.playSound(player, this, isMooshroom ? SoundEvents.MOOSHROOM_EAT : HungryCowsSoundEvents.COW_EAT, SoundSource.NEUTRAL,0.95F, eatSoundPitch*0.8F);
                itemStack.consume(1, player);
                if (!this.level.isClientSide()) {
                    Vec3 udderPos = relParticlePos(this.position, this.getYRot(), "cow_udder");
                    ((ServerLevel) this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, udderPos.x, udderPos.y, udderPos.z, 6, 0.2F, 0.1F, 0.2F, 0.25F);
                }
                    cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }

            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
                this.level.playSound(player, this.getOnPos(), isMooshroom ? SoundEvents.MOOSHROOM_EAT : HungryCowsSoundEvents.COW_EAT, SoundSource.NEUTRAL,0.95F, eatSoundPitch*1.1F);
                itemStack.consume(1, player);
                if (!this.level.isClientSide()) {
                    Vec3 bodyPos = relParticlePos(this.position, this.getYRot(), "cow_body");
                    ((ServerLevel) this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, bodyPos.x, bodyPos.y, bodyPos.z, 2, 0.375F, 0.625F, 0.375F, 0.2F);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
            if (this.level.isClientSide()) {
                cir.setReturnValue(InteractionResult.CONSUME);
                return;
            }
        }
        if (itemStack.is(Items.BUCKET)){
            if (!this.isBaby() && this.hungrycows$isMilkable()) {
                this.hungrycows$setMilked(true);
                player.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 0.237F, 3.17F);
                this.playSound(!isMooshroom ? SoundEvents.COW_MILK : HungryCowsSoundEvents.MOOSHROOM_MILK, 1.317F, 1.237F);
                if (!this.level.isClientSide()) {
                    Vec3 heartPos = relParticlePos(this.position, this.getYRot(), "udder_heart");
                    ((ServerLevel) this.level).sendParticles(ParticleTypes.HEART, heartPos.x, heartPos.y, heartPos.z, 1, 0.05F, 0.05F, 0.05F, 0.05F);
                }
                ItemStack itemStackMilk = ItemUtils.createFilledResult(itemStack, player, hungrycows$getEdibleMilk());
                player.setItemInHand(hand, itemStackMilk);

                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            }
            else cir.setReturnValue(InteractionResult.PASS);
        }
    }
}