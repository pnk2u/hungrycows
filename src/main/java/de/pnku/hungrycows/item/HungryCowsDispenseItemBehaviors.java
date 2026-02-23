package de.pnku.hungrycows.item;

import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

public class HungryCowsDispenseItemBehaviors {

    public static void registerBucketBehavior() {
        DispenserBlock.registerBehavior(Items.BUCKET, milkingBucketBehavior());
        DispenserBlock.registerBehavior(Items.BOWL, milkingBowlBehavior());
    }

    protected static DispenseItemBehavior milkingBucketBehavior() {
        return new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack item) {
                LevelAccessor levelAccessor = blockSource.level();
                BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                BlockState blockState = levelAccessor.getBlockState(blockPos);
                if (blockState.getBlock() instanceof BucketPickup bucketPickup) {
                    ItemStack itemStack = bucketPickup.pickupBlock(null, levelAccessor, blockPos, blockState);
                    if (itemStack.isEmpty()) {
                        return super.execute(blockSource, item);
                    } else {
                        levelAccessor.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                        Item item2 = itemStack.getItem();
                        return this.consumeWithRemainder(blockSource, item, new ItemStack(item2));
                    }
                } else {
                    ServerLevel serverLevel = blockSource.level();
                    if (!serverLevel.isClientSide()) {
                        for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS)) {
                            if (livingEntity.getType() == EntityType.COW || livingEntity.getType() == EntityType.MOOSHROOM || livingEntity.getType() == EntityType.GOAT) {
                                if (((IHungryCows) livingEntity).hungrycows$isMilkable()) {
                                    ((IHungryCows) livingEntity).hungrycows$setMilked(true);
                                    serverLevel.playSound(livingEntity, blockPos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.317F, 2.37F);
                                    serverLevel.playSound(livingEntity, blockPos, SoundEvents.COW_MILK, SoundSource.BLOCKS, 0.554F, 1.108F);
                                    ItemStack itemStackMilk = ((IHungryCows) livingEntity).hungrycows$getEdibleMilk();
                                    return this.consumeWithRemainder(blockSource, item, itemStackMilk);
                                }
                            }
                        }
                    }
                }
                return super.execute(blockSource, item);
            }
        };
    }

    protected static DispenseItemBehavior milkingBowlBehavior() {
        return new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack item) {
                LevelAccessor levelAccessor = blockSource.level();
                BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                BlockState blockState = levelAccessor.getBlockState(blockPos);
                ServerLevel serverLevel = blockSource.level();
                if (!serverLevel.isClientSide()) {
                    for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS)) {
                        if (livingEntity.getType() == EntityType.MOOSHROOM) {
                            if (((IHungryCows) livingEntity).hungrycows$isMilkable()) {
                                boolean bl = false;
                                ItemStack itemStack2;
                                if (((MushroomCow) livingEntity).stewEffects != null) {
                                    bl = true;
                                    itemStack2 = new ItemStack(Items.SUSPICIOUS_STEW);
                                    itemStack2.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, ((MushroomCow) livingEntity).stewEffects);
                                    ((MushroomCow) livingEntity).stewEffects = null;
                                } else {
                                    itemStack2 = new ItemStack(Items.MUSHROOM_STEW);
                                }

                                ((IHungryCows) livingEntity).hungrycows$setMilked(true);

                                SoundEvent soundEvent = bl ? SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY : SoundEvents.MOOSHROOM_MILK;

                                serverLevel.playSound(livingEntity, blockPos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.317F, 2.37F);
                                serverLevel.playSound(livingEntity, blockPos, soundEvent, SoundSource.BLOCKS, 0.554F, 1.108F);
                                return this.consumeWithRemainder(blockSource, item, itemStack2);
                            }
                        }
                    }
                } return super.execute(blockSource, item);
            }
        };
    }
}
