package de.pnku.hungrycows.mixin.entity;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.HungryCowsEntityInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.HungryCows.*;
import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.*;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal implements Shearable, HungryCowsEntityInterface {

    @Shadow public abstract void setSheared(boolean sheared);

    @Shadow public abstract boolean isSheared();

    @Shadow protected abstract void registerGoals();

    @Unique
    Sheep thisSheep = (Sheep) (Object) this;
    @Unique
    protected int hasBeenFedManuallyTimer;

    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    public void injectedRegisterGoals(CallbackInfo ci){
        this.goalSelector.removeAllGoals(goal -> goal instanceof TemptGoal);
        TemptGoal sheepFeedTemptGoal = new TemptGoal(this, 1.1F, Ingredient.of(getFeedableItemsFromConfig(EntityType.SHEEP)), false);
        this.goalSelector.addGoal(3, sheepFeedTemptGoal);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void injectedSynchedData(CallbackInfo ci) {
        this.entityData.define(FED_TIMER_SHEEP, 0);
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


    @Inject(method = "mobInteract", at = @At("HEAD"))
    public void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if ((checkFeedability(itemStack, thisSheep) && this.isSheared() && sheepSettings.isSheepFeedToRegrowWool()) || sheepSettings.isSheepFeedToHeal()) {
            int s = ((HungryCowsEntityInterface) thisSheep).hungrycows$getSheepHasBeenFedManuallyTimer();
            if ((sheepSettings.isSheepFeedToRegrowWool() && thisSheep.isSheared() && s <= 1)) {
                thisSheep.setSheared(false);
                SynchedEntityData data = thisSheep.getEntityData();
                data.set(Sheep.DATA_WOOL_ID, (byte) (data.get(Sheep.DATA_WOOL_ID) & -17));
                ((HungryCowsEntityInterface) thisSheep).hungrycows$setSheepHasBeenFedManuallyTimer(milkabilitySettings.secondsUntilFeedabilityRegain() * 20);
                itemStack.shrink(player.getAbilities().instabuild ? 0 : 1);
                level().playSound(player, this, SoundEvents.GOAT_EAT, SoundSource.NEUTRAL, 0.95F, 0.85F);
            }
            if ((thisSheep.getHealth() < thisSheep.getMaxHealth()) && sheepSettings.isSheepFeedToHeal()) {
                thisSheep.heal(2.0F);
                itemStack.shrink(player.getAbilities().instabuild ? 0 : 1);
                level().playSound(player, this, SoundEvents.GOAT_EAT, SoundSource.NEUTRAL, 0.95F, 0.85F);
            }
        }
    }

    static {
        HungryCows.FED_TIMER_SHEEP = SynchedEntityData.defineId(SheepMixin.class, EntityDataSerializers.INT);
    }
}
