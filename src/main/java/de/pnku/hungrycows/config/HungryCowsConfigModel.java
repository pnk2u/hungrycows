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

    @Nest
    public MilkSettings milkSettings = new MilkSettings();
    public static class MilkSettings {
        @RangeConstraint(min = -1, max = 0)
        public int milkNutritionValue = 0;

        @RangeConstraint(min = -1.0, max = 0.0, decimalPlaces = 1)
        public float milkSaturationModifier = 0.0F;
    }

}