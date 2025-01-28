package de.pnku.hungrycows.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.network.chat.Component;

public class HungryCowsConfigScreen implements ModMenuApi {

    private static final HungryCowsConfig config = HungryCowsConfig.getInstance();

    public static ConfigBuilder builder() {
        ConfigBuilder configBuilder = ConfigBuilder.create()
                .setTitle(Component.translatable("title.hungrycows.config"))
                .setEditable(true)
                .setSavingRunnable(() -> HungryCowsConfigJsonHelper.writeToConfig());

        ConfigCategory cow = configBuilder.getOrCreateCategory(Component.translatable("config.category.hungrycows.cow"));
        ConfigCategory milk = configBuilder.getOrCreateCategory(Component.translatable("config.category.hungrycows.milk"));
        cow.addEntry(configBuilder.entryBuilder()
                .startFloatField(Component.translatable("config.cow_option.hungrycows.grasseat_probability"), config.getGrassEatProbability())
                .setDefaultValue(1)
                        .setMin(0)
                        .setMax(3)
                .setSaveConsumer(grassEatProbabilityFloat -> {
                    config.setGrassEatProbability(grassEatProbabilityFloat);})
                .setTooltip(Component.translatable("config.cow_option.hungrycows.grasseat_probability.tooltip"))
                .build());

        cow.addEntry(configBuilder.entryBuilder()
                .startIntField(Component.translatable("config.cow_option.hungrycows.grasseat_growth"), config.getCowBlockEatGrowthAmount())
                .setDefaultValue(60)
                .setMin(0)
                .setMax(1200)
                .setSaveConsumer(cowBlockEatGrowthAmountInt -> {
                    config.setCowBlockEatGrowthAmount(cowBlockEatGrowthAmountInt);})
                .setTooltip(Component.translatable("config.cow_option.hungrycows.grasseat_growth.tooltip"))
                .build());

        cow.addEntry(configBuilder.entryBuilder()
                .startIntField(Component.translatable("config.cow_option.hungrycows.grasseat_heal_cow"), config.getCowBlockEatHealAmount())
                .setDefaultValue(1)
                        .setMin(0)
                        .setMax(10)
                .setSaveConsumer(cowBlockEatHealAmountInt -> {
                    config.setCowBlockEatHealAmount(cowBlockEatHealAmountInt);})
                .setTooltip(Component.translatable("config.cow_option.hungrycows.grasseat_heal_cow.tooltip"))
                .build());

        cow.addEntry(configBuilder.entryBuilder()
                .startIntField(Component.translatable("config.cow_option.hungrycows.grasseat_heal_sheep"), config.getSheepBlockEatHealAmount())
                .setDefaultValue(1)
                        .setMin(0)
                        .setMax(10)
                .setSaveConsumer(sheepBlockEatHealAmountInt -> {
                    config.setSheepBlockEatHealAmount(sheepBlockEatHealAmountInt);})
                .setTooltip(Component.translatable("config.cow_option.hungrycows.grasseat_heal_sheep.tooltip"))
                .build());
        cow.addEntry(configBuilder.entryBuilder()
                .startFloatField(Component.translatable("config.cow_option.cowfood_eat_milkability_regain"), config.getAverageFoodForMilkabilityRegainAmount())
                .setDefaultValue(1.0F)
                        .setMin(1.0F)
                        .setMax(64.0F)
                .setSaveConsumer(cowFoodEatMilkabilityRegainFloat -> {
                    config.setAverageFoodForMilkabilityRegainAmount(cowFoodEatMilkabilityRegainFloat);})
                .setTooltip(Component.translatable("config.cow_option.cowfood_eat_milkability_regain.tooltip"))
                .build());

        cow.addEntry(configBuilder.entryBuilder()
                .startBooleanToggle(Component.translatable("config.cow_option.hungrycows.hide_milkable_model"), config.isMilkableModelHidden())
                .setDefaultValue(false)
                .setSaveConsumer(milkableModelHiddenBoolean -> {
                    config.setMilkableModelHidden(milkableModelHiddenBoolean);})
                .setTooltip(Component.translatable("config.cow_option.hungrycows.hide_milkable_model.tooltip"))
                .build());

        milk.addEntry(configBuilder.entryBuilder()
                .startIntField(Component.translatable("config.milk_option.hungrycows.milk_nutrition"), config.getMilkNutritionValue())
                .setDefaultValue(6)
                .setMin(0)
                .setMax(20)
                .setSaveConsumer(milkNutritionValueInt -> {
                    config.setMilkNutritionValue(milkNutritionValueInt);})
                .setTooltip(Component.translatable("config.milk_option.hungrycows.milk_nutrition.tooltip"))
                .build());
        milk.addEntry(configBuilder.entryBuilder()
                .startFloatField(Component.translatable("config.milk_option.hungrycows.milk_saturation"), config.getMilkSaturationModifier())
                .setDefaultValue(1.2f)
                .setMin(0.0f)
                .setMax(2.0f)
                .setSaveConsumer(milkSaturationModifierFloat -> {
                    config.setMilkSaturationModifier(milkSaturationModifierFloat);})
                .setTooltip(Component.translatable("config.milk_option.hungrycows.milk_saturation.tooltip"))
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