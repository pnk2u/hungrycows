package com.pnku.hungrycows.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;

public class HungryCowsConfigScreen implements ModMenuApi {

    public static ConfigBuilder builder() {
        ConfigBuilder configBuilder = ConfigBuilder.create()
                .setTitle(Text.translatable("title.hungrycows.config"))
                .setEditable(true)
                .setSavingRunnable(() -> HungryCowsConfigJsonHelper.writeToConfig());

        ConfigCategory cow = configBuilder.getOrCreateCategory(Text.translatable("config.category.hungrycows.cow"));
        ConfigCategory milk = configBuilder.getOrCreateCategory(Text.translatable("config.category.hungrycows.milk"));

        cow.addEntry(configBuilder.entryBuilder()
                .startFloatField(Text.translatable("config.cow_option.hungrycows.grasseat_probability"), HungryCowsConfig.getInstance().getGrassEatProbability())
                .setDefaultValue(1)
                        .setMin(0)
                        .setMax(3)
                .setSaveConsumer(grassEatProbabilityFloat -> {
                    HungryCowsConfigJsonHelper.INSTANCE.setGrassEatProbability(grassEatProbabilityFloat);})
                .build());
        milk.addEntry(configBuilder.entryBuilder()
                .startIntField(Text.translatable("config.milk_option.hungrycows.milk_nutrition"), HungryCowsConfig.INSTANCE.getMilkNutritionValue())
                .setDefaultValue(6)
                .setMin(0)
                .setMax(20)
                .setSaveConsumer(milkNutritionValueInt -> {
                    HungryCowsConfigJsonHelper.INSTANCE.setMilkNutritionValue(milkNutritionValueInt);})
                .setTooltip(Text.translatable("config.milk_option.hungrycows.milk_nutrition.tooltip"))
                .build());
        milk.addEntry(configBuilder.entryBuilder()
                .startFloatField(Text.translatable("config.milk_option.hungrycows.milk_saturation"), HungryCowsConfig.INSTANCE.getMilkSaturationModifier())
                .setDefaultValue(1.2f)
                .setMin(0.0f)
                .setMax(2.0f)
                .setSaveConsumer(milkSaturationModifierFloat -> {
                    HungryCowsConfigJsonHelper.INSTANCE.setMilkSaturationModifier(milkSaturationModifierFloat);})
                .setTooltip(Text.translatable("config.milk_option.hungrycows.milk_saturation.tooltip"))
                .build());
        return configBuilder;

    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
           return builder().setParentScreen(parent).build();
        };
    }
}