package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.entity;

import com.llamalad7.mixinextras.sugar.Local;
import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.item.HungryCowsItemComponents;
import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
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

import static de.pnku.hungrycows.HungryCows.FED_TIMER;
import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.*;

@Mixin(Moobloom.class)
public abstract class MoobloomMixin extends Cow implements IHungryCows {

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
