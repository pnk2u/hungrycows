package de.pnku.hungrycows.mixin.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item {
    public MilkBucketItemMixin(Item.Properties properties) {super(properties);}


    /**
     * @author pnku (pnk2u)
     * @reason HoneyBottleItem's finishUsingItem() is more fitting now that milk needs to respect FoodComponents. Otherwise it is essentially the same.
     */
    @Overwrite
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity){
        super.finishUsingItem(stack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            livingEntity.removeAllEffects();
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.BUCKET);
        } else {
            if (livingEntity instanceof Player) {
                Player player = (Player)livingEntity;
                if (!player.hasInfiniteMaterials()) {
                    ItemStack itemStack = new ItemStack(Items.BUCKET);
                    if (!player.getInventory().add(itemStack)) {
                        player.drop(itemStack, false);
                    }
                }
            }

            return stack;
        }
    }
}
