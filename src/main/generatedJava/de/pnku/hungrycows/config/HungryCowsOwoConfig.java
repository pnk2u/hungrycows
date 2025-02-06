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
    private final Option<java.lang.Integer> milkabilitySettings_secondsUntilFeedabilityRegain = this.optionForKey(this.keys.milkabilitySettings_secondsUntilFeedabilityRegain);
    private final Option<java.util.List<java.lang.String>> milkabilitySettings_feedSettings_cowFeedableItems = this.optionForKey(this.keys.milkabilitySettings_feedSettings_cowFeedableItems);
    private final Option<java.util.List<java.lang.String>> milkabilitySettings_feedSettings_mushroomCowFeedableItems = this.optionForKey(this.keys.milkabilitySettings_feedSettings_mushroomCowFeedableItems);
    private final Option<java.util.List<java.lang.String>> milkabilitySettings_feedSettings_goatFeedableItems = this.optionForKey(this.keys.milkabilitySettings_feedSettings_goatFeedableItems);
    private final Option<java.util.List<java.lang.String>> milkabilitySettings_feedSettings_sheepFeedableItems = this.optionForKey(this.keys.milkabilitySettings_feedSettings_sheepFeedableItems);
    private final Option<de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO> milkabilitySettings_milkableCowDisplayType = this.optionForKey(this.keys.milkabilitySettings_milkableCowDisplayType);
    private final Option<java.lang.Boolean> sheepSettings_isSheepBlockEatToHeal = this.optionForKey(this.keys.sheepSettings_isSheepBlockEatToHeal);
    private final Option<java.lang.Boolean> sheepSettings_isSheepFeedToHeal = this.optionForKey(this.keys.sheepSettings_isSheepFeedToHeal);
    private final Option<java.lang.Boolean> sheepSettings_isSheepFeedToRegrowWool = this.optionForKey(this.keys.sheepSettings_isSheepFeedToRegrowWool);
    private final Option<java.lang.Integer> cowMilkSettings_milkNutritionValue = this.optionForKey(this.keys.cowMilkSettings_milkNutritionValue);
    private final Option<java.lang.Float> cowMilkSettings_milkSaturationModifier = this.optionForKey(this.keys.cowMilkSettings_milkSaturationModifier);
    private final Option<java.lang.Integer> mushroomCowMilkSettings_milkNutritionValue = this.optionForKey(this.keys.mushroomCowMilkSettings_milkNutritionValue);
    private final Option<java.lang.Float> mushroomCowMilkSettings_milkSaturationModifier = this.optionForKey(this.keys.mushroomCowMilkSettings_milkSaturationModifier);
    private final Option<java.lang.Integer> goatMilkSettings_milkNutritionValue = this.optionForKey(this.keys.goatMilkSettings_milkNutritionValue);
    private final Option<java.lang.Float> goatMilkSettings_milkSaturationModifier = this.optionForKey(this.keys.goatMilkSettings_milkSaturationModifier);

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
        public int secondsUntilFeedabilityRegain() {
            return milkabilitySettings_secondsUntilFeedabilityRegain.value();
        }

        public void secondsUntilFeedabilityRegain(int value) {
            milkabilitySettings_secondsUntilFeedabilityRegain.set(value);
        }

        public final FeedSettings_ feedSettings = new FeedSettings_();
        public class FeedSettings_ implements FeedSettings {
            public java.util.List<java.lang.String> cowFeedableItems() {
                return milkabilitySettings_feedSettings_cowFeedableItems.value();
            }

            public void cowFeedableItems(java.util.List<java.lang.String> value) {
                milkabilitySettings_feedSettings_cowFeedableItems.set(value);
            }

            public java.util.List<java.lang.String> mushroomCowFeedableItems() {
                return milkabilitySettings_feedSettings_mushroomCowFeedableItems.value();
            }

            public void mushroomCowFeedableItems(java.util.List<java.lang.String> value) {
                milkabilitySettings_feedSettings_mushroomCowFeedableItems.set(value);
            }

            public java.util.List<java.lang.String> goatFeedableItems() {
                return milkabilitySettings_feedSettings_goatFeedableItems.value();
            }

            public void goatFeedableItems(java.util.List<java.lang.String> value) {
                milkabilitySettings_feedSettings_goatFeedableItems.set(value);
            }

            public java.util.List<java.lang.String> sheepFeedableItems() {
                return milkabilitySettings_feedSettings_sheepFeedableItems.value();
            }

            public void sheepFeedableItems(java.util.List<java.lang.String> value) {
                milkabilitySettings_feedSettings_sheepFeedableItems.set(value);
            }

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
    public final CowMilkSettings_ cowMilkSettings = new CowMilkSettings_();
    public class CowMilkSettings_ implements CowMilkSettings {
        public int milkNutritionValue() {
            return cowMilkSettings_milkNutritionValue.value();
        }

        public void milkNutritionValue(int value) {
            cowMilkSettings_milkNutritionValue.set(value);
        }

        public float milkSaturationModifier() {
            return cowMilkSettings_milkSaturationModifier.value();
        }

        public void milkSaturationModifier(float value) {
            cowMilkSettings_milkSaturationModifier.set(value);
        }

    }
    public final MushroomCowMilkSettings_ mushroomCowMilkSettings = new MushroomCowMilkSettings_();
    public class MushroomCowMilkSettings_ implements MushroomCowMilkSettings {
        public int milkNutritionValue() {
            return mushroomCowMilkSettings_milkNutritionValue.value();
        }

        public void milkNutritionValue(int value) {
            mushroomCowMilkSettings_milkNutritionValue.set(value);
        }

        public float milkSaturationModifier() {
            return mushroomCowMilkSettings_milkSaturationModifier.value();
        }

        public void milkSaturationModifier(float value) {
            mushroomCowMilkSettings_milkSaturationModifier.set(value);
        }

    }
    public final GoatMilkSettings_ goatMilkSettings = new GoatMilkSettings_();
    public class GoatMilkSettings_ implements GoatMilkSettings {
        public int milkNutritionValue() {
            return goatMilkSettings_milkNutritionValue.value();
        }

        public void milkNutritionValue(int value) {
            goatMilkSettings_milkNutritionValue.set(value);
        }

        public float milkSaturationModifier() {
            return goatMilkSettings_milkSaturationModifier.value();
        }

        public void milkSaturationModifier(float value) {
            goatMilkSettings_milkSaturationModifier.set(value);
        }

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
        int secondsUntilFeedabilityRegain();
        void secondsUntilFeedabilityRegain(int value);
        de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO milkableCowDisplayType();
        void milkableCowDisplayType(de.pnku.hungrycows.config.HungryCowsConfigModel.MilkabilitySettings.mCDO value);
    }
    public interface FeedSettings {
        java.util.List<java.lang.String> cowFeedableItems();
        void cowFeedableItems(java.util.List<java.lang.String> value);
        java.util.List<java.lang.String> mushroomCowFeedableItems();
        void mushroomCowFeedableItems(java.util.List<java.lang.String> value);
        java.util.List<java.lang.String> goatFeedableItems();
        void goatFeedableItems(java.util.List<java.lang.String> value);
        java.util.List<java.lang.String> sheepFeedableItems();
        void sheepFeedableItems(java.util.List<java.lang.String> value);
    }
    public interface SheepSettings {
        boolean isSheepBlockEatToHeal();
        void isSheepBlockEatToHeal(boolean value);
        boolean isSheepFeedToHeal();
        void isSheepFeedToHeal(boolean value);
        boolean isSheepFeedToRegrowWool();
        void isSheepFeedToRegrowWool(boolean value);
    }
    public interface CowMilkSettings {
        int milkNutritionValue();
        void milkNutritionValue(int value);
        float milkSaturationModifier();
        void milkSaturationModifier(float value);
    }
    public interface MushroomCowMilkSettings {
        int milkNutritionValue();
        void milkNutritionValue(int value);
        float milkSaturationModifier();
        void milkSaturationModifier(float value);
    }
    public interface GoatMilkSettings {
        int milkNutritionValue();
        void milkNutritionValue(int value);
        float milkSaturationModifier();
        void milkSaturationModifier(float value);
    }
    public static class Keys {
        public final Option.Key blockEatSettings_grassEatProbability = new Option.Key("blockEatSettings.grassEatProbability");
        public final Option.Key blockEatSettings_cowBlockEatGrowthAmount = new Option.Key("blockEatSettings.cowBlockEatGrowthAmount");
        public final Option.Key blockEatSettings_cowBlockEatHealAmount = new Option.Key("blockEatSettings.cowBlockEatHealAmount");
        public final Option.Key milkabilitySettings_secondsUntilFeedabilityRegain = new Option.Key("milkabilitySettings.secondsUntilFeedabilityRegain");
        public final Option.Key milkabilitySettings_feedSettings_cowFeedableItems = new Option.Key("milkabilitySettings.feedSettings.cowFeedableItems");
        public final Option.Key milkabilitySettings_feedSettings_mushroomCowFeedableItems = new Option.Key("milkabilitySettings.feedSettings.mushroomCowFeedableItems");
        public final Option.Key milkabilitySettings_feedSettings_goatFeedableItems = new Option.Key("milkabilitySettings.feedSettings.goatFeedableItems");
        public final Option.Key milkabilitySettings_feedSettings_sheepFeedableItems = new Option.Key("milkabilitySettings.feedSettings.sheepFeedableItems");
        public final Option.Key milkabilitySettings_milkableCowDisplayType = new Option.Key("milkabilitySettings.milkableCowDisplayType");
        public final Option.Key sheepSettings_isSheepBlockEatToHeal = new Option.Key("sheepSettings.isSheepBlockEatToHeal");
        public final Option.Key sheepSettings_isSheepFeedToHeal = new Option.Key("sheepSettings.isSheepFeedToHeal");
        public final Option.Key sheepSettings_isSheepFeedToRegrowWool = new Option.Key("sheepSettings.isSheepFeedToRegrowWool");
        public final Option.Key cowMilkSettings_milkNutritionValue = new Option.Key("cowMilkSettings.milkNutritionValue");
        public final Option.Key cowMilkSettings_milkSaturationModifier = new Option.Key("cowMilkSettings.milkSaturationModifier");
        public final Option.Key mushroomCowMilkSettings_milkNutritionValue = new Option.Key("mushroomCowMilkSettings.milkNutritionValue");
        public final Option.Key mushroomCowMilkSettings_milkSaturationModifier = new Option.Key("mushroomCowMilkSettings.milkSaturationModifier");
        public final Option.Key goatMilkSettings_milkNutritionValue = new Option.Key("goatMilkSettings.milkNutritionValue");
        public final Option.Key goatMilkSettings_milkSaturationModifier = new Option.Key("goatMilkSettings.milkSaturationModifier");
    }
}

