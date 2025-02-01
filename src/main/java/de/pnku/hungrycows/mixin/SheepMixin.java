package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
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

import java.util.Random;

import static de.pnku.hungrycows.HungryCows.*;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal implements Shearable,ICowEntity {

    @Shadow public abstract void setSheared(boolean sheared);

    @Shadow public abstract boolean isSheared();

    @Unique
    Sheep thisSheep = (Sheep) (Object) this;
    @Unique
    protected int hasBeenFedManuallyTimer;

    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void injectedSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(FED_TIMER_SHEEP, 0);
        builder.define(UNSHEAR_FLAG, false);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    public void injectedAiStep(CallbackInfo ci) {
            this.hungrycows$setSheepHasBeenFedManuallyTimer(!thisSheep.isBaby() ? Math.max(1, this.hungrycows$getSheepHasBeenFedManuallyTimer() - 1) : 0);
    }

    @Inject(method = "ate", at = @At("TAIL"))
    public void injectedAte(CallbackInfo ci) {
        if (thisSheep.getHealth() < thisSheep.getMaxHealth() && sheepSettings.isSheepBlockEatToHeal()) {
            thisSheep.heal(blockEatSettings.cowBlockEatHealAmount());
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void injectedAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("HasBeenFed", this.hungrycows$getSheepHasBeenFedManuallyTimer());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void injectedReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        this.hungrycows$setSheepHasBeenFedManuallyTimer(nbt.getInt("HasBeenFed"));
    }

    @Unique
    public int hungrycows$getSheepHasBeenFedManuallyTimer() {
        this.hasBeenFedManuallyTimer = thisSheep.getEntityData().get(HungryCows.FED_TIMER_SHEEP);
        return !thisSheep.isBaby() ? this.hasBeenFedManuallyTimer : 0;
    }

    @Unique
    public void hungrycows$setSheepHasBeenFedManuallyTimer(int time) {
        this.hasBeenFedManuallyTimer = time;
        thisSheep.getEntityData().set(HungryCows.FED_TIMER_SHEEP, time);
    }

    @Unique
    public void setThisSheepSheared(boolean sheared) {
        this.setSheared(false);
    }


    @Inject(method = "mobInteract", at = @At("HEAD"))
    public void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemTags.SHEEP_FOOD) && this.isSheared() && (sheepSettings.isSheepFeedToRegrowWool() || sheepSettings.isSheepFeedToHeal())) {
            boolean ate = false;
            float f = milkabilitySettings.averageFoodForMilkabilityRegainAmount();
            Random rand = new Random();
            int n = (f < 1) ?
                    (
                            (f == 0) ? 11 : rand.nextInt(10) + 1
                    ) : (
                    rand.nextInt((int) (f * 10F)) + 1
            );

            int s = ((ICowEntity) thisSheep).hungrycows$getSheepHasBeenFedManuallyTimer();
            boolean unshearFlag = this.getEntityData().get(UNSHEAR_FLAG);
            LOGGER.info("Pre-FeedToRegrowAttempt; " + (this.level().isClientSide ? "Client" : "Server") + ":-: Ready?: " + readyForShearing() + "/f; Alive?: " + this.isAlive() + "/t; Sheared?: " + this.isSheared() + "/t; Baby? " + this.isBaby() + "/f;-> " + s + "s");
            if ((n <= 10 && sheepSettings.isSheepFeedToRegrowWool() && thisSheep.isSheared() && s <= 1 && !unshearFlag) || (unshearFlag && this.level() instanceof ServerLevel)) {
                LOGGER.info((this.level().isClientSide ? "Client" : "Server") + ":-: In the if because n<=10(" + (n <= 10) + "), opt.feedWool(" + sheepSettings.isSheepFeedToRegrowWool() + "), either s<=1/server(" + (s <= 1) + "/" + (this.level() instanceof ServerLevel) + ")");
                thisSheep.setSheared(false);
                SynchedEntityData data = thisSheep.getEntityData();
                data.set(Sheep.DATA_WOOL_ID, (byte) (data.get(Sheep.DATA_WOOL_ID) & -17));
                ((ICowEntity) thisSheep).hungrycows$setSheepHasBeenFedManuallyTimer(milkabilitySettings.secondsUntilFeedabilityRegain() * 20);
                if (!unshearFlag) {
                    ate = true;
                    data.set(UNSHEAR_FLAG, true);
                } else {
                    data.set(UNSHEAR_FLAG, false);
                }
            } else { LOGGER.info((this.level().isClientSide ? "Client" : "Server") + ":-: NOT In the if because n<=10(" + (n <= 10) + "), opt.feedWool(" + sheepSettings.isSheepFeedToRegrowWool() + "), either s<=1/server(" + (s <= 1) + "/" + (this.level() instanceof ServerLevel) + ")");}
                LOGGER.info("Post-FeedToRegrowAttempt (" + ate + "); " + (this.level().isClientSide ? "Client" : "Server") + ":-: Ready?: " + readyForShearing() + "/f; Alive?: " + this.isAlive() + "/t; Sheared?: " + this.isSheared() + "/t; Baby? " + this.isBaby() + "/f; ->" + s + "s");
                if ((thisSheep.getHealth() < thisSheep.getMaxHealth()) && sheepSettings.isSheepFeedToHeal()) {
                    thisSheep.heal(2.0F);
                    ate = true;
                }
                if (ate && !(this.isFood(itemStack) && !this.level().isClientSide && this.getAge() == 0 && this.canFallInLove()) && this.level().isClientSide) { // Checks for if the stack was used AND if it won't later be used to setInLove()
                    itemStack.consume(1, player);
                    this.playSound(SoundEvents.MOOSHROOM_EAT, 0.86F, 1.75F);
                }
            }
            if (itemStack.is(Items.SHEARS)) {
                LOGGER.info((this.level().isClientSide ? "Client" : "Server") + ":-: Ready?: " + readyForShearing() + "/t; Alive?: " + this.isAlive() + "/t; Sheared?: " + this.isSheared() + "/f; Baby? " + this.isBaby() + "/f;");
            }
        }

    static {
        HungryCows.FED_TIMER_SHEEP = SynchedEntityData.defineId(SheepMixin.class, EntityDataSerializers.INT);
        HungryCows.UNSHEAR_FLAG = SynchedEntityData.defineId(SheepMixin.class, EntityDataSerializers.BOOLEAN);
    }
}
