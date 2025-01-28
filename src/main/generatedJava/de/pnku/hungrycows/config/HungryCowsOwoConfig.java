package de.pnku.hungrycows.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HungryCowsOwoConfig extends ConfigWrapper<de.pnku.hungrycows.config.HungryCowsConfig> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Float> grassEatProbability = this.optionForKey(this.keys.grassEatProbability);
    private final Option<java.lang.Integer> milkNutritionValue = this.optionForKey(this.keys.milkNutritionValue);
    private final Option<java.lang.Float> milkSaturationModifier = this.optionForKey(this.keys.milkSaturationModifier);
    private final Option<java.lang.Boolean> milkableModelHidden = this.optionForKey(this.keys.milkableModelHidden);
    private final Option<java.lang.Float> averageFoodForMilkabilityRegainAmount = this.optionForKey(this.keys.averageFoodForMilkabilityRegainAmount);
    private final Option<java.lang.Integer> cowBlockEatGrowthAmount = this.optionForKey(this.keys.cowBlockEatGrowthAmount);
    private final Option<java.lang.Integer> cowBlockEatHealAmount = this.optionForKey(this.keys.cowBlockEatHealAmount);
    private final Option<java.lang.Integer> sheepBlockEatHealAmount = this.optionForKey(this.keys.sheepBlockEatHealAmount);
    private final Option<de.pnku.hungrycows.config.HungryCowsConfig> INSTANCE = this.optionForKey(this.keys.INSTANCE);

    private HungryCowsOwoConfig() {
        super(de.pnku.hungrycows.config.HungryCowsConfig.class);
    }

    private HungryCowsOwoConfig(Consumer<Jankson.Builder> janksonBuilder) {
        super(de.pnku.hungrycows.config.HungryCowsConfig.class, janksonBuilder);
    }

    public static HungryCowsOwoConfig createAndLoad() {
        var wrapper = new HungryCowsOwoConfig();
        wrapper.load();
        return wrapper;
    }

    public static HungryCowsOwoConfig createAndLoad(Consumer<Jankson.Builder> janksonBuilder) {
        var wrapper = new HungryCowsOwoConfig(janksonBuilder);
        wrapper.load();
        return wrapper;
    }

    public float grassEatProbability() {
        return grassEatProbability.value();
    }

    public void grassEatProbability(float value) {
        grassEatProbability.set(value);
    }

    public int milkNutritionValue() {
        return milkNutritionValue.value();
    }

    public void milkNutritionValue(int value) {
        milkNutritionValue.set(value);
    }

    public float milkSaturationModifier() {
        return milkSaturationModifier.value();
    }

    public void milkSaturationModifier(float value) {
        milkSaturationModifier.set(value);
    }

    public boolean milkableModelHidden() {
        return milkableModelHidden.value();
    }

    public void milkableModelHidden(boolean value) {
        milkableModelHidden.set(value);
    }

    public float averageFoodForMilkabilityRegainAmount() {
        return averageFoodForMilkabilityRegainAmount.value();
    }

    public void averageFoodForMilkabilityRegainAmount(float value) {
        averageFoodForMilkabilityRegainAmount.set(value);
    }

    public int cowBlockEatGrowthAmount() {
        return cowBlockEatGrowthAmount.value();
    }

    public void cowBlockEatGrowthAmount(int value) {
        cowBlockEatGrowthAmount.set(value);
    }

    public int cowBlockEatHealAmount() {
        return cowBlockEatHealAmount.value();
    }

    public void cowBlockEatHealAmount(int value) {
        cowBlockEatHealAmount.set(value);
    }

    public int sheepBlockEatHealAmount() {
        return sheepBlockEatHealAmount.value();
    }

    public void sheepBlockEatHealAmount(int value) {
        sheepBlockEatHealAmount.set(value);
    }

    public de.pnku.hungrycows.config.HungryCowsConfig INSTANCE() {
        return INSTANCE.value();
    }

    public void INSTANCE(de.pnku.hungrycows.config.HungryCowsConfig value) {
        INSTANCE.set(value);
    }


    public static class Keys {
        public final Option.Key grassEatProbability = new Option.Key("grassEatProbability");
        public final Option.Key milkNutritionValue = new Option.Key("milkNutritionValue");
        public final Option.Key milkSaturationModifier = new Option.Key("milkSaturationModifier");
        public final Option.Key milkableModelHidden = new Option.Key("milkableModelHidden");
        public final Option.Key averageFoodForMilkabilityRegainAmount = new Option.Key("averageFoodForMilkabilityRegainAmount");
        public final Option.Key cowBlockEatGrowthAmount = new Option.Key("cowBlockEatGrowthAmount");
        public final Option.Key cowBlockEatHealAmount = new Option.Key("cowBlockEatHealAmount");
        public final Option.Key sheepBlockEatHealAmount = new Option.Key("sheepBlockEatHealAmount");
        public final Option.Key INSTANCE = new Option.Key("INSTANCE");
    }
}

