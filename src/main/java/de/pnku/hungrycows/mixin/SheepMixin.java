package de.pnku.hungrycows.mixin;

import de.pnku.hungrycows.util.ICowEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static de.pnku.hungrycows.HungryCows.*;
import static net.minecraft.resources.ResourceLocation.DEFAULT_NAMESPACE;

@Mixin(Sheep.class)
public abstract class SheepMixin implements ICowEntity {

    @Unique
    Sheep thisSheep = (Sheep) (Object) this;
    @Unique
    public final TagKey<Item> hungrycows$SHEEP_FOOD() {
        return TagKey.create(Registries.ITEM, new ResourceLocation(DEFAULT_NAMESPACE, "sheep_food"));
    }

    @Inject(method = "ate", at = @At("TAIL"))
    public void injectedAte(CallbackInfo ci) {
        if (thisSheep.getHealth() < thisSheep.getMaxHealth() && sheepSettings.isSheepBlockEatToHeal()) {
            thisSheep.heal(blockEatSettings.cowBlockEatHealAmount());
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    public void injectedMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(hungrycows$SHEEP_FOOD()) && thisSheep.isSheared() && (sheepSettings.isSheepFeedToRegrowWool() || sheepSettings.isSheepFeedToHeal())) {
            Random rand = new Random(); int n = rand.nextInt((int)(milkabilitySettings.averageFoodForMilkabilityRegainAmount() * 10F)) + 1;
            if (n <= 10) {
                thisSheep.setSheared(false);
            }
            itemStack.shrink(player.getAbilities().instabuild ? 0 : 1);
            if ((thisSheep.getHealth() < thisSheep.getMaxHealth()) && sheepSettings.isSheepFeedToHeal()) {
                thisSheep.heal(2.0F);
            }
        }
    }

}
