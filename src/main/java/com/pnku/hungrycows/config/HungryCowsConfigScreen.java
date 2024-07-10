package com.pnku.hungrycows.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.network.chat.Component;

public class HungryCowsConfigScreen implements ModMenuApi {

    public static ConfigBuilder builder() {
        ConfigBuilder configBuilder = ConfigBuilder.create()
                .setTitle(Component.translatable("title.hungrycows.config"))
                .setEditable(true)
                .setSavingRunnable(() -> HungryCowsConfigJsonHelper.writeToConfig());

        ConfigCategory cow = configBuilder.getOrCreateCategory(Component.translatable("config.category.hungrycows.cow"));

        cow.addEntry(configBuilder.entryBuilder()
                .startFloatField(Component.translatable("config.cow_option.hungrycows.grasseat_probability"), HungryCowsConfig.getInstance().getGrassEatProbability())
                .setDefaultValue(1)
                        .setMin(0)
                        .setMax(3)
                .setSaveConsumer(grassEatProbabilityFloat -> {
                    HungryCowsConfig.getInstance().setGrassEatProbability(grassEatProbabilityFloat);})
                .setTooltip(Component.translatable("config.cow_option.hungrycows.grasseat_probability.tooltip"))
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