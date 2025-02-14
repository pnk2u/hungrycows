package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups.entity;

import de.pnku.hungrycows.item.HungryCowsItemComponents;
import de.pnku.hungrycows.util.IHungryCows;
import house.greenhouse.bovinesandbuttercups.api.CowVariant;
import house.greenhouse.bovinesandbuttercups.content.data.configuration.MoobloomConfiguration;
import house.greenhouse.bovinesandbuttercups.content.entity.Moobloom;
import house.greenhouse.bovinesandbuttercups.content.sound.BovinesSoundEvents;
import net.minecraft.core.Holder;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.*;

@Debug(export = true)
@Mixin(Moobloom.class)
public abstract class MoobloomMixin extends Cow implements IHungryCows {

    public MoobloomMixin(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public Cow getBreedOffspring(ServerLevel level, AgeableMob otherParent){
        return null;
    }

    @Shadow public Holder<CowVariant<MoobloomConfiguration>> getCowVariant() {return null;}

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

    @Unique
    public ItemStack getEdibleNectar(){
        ItemStack edibleNectar = new ItemStack(((MoobloomConfiguration) ((CowVariant) this.getCowVariant().value()).configuration()).nectar().get().getItem());
        edibleNectar.set(DataComponents.FOOD, HungryCowsItemComponents.COW_MILK_BUCKET);
        edibleNectar.set(DataComponents.MAX_STACK_SIZE, 16);

        return edibleNectar;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void injectMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.BOWL)) {
            if (this.hungrycows$isMilkable() && ((MoobloomConfiguration) ((CowVariant) this.getCowVariant().value()).configuration()).nectar().isPresent()) {
                this.hungrycows$setMilked(true);
                ItemStack filledStack = this.getEdibleNectar();
                ItemStack filledNectarBowl = ItemUtils.createFilledResult(stack, player, filledStack);
                player.setItemInHand(hand, filledNectarBowl);
                this.playSound(BovinesSoundEvents.MOOBLOOM_MILK, 1.0F, 1.0F);
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            } else {
                cir.setReturnValue(InteractionResult.PASS);
            }
        }
    }

    static {
        IS_MILKED_MOOBLOOM = SynchedEntityData.defineId(MoobloomMixin.class, EntityDataSerializers.BOOLEAN);
    }

}
