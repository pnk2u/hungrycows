package de.pnku.hungrycows.mixin.entity.ai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.sound.HungryCowsSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Predicate;
import static de.pnku.hungrycows.block.HungryCowsBlockTags.*;
import static de.pnku.hungrycows.entity.HungryCowsEntityTypeTags.*;


@Mixin(EatBlockGoal.class)
public abstract class EatBlockGoalMixin {

    @Shadow @Final
    private Mob mob;
    @Shadow
    private int eatAnimationTick;
    @Unique
    private Predicate<BlockState> IS_EDIBLE_PLANT = blockState -> {
        if (this.mob.getType().is(HUNGRY_COWS)) { return blockState.is(EDIBLE_PLANTS_FOR_COWS); }
        else if (this.mob.getType().is(HUNGRY_MOOSHROOMS)) { return blockState.is(EDIBLE_PLANTS_FOR_MOOSHROOMS); }
        else if (this.mob.getType().is(HUNGRY_SHEEP)) { return blockState.is(EDIBLE_PLANTS_FOR_SHEEP);}
        return false;
    };
    @Unique
    private Predicate<BlockState> IS_EDIBLE_BLOCK = blockState -> {
        if (this.mob.getType().is(HUNGRY_GRAZERS)) { return blockState.is(Blocks.GRASS_BLOCK); }
        else if (this.mob.getType().is(HUNGRY_MYCOPHAGES)) {
            BlockState onTopBlockState = this.mob.level().getBlockState(this.mob.blockPosition());
            return blockState.is(Blocks.MYCELIUM) && !(onTopBlockState.is(Blocks.RED_MUSHROOM) || onTopBlockState.is(Blocks.BROWN_MUSHROOM)); }
        return false;
    };
    @Unique Predicate<BlockState> IS_EDIBLE_FLOWER_FOR_BROWN_MOOSHROOMS = blockState ->
            this.mob instanceof MushroomCow mooshroom
                    && mooshroom.getVariant().equals(MushroomCow.MushroomType.BROWN)
                    && blockState.is(BlockTags.SMALL_FLOWERS);
    @Unique
    private boolean isPlantEater() {
        return this.mob.getType().is(HUNGRY_COWS) || this.mob.getType().is(HUNGRY_MOOSHROOMS) || this.mob.getType().is(HUNGRY_SHEEP);
    }
    @Unique
    private boolean isBlockEater() {
        return this.mob.getType().is(HUNGRY_GRAZERS) || this.mob.getType().is(HUNGRY_MYCOPHAGES);
    }
    @Unique
    private void playEatSound() {
        if (this.mob.getType().is(HUNGRY_COWS)) {
            this.mob.playSound(HungryCowsSoundEvents.COW_EAT, 0.95F, 0.7F);
        }
        else if (this.mob.getType().is(HUNGRY_MOOSHROOMS)) {
            this.mob.playSound(SoundEvents.MOOSHROOM_EAT, 1.2F, 0.95F);
        }
        else if (this.mob.getType().is(HUNGRY_SHEEP)) {
            this.mob.playSound(HungryCowsSoundEvents.SHEEP_EAT, 0.5F, 0.8F);
        }
    }

    @WrapOperation(method = "canUse", at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"))
    private boolean wrappedCanUseAtPredicateTest(Predicate<Object> instance, Object blockState, Operation<Boolean> original) {
        if (IS_EDIBLE_FLOWER_FOR_BROWN_MOOSHROOMS.test((BlockState) blockState)) {
            return true;
        }
        if (isPlantEater()) {
            return IS_EDIBLE_PLANT.test((BlockState) blockState);
        }
        return original.call(instance, blockState);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"))
    private boolean wrappedTickAtPredicateTest(Predicate<Object> instance, Object blockState, Operation<Boolean> original) {
        if (this.mob instanceof MushroomCow mooshroom && IS_EDIBLE_FLOWER_FOR_BROWN_MOOSHROOMS.test((BlockState) blockState)) {
            HungryCows.getLogger().debug("Ate an edible flower as a brown mooshroom");
            if (mooshroom.effect != null) {
                HungryCows.getLogger().debug("The Mooshroom already had stew effects, no new effects will be added");
                for(int i = 0; i < 2; ++i) {
                    mooshroom.level().addParticle(ParticleTypes.SMOKE, mooshroom.getX() + mooshroom.getRandom().nextDouble() / (double)2.0F, mooshroom.getY((double)0.5F), mooshroom.getZ() + mooshroom.getRandom().nextDouble() / (double)2.0F, (double)0.0F, mooshroom.getRandom().nextDouble() / (double)5.0F, (double)0.0F);
                }
            } else {
                HungryCows.getLogger().debug("The Mooshroom did not have any stew effects, trying to get new effects from the eaten flower: " + ((BlockState) blockState).getBlock().asItem());
                Optional<Pair<MobEffect, Integer>> optional = mooshroom.getEffectFromItemStack(((BlockState) blockState).getBlock().asItem().getDefaultInstance());
                if (optional.isPresent()) {
                    HungryCows.getLogger().debug("The eaten flower had stew effects, adding them to the mooshroom");
                    for (int j = 0; j < 4; ++j) {
                        mooshroom.level().addParticle(ParticleTypes.EFFECT, mooshroom.getX() + mooshroom.getRandom().nextDouble() / (double) 2.0F, mooshroom.getY((double) 0.5F), mooshroom.getZ() + mooshroom.getRandom().nextDouble() / (double) 2.0F, (double) 0.0F, mooshroom.getRandom().nextDouble() / (double) 5.0F, (double) 0.0F);
                    }
                    mooshroom.effect = optional.get().getLeft();
                    mooshroom.effectDuration = optional.get().getRight();
                } else HungryCows.getLogger().debug("The eaten flower did not have any stew effects, no new effects will be added");
            }
            return true;
        }
        if (isPlantEater()) {
            return IS_EDIBLE_PLANT.test((BlockState) blockState);
        }
        return original.call(instance, blockState);
    }

    @WrapOperation(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean wrappedCanUseAtBlockStateIs(BlockState instance, Block block, Operation<Boolean> original) {
         if (isBlockEater()) {
            return IS_EDIBLE_BLOCK.test(instance);
        }
        return original.call(instance, block);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean wrappedTickAtBlockStateIs(BlockState instance, Block block, Operation<Boolean> original) {
        if (isBlockEater()) {
            return IS_EDIBLE_BLOCK.test(instance);
        }
        return original.call(instance, block);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectedTickAtHead(CallbackInfo ci) {
        if (this.eatAnimationTick == 5) {
            playEatSound();
        }
    }
}
