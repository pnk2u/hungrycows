package de.pnku.hungrycows.item;

import net.minecraft.world.food.FoodProperties;

import static de.pnku.hungrycows.HungryCows.CONFIG;

public class PinkFoodComponents {
    public static final FoodProperties MILK_BUCKET = new FoodProperties.Builder().nutrition(CONFIG.milkNutritionValue()).saturationModifier(CONFIG.milkSaturationModifier()).alwaysEdible().build();
}
