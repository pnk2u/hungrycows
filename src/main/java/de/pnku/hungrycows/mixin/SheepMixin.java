package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static de.pnku.hungrycows.HungryCows.*;

@Mixin(Sheep.class)
public abstract class SheepMixin {

    @Unique
    Sheep thisSheep = (Sheep) (Object) this;

    @Inject(method = "ate", at = @At("TAIL"))
    public void injectedAte(CallbackInfo ci) {
        if (thisSheep.getHealth() < thisSheep.getMaxHealth() && sheepSettings.isSheepBlockEatToHeal()) {
            thisSheep.heal(blockEatSettings.cowBlockEatHealAmount());
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    public void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ItemTags.SHEEP_FOOD) && thisSheep.isSheared() && (sheepSettings.isSheepFeedToRegrowWool() || sheepSettings.isSheepFeedToHeal())) {
            Random rand = new Random(); int n = rand.nextInt((int)(milkabilitySettings.averageFoodForMilkabilityRegainAmount() * 10F)) + 1;
            if (n <= 10) {
                thisSheep.setSheared(false);
            }
            itemStack.consume(1, player);
            if ((thisSheep.getHealth() < thisSheep.getMaxHealth()) && sheepSettings.isSheepFeedToHeal()) {
                thisSheep.heal(2.0F);
            }
        }
    }

}
