package de.pnku.hungrycows.item;

import de.pnku.hungrycows.util.IHungryCows;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
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
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack stack) {
                LevelAccessor levelAccessor = blockSource.getLevel();
                BlockPos blockPos = blockSource.getPos().relative((Direction)blockSource.getBlockState().getValue(DispenserBlock.FACING));
                BlockState blockState = levelAccessor.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (block instanceof BucketPickup) {
                    ItemStack itemStack = ((BucketPickup)block).pickupBlock(levelAccessor, blockPos, blockState);
                    if (itemStack.isEmpty()) {
                        return super.execute(blockSource, stack);
                    } else {
                        levelAccessor.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, blockPos);
                        Item item = itemStack.getItem();
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            return new ItemStack(item);
                        } else {
                            if (((DispenserBlockEntity)blockSource.getEntity()).addItem(new ItemStack(item)) < 0) {
                                this.defaultDispenseItemBehavior.dispense(blockSource, new ItemStack(item));
                            }

                            return stack;
                        }
                    }
                } else {
                    ServerLevel serverLevel = blockSource.getLevel();
                    if (!serverLevel.isClientSide()) {
                        for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS)) {
                            if (livingEntity.getType() == EntityType.COW || livingEntity.getType() == EntityType.MOOSHROOM || livingEntity.getType() == EntityType.GOAT) {
                                if (((IHungryCows) livingEntity).hungrycows$isMilkable()) {
                                    ((IHungryCows) livingEntity).hungrycows$setMilked(true);
                                    serverLevel.playSound(livingEntity, blockPos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.317F, 2.37F);
                                    serverLevel.playSound(livingEntity, blockPos, SoundEvents.COW_MILK, SoundSource.BLOCKS, 0.554F, 1.108F);
                                    ItemStack itemStackMilk = ((IHungryCows) livingEntity).hungrycows$getEdibleMilk();
                                    stack.shrink(1);
                                    if (stack.isEmpty()) {
                                        return itemStackMilk.copy();
                                    } else {
                                        if (((DispenserBlockEntity)blockSource.getEntity()).addItem(itemStackMilk.copy()) < 0) {
                                            this.defaultDispenseItemBehavior.dispense(blockSource, itemStackMilk.copy());
                                        }

                                        return stack;
                                    }
                                }
                            }
                        }
                    }
                }
                return super.execute(blockSource, stack);
            }
        };
    }

    protected static DispenseItemBehavior milkingBowlBehavior() {
        return new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack item) {
                LevelAccessor levelAccessor = blockSource.getLevel();
                BlockPos blockPos = blockSource.getPos().relative((Direction)blockSource.getBlockState().getValue(DispenserBlock.FACING));
                BlockState blockState = levelAccessor.getBlockState(blockPos);
                Block block = blockState.getBlock();
                ServerLevel serverLevel = blockSource.getLevel();
                if (!serverLevel.isClientSide()) {
                    for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos), EntitySelector.NO_SPECTATORS)) {
                        if (livingEntity.getType() == EntityType.MOOSHROOM) {
                            if (((IHungryCows) livingEntity).hungrycows$isMilkable()) {
                                boolean bl = false;
                                ItemStack itemStack2;
                                if (((MushroomCow) livingEntity).effect != null) {
                                    bl = true;
                                    itemStack2 = new ItemStack(Items.SUSPICIOUS_STEW);
                                    SuspiciousStewItem.saveMobEffect(itemStack2, ((MushroomCow) livingEntity).effect, ((MushroomCow) livingEntity).effectDuration);
                                    ((MushroomCow) livingEntity).effect = null;
                                    ((MushroomCow) livingEntity).effectDuration = 0;
                                } else {
                                    itemStack2 = new ItemStack(Items.MUSHROOM_STEW);
                                }

                                ((IHungryCows) ((MushroomCow) livingEntity)).hungrycows$setMilked(true);

                                SoundEvent soundEvent = bl ? SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY : SoundEvents.MOOSHROOM_MILK;

                                serverLevel.playSound(livingEntity, blockPos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.317F, 2.37F);
                                serverLevel.playSound(livingEntity, blockPos, soundEvent, SoundSource.BLOCKS, 0.554F, 1.108F);
                                item.shrink(1);
                                if (item.isEmpty()) {
                                    return itemStack2.copy();
                                } else {
                                    if (((DispenserBlockEntity)blockSource.getEntity()).addItem(itemStack2.copy()) < 0) {
                                        this.defaultDispenseItemBehavior.dispense(blockSource, itemStack2.copy());
                                    }

                                    return item;
                                }
                            }
                        }
                    }
                } return super.execute(blockSource, item);
            }
        };
    }
}
