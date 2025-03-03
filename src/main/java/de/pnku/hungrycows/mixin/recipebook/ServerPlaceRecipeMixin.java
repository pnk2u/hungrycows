package de.pnku.hungrycows.mixin.recipebook;

import net.minecraft.core.Holder;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlaceRecipe.class)
public abstract class ServerPlaceRecipeMixin {

    @Shadow
    protected Inventory inventory;

    @Inject(method = "moveItemToGrid", at = @At("HEAD"), cancellable = true)
    private void moveItemToGrid(Slot slot, Holder<Item> item, int maxAmount, CallbackInfoReturnable<Integer> cir) {
        int i = this.inventory.findSlotMatchingCraftingIngredient(item, slot.getItem());
        if (i == -1 && slot.getItem().is(Items.MILK_BUCKET) && !slot.hasItem()) {
            for (int j = 0; j < this.inventory.items.size(); j++) {
                ItemStack itemStack = this.inventory.getItem(j);
                if (!itemStack.isEmpty()
                        && itemStack.is(slot.getItem().getItem())
                        && !itemStack.isDamaged()
                        && !itemStack.isEnchanted()) {
                    i = j;
                    break;
                }
            }
        }

        if (i != -1) {
            ItemStack itemStack = this.inventory.getItem(i);
            int j;
            if (maxAmount < itemStack.getCount()) {
                this.inventory.removeItem(i, maxAmount);
                j = maxAmount;
            } else {
                this.inventory.removeItemNoUpdate(i);
                j = itemStack.getCount();
            }

            if (slot.getItem().isEmpty()) {
                slot.set(itemStack.copyWithCount(j));
            } else {
                slot.getItem().grow(j);
            }

            cir.setReturnValue(maxAmount - j);
        }
    }
}