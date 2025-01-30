package de.pnku.hungrycows.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.EnumSet;

public class EatMyceliumBlockGoal extends Goal {
    private static final int EAT_ANIMATION_TICKS = 40;
    private final Mob mob;
    private final Level level;
    private int eatAnimationTick;

    public EatMyceliumBlockGoal(Mob mob) {
        this.mob = mob;
        this.level = mob.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos blockPos = this.mob.blockPosition();
            return this.level.getBlockState(blockPos.below()).is(Blocks.MYCELIUM);
        }
    }

    @Override
    public void start() {
        this.eatAnimationTick = this.adjustedTickDelay(40);
        this.mob.playSound(SoundEvents.MOOSHROOM_EAT, 1.2F, 0.95F);
        this.level.broadcastEntityEvent(this.mob, (byte)10);
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.eatAnimationTick = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.eatAnimationTick > 0;
    }

    public int getEatAnimationTick() {
        return this.eatAnimationTick;
    }

    @Override
    public void tick() {
        this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
            BlockPos blockPos = this.mob.blockPosition();
            BlockPos blockPos2 = blockPos.below();
            if (this.level.getBlockState(blockPos2).is(Blocks.MYCELIUM)) {
                if (((ServerLevel)this.level).getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.level.levelEvent(2001, blockPos2, Block.getId(Blocks.MYCELIUM.defaultBlockState()));
                    this.level.setBlock(blockPos2, Blocks.DIRT.defaultBlockState(), 2);
                }

                this.mob.ate();
            }
        }
    }
}

