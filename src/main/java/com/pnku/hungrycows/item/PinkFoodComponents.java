package com.pnku.hungrycows.item;

import com.pnku.hungrycows.config.HungryCowsConfig;
import net.minecraft.world.food.FoodProperties;

public class PinkFoodComponents {
    public static final FoodProperties MILK_BUCKET = new FoodProperties.Builder().nutrition(HungryCowsConfig.getInstance().getMilkNutritionValue()).saturationModifier(HungryCowsConfig.getInstance().getMilkSaturationModifier()).alwaysEdible().build();
}