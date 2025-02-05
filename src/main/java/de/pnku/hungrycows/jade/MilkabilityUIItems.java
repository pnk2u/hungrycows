package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class MilkabilityUIItems {
    public static final Item HEART_MILK_UI_ITEM = new Item (new Item.Properties());

    public static void initUISpriteItem(){
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("heart_milk"), HEART_MILK_UI_ITEM);
    }
}
