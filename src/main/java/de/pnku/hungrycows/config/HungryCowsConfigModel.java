package de.pnku.hungrycows.config;

import io.wispforest.owo.config.annotation.*;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

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

        @RangeConstraint(min = 0, max = 1200)
        public int secondsUntilFeedabilityRegain = 300;

        @Nest
        public FeedSettings feedSettings = new FeedSettings();
        public static class FeedSettings {
            @PredicateConstraint("validateItemId")
            public List<String> cowFeedableItems = new ArrayList<>(List.of("minecraft:wheat", "minecraft:short_grass"));

            @PredicateConstraint("validateItemId")
            public List<String> mushroomCowFeedableItems = new ArrayList<>(List.of("minecraft:wheat", "minecraft:short_grass"));

            @PredicateConstraint("validateItemId")
            public List<String> goatFeedableItems = new ArrayList<>(List.of("minecraft:wheat", "minecraft:short_grass"));

            @PredicateConstraint("validateItemId")
            public List<String> sheepFeedableItems = new ArrayList<>(List.of("minecraft:wheat", "minecraft:short_grass"));


            public static boolean validateItemId(List<String> feedableItems) {
                for (String itemId : feedableItems) {
                    String itemNamespace = itemId.split(":")[0];
                    String itemPath = itemId.split(":")[1];
                    if (!Identifier.isValidNamespace(itemNamespace) || !Identifier.isValidPath(itemPath)) {return false;}
                }
                return true;
            }
        }

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
    @Expanded
    public CowMilkSettings cowMilkSettings = new CowMilkSettings();
    public static class CowMilkSettings {
        @RangeConstraint(min = 0, max = 20)
        public int milkNutritionValue = 6;

        @RangeConstraint(min = 0.0, max = 2.0, decimalPlaces = 1)
        public float milkSaturationModifier = 1.2F;
    }

    @Nest
    public MushroomCowMilkSettings mushroomCowMilkSettings = new MushroomCowMilkSettings();
    public static class MushroomCowMilkSettings {
        @RangeConstraint(min = 0, max = 20)
        public int milkNutritionValue = 4;

        @RangeConstraint(min = 0.0, max = 2.0, decimalPlaces = 1)
        public float milkSaturationModifier = 1.3F;
    }

    @Nest
    public GoatMilkSettings goatMilkSettings = new GoatMilkSettings();
    public static class GoatMilkSettings {
        @RangeConstraint(min = 0, max = 20)
        public int milkNutritionValue = 2;

        @RangeConstraint(min = 0.0, max = 2.0, decimalPlaces = 1)
        public float milkSaturationModifier = 1.4F;
    }
}