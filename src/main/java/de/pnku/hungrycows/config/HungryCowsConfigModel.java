package de.pnku.hungrycows.config;

import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "hungrycows")
@Config(name = "hungrycows", wrapperName = "HungryCowsOwoConfig")
public class HungryCowsConfigModel {

    @SectionHeader("CowSettings")
    @Nest
    @Expanded
    public BlockEatSettings blockEatSettings = new BlockEatSettings();
    public static class BlockEatSettings {

        @RangeConstraint(min = 0.0, max = 3.0, decimalPlaces = 1)
        public float grassEatProbability = 1;

        @RangeConstraint(min = 0, max = 1000)
        public int cowBlockEatGrowthAmount = 60;

        @RangeConstraint(min = 0, max = 10)
        public int cowBlockEatHealAmount = 1;

    }

    @Nest
    @Expanded
    public MilkabilitySettings milkabilitySettings = new MilkabilitySettings();
    public static class MilkabilitySettings {

        @RangeConstraint(min = 0.0, max = 64.0, decimalPlaces = 1)
        public float averageFoodForMilkabilityRegainAmount = 1.0F;

        @RangeConstraint(min = 0, max = 1200)
        public int secondsUntilFeedabilityRegain = 300;

        public mCDO milkableCowDisplayType = mCDO.HIDE_NONE;

        public enum mCDO {
            HIDE_MODEL, HIDE_NONE, HIDE_TEXTURE_AND_MODEL
        }
    }

    @Nest
    public SheepSettings sheepSettings = new SheepSettings();
    public static class SheepSettings {

        public boolean isSheepBlockEatToHeal = true;

        public boolean isSheepFeedToHeal = true;

        public boolean isSheepFeedToRegrowWool = true;
    }

    @SectionHeader("MilkSettings")

    @RangeConstraint(min = 0, max = 20)
    public int milkNutritionValue = 6;

    @RangeConstraint(min = 0.0,  max = 2.0, decimalPlaces = 1)
    public float milkSaturationModifier = 1.2F;

}