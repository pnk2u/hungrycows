package de.pnku.hungrycows.item;

import net.minecraft.world.food.FoodProperties;

import static de.pnku.hungrycows.config.HungryCowsConfigAccessor.*;

public class HungryCowsItemComponents {
    public static final FoodProperties COW_MILK_BUCKET = new FoodProperties.Builder().nutrition(cowMilkSettings.milkNutritionValue()).saturationModifier(cowMilkSettings.milkSaturationModifier()).alwaysEdible().build();
    public static final FoodProperties MOOSHROOM_MILK_BUCKET = new FoodProperties.Builder().nutrition(mushroomCowMilkSettings.milkNutritionValue()).saturationModifier(mushroomCowMilkSettings.milkSaturationModifier()).alwaysEdible().build();
    public static final FoodProperties GOAT_MILK_BUCKET = new FoodProperties.Builder().nutrition(goatMilkSettings.milkNutritionValue()).saturationModifier(goatMilkSettings.milkSaturationModifier()).alwaysEdible().build();
}
