package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.entity.ai.EatMyceliumBlockGoal;
import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
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

import static de.pnku.hungrycows.HungryCows.*;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends Cow implements Shearable, VariantHolder<MushroomCow.Variant>, ICowEntity {
    public MushroomCowMixin(EntityType<? extends MushroomCow> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private EatMyceliumBlockGoal mushroomCowEatMyceliumGoal;
    @Unique
    private int eatMyceliumTimer;
    @Unique
    MushroomCow mushroomCow = (MushroomCow) (Object) this;
    @Unique
    protected int hasBeenFedManuallyTimer;

    @Unique
    public boolean hungrycows$isMooshroom(){
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, itemStack -> itemStack.is(ItemTags.COW_FOOD), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.mushroomCowEatMyceliumGoal = new EatMyceliumBlockGoal(this);
        this.goalSelector.addGoal((int) Math.pow(2, 4 - blockEatSettings.grassEatProbability()), this.mushroomCowEatMyceliumGoal);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        this.eatMyceliumTimer = this.mushroomCowEatMyceliumGoal.getEatAnimationTick();
        this.hungrycows$setCowHasBeenFedManuallyTimer(this.getEntityData().get(FED_TIMER));
        super.customServerAiStep(level);
    }

    @Override
    public void aiStep() {
        this.eatMyceliumTimer = Math.max(0, this.eatMyceliumTimer - 1);
        this.hungrycows$setCowHasBeenFedManuallyTimer(!this.isBaby() ? Math.max(1, this.hungrycows$getCowHasBeenFedManuallyTimer() - 1) : 0);

        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte status) {
        if (status == 10) {
            this.eatMyceliumTimer = 40;
        } else {
            super.handleEntityEvent(status);
        }
    }

    @Unique
    public float hungrycows$getNeckAngle(float delta) {
        if (this.eatMyceliumTimer <= 0) {
            return 0.0F;
        } else if (this.eatMyceliumTimer >= 4 && this.eatMyceliumTimer <= 36) {
            return 1.0F;
        } else {
            return this.eatMyceliumTimer < 4 ? ((float)this.eatMyceliumTimer - delta) / 4.0F : -((float)(this.eatMyceliumTimer - 40) - delta) / 4.0F;
        }
    }

    @Unique
    public float hungrycows$getHeadAngle(float delta) {
        if (this.eatMyceliumTimer > 4 && this.eatMyceliumTimer <= 36) {
            float f = ((float)(this.eatMyceliumTimer - 4) - delta) / 32.0F;
            return 0.62831855F + 0.21991149F * Mth.sin(f * 28.7F);
        } else {
            return this.eatMyceliumTimer > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemTags.SMALL_FLOWERS) && mushroomCow.getVariant().equals(MushroomCow.MushroomType.BROWN) && mushroomCow.stewEffects == null) {
            ((ICowEntity) mushroomCow).hungrycows$setMilked(false);
            this.playSound(SoundEvents.MOOSHROOM_EAT, 1.2F, 1.05F);
        }
        if (itemStack.is(Items.BOWL)) {
            if (((ICowEntity) mushroomCow).hungrycows$isMilkable()) {
                boolean bl = false;
                ItemStack itemStack2;
                if (mushroomCow.stewEffects != null) {
                    bl = true;
                    itemStack2 = new ItemStack(Items.SUSPICIOUS_STEW);
                    itemStack2.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, mushroomCow.stewEffects);
                    mushroomCow.stewEffects = null;
                } else {
                    itemStack2 = new ItemStack(Items.MUSHROOM_STEW);
                }

                ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
                player.setItemInHand(hand, itemStack3);

                ((ICowEntity) mushroomCow).hungrycows$setMilked(true);

                SoundEvent soundEvent;
                if (bl) {
                    soundEvent = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
                } else {
                    soundEvent = SoundEvents.MOOSHROOM_MILK;
                }

                this.playSound(soundEvent, 1.0F, 1.0F);
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                cir.setReturnValue(InteractionResult.PASS);
            }
            return;
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectedDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
            builder.define(IS_MILKED_MOOSHROOM, (byte)0);
            builder.define(UNMILK_MOOSHROOM_FLAG, false);
    }

    @Unique
    public boolean hungrycows$isMilked() {
        return ((Byte)this.entityData.get(HungryCows.IS_MILKED_MOOSHROOM)) != 0;
    }
    @Unique
    public void hungrycows$setMilked(boolean isMilked) {
        byte isMilkedByte = isMilked ? (byte) 1 : (byte) 0;

        this.entityData.set(HungryCows.IS_MILKED_MOOSHROOM, isMilkedByte);
    }

    @Unique
    public boolean hungrycows$isMilkable() { return this.isAlive() && !this.hungrycows$isMilked() && !this.isBaby();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectedAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("Milked",((ICowEntity) this).hungrycows$isMilked());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectedReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        ((ICowEntity) this).hungrycows$setMilked(nbt.getBoolean("Milked"));
    }

    @Override
    public MushroomCow getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        MushroomCow mushroomCow = (MushroomCow)EntityType.MOOSHROOM.create(level, EntitySpawnReason.BREEDING);
        if (mushroomCow != null) {
            mushroomCow.setVariant(mushroomCow.getOffspringVariant((MushroomCow)otherParent));
        }

        return mushroomCow;
    }

    static {
        IS_MILKED_MOOSHROOM = SynchedEntityData.defineId(MushroomCowMixin.class, EntityDataSerializers.BYTE);
        UNMILK_MOOSHROOM_FLAG = SynchedEntityData.defineId(MushroomCowMixin.class, EntityDataSerializers.BOOLEAN);
    }

}
