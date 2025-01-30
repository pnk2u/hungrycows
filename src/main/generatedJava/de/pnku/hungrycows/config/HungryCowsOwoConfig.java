package de.pnku.hungrycows.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.ConfigWrapper.BuilderConsumer;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HungryCowsOwoConfig extends ConfigWrapper<de.pnku.hungrycows.config.HungryCowsConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Float> blockEatSettings_grassEatProbability = this.optionForKey(this.keys.blockEatSettings_grassEatProbability);
    private final Option<java.lang.Integer> blockEatSettings_cowBlockEatGrowthAmount = this.optionForKey(this.keys.blockEatSettings_cowBlockEatGrowthAmount);
    private final Option<java.lang.Integer> blockEatSettings_cowBlockEatHealAmount = this.optionForKey(this.keys.blockEatSettings_cowBlockEatHealAmount);
    private final Option<java.lang.Float> milkabilitySettings_averageFoodForMilkabilityRegainAmount = this.optionForKey(this.keys.milkabilitySettings_averageFoodForMilkabilityRegainAmount);
    private final Option<de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO> milkabilitySettings_milkableCowDisplayType = this.optionForKey(this.keys.milkabilitySettings_milkableCowDisplayType);
    private final Option<java.lang.Boolean> sheepSettings_isSheepBlockEatToHeal = this.optionForKey(this.keys.sheepSettings_isSheepBlockEatToHeal);
    private final Option<java.lang.Boolean> sheepSettings_isSheepFeedToHeal = this.optionForKey(this.keys.sheepSettings_isSheepFeedToHeal);
    private final Option<java.lang.Boolean> sheepSettings_isSheepFeedToRegrowWool = this.optionForKey(this.keys.sheepSettings_isSheepFeedToRegrowWool);
    private final Option<java.lang.Integer> milkNutritionValue = this.optionForKey(this.keys.milkNutritionValue);
    private final Option<java.lang.Float> milkSaturationModifier = this.optionForKey(this.keys.milkSaturationModifier);

    private HungryCowsOwoConfig() {
        super(de.pnku.hungrycows.config.HungryCowsConfigModel.class);
    }

    private HungryCowsOwoConfig(BuilderConsumer consumer) {
        super(de.pnku.hungrycows.config.HungryCowsConfigModel.class, consumer);
    }

    public static HungryCowsOwoConfig createAndLoad() {
        var wrapper = new HungryCowsOwoConfig();
        wrapper.load();
        return wrapper;
    }

    public static HungryCowsOwoConfig createAndLoad(BuilderConsumer consumer) {
        var wrapper = new HungryCowsOwoConfig(consumer);
        wrapper.load();
        return wrapper;
    }

    public final BlockEatSettings_ blockEatSettings = new BlockEatSettings_();
    public class BlockEatSettings_ implements BlockEatSettings {
        public float grassEatProbability() {
            return blockEatSettings_grassEatProbability.value();
        }

        public void grassEatProbability(float value) {
            blockEatSettings_grassEatProbability.set(value);
        }

        public int cowBlockEatGrowthAmount() {
            return blockEatSettings_cowBlockEatGrowthAmount.value();
        }

        public void cowBlockEatGrowthAmount(int value) {
            blockEatSettings_cowBlockEatGrowthAmount.set(value);
        }

        public int cowBlockEatHealAmount() {
            return blockEatSettings_cowBlockEatHealAmount.value();
        }

        public void cowBlockEatHealAmount(int value) {
            blockEatSettings_cowBlockEatHealAmount.set(value);
        }

    }
    public final MilkabilitySettings_ milkabilitySettings = new MilkabilitySettings_();
    public class MilkabilitySettings_ implements MilkabilitySettings {
        public float averageFoodForMilkabilityRegainAmount() {
            return milkabilitySettings_averageFoodForMilkabilityRegainAmount.value();
        }

        public void averageFoodForMilkabilityRegainAmount(float value) {
            milkabilitySettings_averageFoodForMilkabilityRegainAmount.set(value);
        }

        public de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO milkableCowDisplayType() {
            return milkabilitySettings_milkableCowDisplayType.value();
        }

        public void milkableCowDisplayType(de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO value) {
            milkabilitySettings_milkableCowDisplayType.set(value);
        }

    }
    public final SheepSettings_ sheepSettings = new SheepSettings_();
    public class SheepSettings_ implements SheepSettings {
        public boolean isSheepBlockEatToHeal() {
            return sheepSettings_isSheepBlockEatToHeal.value();
        }

        public void isSheepBlockEatToHeal(boolean value) {
            sheepSettings_isSheepBlockEatToHeal.set(value);
        }

        public boolean isSheepFeedToHeal() {
            return sheepSettings_isSheepFeedToHeal.value();
        }

        public void isSheepFeedToHeal(boolean value) {
            sheepSettings_isSheepFeedToHeal.set(value);
        }

        public boolean isSheepFeedToRegrowWool() {
            return sheepSettings_isSheepFeedToRegrowWool.value();
        }

        public void isSheepFeedToRegrowWool(boolean value) {
            sheepSettings_isSheepFeedToRegrowWool.set(value);
        }

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

    public interface BlockEatSettings {
        float grassEatProbability();
        void grassEatProbability(float value);
        int cowBlockEatGrowthAmount();
        void cowBlockEatGrowthAmount(int value);
        int cowBlockEatHealAmount();
        void cowBlockEatHealAmount(int value);
    }
    public interface MilkabilitySettings {
        float averageFoodForMilkabilityRegainAmount();
        void averageFoodForMilkabilityRegainAmount(float value);
        de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO milkableCowDisplayType();
        void milkableCowDisplayType(de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO value);
    }
    public interface SheepSettings {
        boolean isSheepBlockEatToHeal();
        void isSheepBlockEatToHeal(boolean value);
        boolean isSheepFeedToHeal();
        void isSheepFeedToHeal(boolean value);
        boolean isSheepFeedToRegrowWool();
        void isSheepFeedToRegrowWool(boolean value);
    }
    public static class Keys {
        public final Option.Key blockEatSettings_grassEatProbability = new Option.Key("blockEatSettings.grassEatProbability");
        public final Option.Key blockEatSettings_cowBlockEatGrowthAmount = new Option.Key("blockEatSettings.cowBlockEatGrowthAmount");
        public final Option.Key blockEatSettings_cowBlockEatHealAmount = new Option.Key("blockEatSettings.cowBlockEatHealAmount");
        public final Option.Key milkabilitySettings_averageFoodForMilkabilityRegainAmount = new Option.Key("milkabilitySettings.averageFoodForMilkabilityRegainAmount");
        public final Option.Key milkabilitySettings_milkableCowDisplayType = new Option.Key("milkabilitySettings.milkableCowDisplayType");
        public final Option.Key sheepSettings_isSheepBlockEatToHeal = new Option.Key("sheepSettings.isSheepBlockEatToHeal");
        public final Option.Key sheepSettings_isSheepFeedToHeal = new Option.Key("sheepSettings.isSheepFeedToHeal");
        public final Option.Key sheepSettings_isSheepFeedToRegrowWool = new Option.Key("sheepSettings.isSheepFeedToRegrowWool");
        public final Option.Key milkNutritionValue = new Option.Key("milkNutritionValue");
        public final Option.Key milkSaturationModifier = new Option.Key("milkSaturationModifier");
    }
}

