package de.pnku.hungrycows.jade;

import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class HungryCowsUIItems {
    public static final Item HEART_MILK_UI_ITEM = new Item (new Item.Properties());
    public static final Item COW_ICON_UI_ITEM = new Item (new Item.Properties());
    public static final Item COLD_COW_ICON_UI_ITEM = new Item (new Item.Properties());
    public static final Item WARM_COW_ICON_UI_ITEM = new Item (new Item.Properties());
    public static final Item MOOSHROOM_ICON_UI_ITEM = new Item (new Item.Properties());
    public static final Item BROWN_MOOSHROOM_ICON_UI_ITEM = new Item (new Item.Properties());
    public static final Item SHEEP_ICON_UI_ITEM = new Item (new Item.Properties());

    public static void initUISpriteItem(){
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("heart_milk"), HEART_MILK_UI_ITEM);
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("cow_icon"), COW_ICON_UI_ITEM);
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("cold_cow_icon"), COLD_COW_ICON_UI_ITEM);
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("warm_cow_icon"), WARM_COW_ICON_UI_ITEM);
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("red_mooshroom_icon"), MOOSHROOM_ICON_UI_ITEM);
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("brown_mooshroom_icon"), BROWN_MOOSHROOM_ICON_UI_ITEM);
        Registry.register(BuiltInRegistries.ITEM, HungryCows.withModId("sheep_icon"), SHEEP_ICON_UI_ITEM);
    }
}
