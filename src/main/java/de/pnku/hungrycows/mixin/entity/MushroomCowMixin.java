package de.pnku.hungrycows.mixin.entity;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.item.HungryCowsItemComponents;
import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.animal.AbstractCow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.HungryCows.*;
import static de.pnku.hungrycows.config.HungryCowsConfigHelper.*;
import static de.pnku.hungrycows.util.HungryCowsDebug.log;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends AbstractCow implements Shearable, IHungryCows {
    public MushroomCowMixin(EntityType<? extends MushroomCow> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public MushroomCow getBreedOffspring(ServerLevel level, AgeableMob otherParent){
        return null;
    }

    @Shadow
    @Nullable
    public SuspiciousStewEffects stewEffects;
    @Unique
    private EatBlockGoal mushroomCowEatMyceliumGoal;
    @Unique
    private int eatMyceliumTimer;
    @Unique
    MushroomCow thisMushroomCow = (MushroomCow) (Object) this;
    @Unique
    protected int hasBeenFedManuallyTimer;

    @Unique public boolean hungrycows$isMooshroom(){
        return true;
    }
    @Unique public boolean hungrycows$isCow(){
        return false;
    }
    @Unique public String hungrycows$getName(){
        return "mooshroom";
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.removeAllGoals(goal -> goal instanceof TemptGoal || goal instanceof EatBlockGoal);
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, itemStack -> checkFeedability(itemStack, this), false));
        this.mushroomCowEatMyceliumGoal = new EatBlockGoal(this);
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
    public boolean hungrycows$isEating() {
        return this.eatMyceliumTimer > 0;
    }

    @Unique
    public float hungrycows$getNeckAngle(float delta) {
        float babyNeckMultiplier = this.isBaby() ? 0.125F : 1.0F;
        float neckAngle;
        if (this.eatMyceliumTimer <= 0) {
            neckAngle = 0.0F;
        } else if (this.eatMyceliumTimer >= 4 && this.eatMyceliumTimer <= 36) {
            neckAngle = 1.0F;
        } else {
            neckAngle = this.eatMyceliumTimer < 4 ? ((float)this.eatMyceliumTimer - delta) / 4.0F : -((float)(this.eatMyceliumTimer - 40) - delta) / 4.0F;
        }
        return neckAngle * babyNeckMultiplier;
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

    @Unique
    public ItemStack hungrycows$getEdibleMilk(){
        ItemStack edibleMilk = new ItemStack(Items.MILK_BUCKET);
        edibleMilk.set(DataComponents.FOOD, HungryCowsItemComponents.MOOSHROOM_MILK_BUCKET);
        edibleMilk.set(DataComponents.MAX_STACK_SIZE, 16);
        edibleMilk.set(DataComponents.ITEM_NAME, Component.translatable("item.hungrycows.milk_bucket.mooshroom"));

        return edibleMilk;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);

        log(this.level, "MOOSHROOM_INTERACT", itemStack.getItem().getDescriptionId()); log(this.level, "MOOSHROOM_VARIANT", thisMushroomCow.getVariant().name());

        ItemStack syncedFlower = ((IHungryCows) thisMushroomCow).hungrycows$getSuspiciousFlowerStack();
        boolean hasSyncedSuspicious = !syncedFlower.isEmpty();

        log(this.level, "MOOSHROOM_SYNCED_FLOWER", hasSyncedSuspicious ? syncedFlower : "Empty"); log(this.level, "MOOSHROOM_STEW_EFFECTS", thisMushroomCow.stewEffects != null ? thisMushroomCow.stewEffects : "None");

        if (this.level.isClientSide() && stewEffects == null && hasSyncedSuspicious) {
            log(this.level, "MOOSHROOM_CLIENT_SYNC_FIX");
            thisMushroomCow.getEffectsFromItemStack(syncedFlower).ifPresent(effects -> thisMushroomCow.stewEffects = effects);
        }

        if (!this.level.isClientSide()
                && itemStack.is(ItemTags.SMALL_FLOWERS)
                && thisMushroomCow.getVariant() == MushroomCow.Variant.BROWN
                && (!hasSyncedSuspicious || ((IHungryCows) thisMushroomCow).hungrycows$isMilked())) {

            log(this.level, "MOOSHROOM_FEED_FLOWER");
            ((IHungryCows) thisMushroomCow).hungrycows$setMilked(false);

            if (!hasSyncedSuspicious) {
                ItemStack one = itemStack.copyWithCount(1);
                ((IHungryCows) thisMushroomCow).hungrycows$setSuspiciousFlowerStack(one);
            }

            this.playSound(SoundEvents.MOOSHROOM_EAT, 1.2F, 1.05F);
        }

        if (!itemStack.is(Items.BOWL)) {
            log(this.level, "MOOSHROOM_NO_BOWL");
            return;
        }

        if (!((IHungryCows) thisMushroomCow).hungrycows$isMilkable()) {
            log(this.level, "MOOSHROOM_NOT_MILKABLE");
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        log(this.level, "MOOSHROOM_BOWL_SERVER", hasSyncedSuspicious);

        ItemStack result;
        if (hasSyncedSuspicious) {
            result = new ItemStack(Items.SUSPICIOUS_STEW);

            thisMushroomCow.getEffectsFromItemStack(syncedFlower).ifPresent(effects ->
                    result.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effects)
            );

            thisMushroomCow.stewEffects = null;
            ((IHungryCows) thisMushroomCow).hungrycows$setSuspiciousFlowerStack(ItemStack.EMPTY);
        } else {
            result = new ItemStack(Items.MUSHROOM_STEW);
        }

        if (this.level.isClientSide()) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        ItemStack filled = ItemUtils.createFilledResult(itemStack, player, result, false);
        player.setItemInHand(hand, filled);

        ((IHungryCows) thisMushroomCow).hungrycows$setMilked(true);

        SoundEvent soundEvent = hasSyncedSuspicious ? SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY : SoundEvents.MOOSHROOM_MILK;
        this.playSound(soundEvent, 1.0F, 1.0F);

        cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectedDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
            builder.define(IS_MILKED_MOOSHROOM, false);
            builder.define(SUSPICIOUS_FLOWER_STACK, ItemStack.EMPTY);
    }

    @Unique
    public boolean hungrycows$isMilked() {
        return this.entityData.get(HungryCows.IS_MILKED_MOOSHROOM);
    }
    @Unique
    public void hungrycows$setMilked(boolean isMilked) {
        this.entityData.set(HungryCows.IS_MILKED_MOOSHROOM, isMilked);
    }

    @Unique
    public boolean hungrycows$isMilkable() { return this.isAlive() && !this.hungrycows$isMilked() && !this.isBaby();
    }

    @Unique
    public ItemStack hungrycows$getSuspiciousFlowerStack() {
        return this.entityData.get(SUSPICIOUS_FLOWER_STACK);
    }

    @Unique
    public void hungrycows$setSuspiciousFlowerStack(ItemStack stack) {
        this.entityData.set(SUSPICIOUS_FLOWER_STACK, stack);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectedAddAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("Milked",((IHungryCows) this).hungrycows$isMilked());
        nbt.put("SuspiciousFlowerStack", this.hungrycows$getSuspiciousFlowerStack().save(this.registryAccess()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectedReadAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        ((IHungryCows) this).hungrycows$setMilked(nbt.getBooleanOr("Milked", false));
        ItemStack.parse(this.registryAccess(), nbt.getCompoundOrEmpty("SuspiciousFlowerStack")).ifPresent(this::hungrycows$setSuspiciousFlowerStack);
    }

    static {
        IS_MILKED_MOOSHROOM = SynchedEntityData.defineId(MushroomCowMixin.class, EntityDataSerializers.BOOLEAN);
        SUSPICIOUS_FLOWER_STACK = SynchedEntityData.defineId(MushroomCowMixin.class, EntityDataSerializers.ITEM_STACK);
    }

}
