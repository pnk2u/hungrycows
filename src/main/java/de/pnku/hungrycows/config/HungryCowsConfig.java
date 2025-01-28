package de.pnku.hungrycows.config;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "hungrycows-config", wrapperName = "HungryCowsOwoConfig")
public class HungryCowsConfig {

    private float grassEatProbability = 1;
    private int milkNutritionValue = 6;
    private float milkSaturationModifier = 1.2F;
    private boolean milkableModelHidden = false; // Technically only the texture will be prevented from loading effectively hiding the relevant parts of the model.
    private float averageFoodForMilkabilityRegainAmount = 1;
    private int cowBlockEatGrowthAmount = 60;
    private int cowBlockEatHealAmount = 1;
    private int sheepBlockEatHealAmount = 1;
    private static HungryCowsConfig INSTANCE;

    public static HungryCowsConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HungryCowsConfig();
        }

        return INSTANCE;
    }

    public void setGrassEatProbability(float grassEatProbability) {
        this.grassEatProbability = Math.round(grassEatProbability * 10F) / 10F; // Rounded to one decimal place
    }

    public void setMilkNutritionValue(int milkNutritionValue) {
        this.milkNutritionValue = milkNutritionValue;
    }

    public void setMilkSaturationModifier(float milkSaturationModifier) {
        this.milkSaturationModifier = Math.round(milkSaturationModifier * 10F) / 10F; // Rounded to one decimal place
    }

    public void setMilkableModelHidden(boolean milkableModelHidden) {this.milkableModelHidden = milkableModelHidden;}

    public void setAverageFoodForMilkabilityRegainAmount(float averageFoodForMilkabilityRegainAmount) {this.averageFoodForMilkabilityRegainAmount = Math.round(averageFoodForMilkabilityRegainAmount * 10F) / 10F;} // Rounded to one decimal place

    public void setCowBlockEatGrowthAmount(int cowBlockEatGrowthAmount) {this.cowBlockEatGrowthAmount = cowBlockEatGrowthAmount;}

    public void setCowBlockEatHealAmount(int cowBlockEatHealAmount) {this.cowBlockEatHealAmount = cowBlockEatHealAmount;}

    public void setSheepBlockEatHealAmount(int sheepBlockEatHealAmount) {this.sheepBlockEatHealAmount = sheepBlockEatHealAmount;}

    public float getGrassEatProbability() {
        return grassEatProbability;
    }

    public int getGrassEatPriority() {
        int grassEatPriority = (int) Math.pow(2, 4 - getInstance().getGrassEatProbability());
        return grassEatPriority;
    }

    public int getMilkNutritionValue() {
        return milkNutritionValue;
    }

    public float getMilkSaturationModifier() {
        return milkSaturationModifier;
    }

    public boolean isMilkableModelHidden() {return milkableModelHidden;}

    public float getAverageFoodForMilkabilityRegainAmount() {return averageFoodForMilkabilityRegainAmount;}

    public int getCowBlockEatGrowthAmount() {return cowBlockEatGrowthAmount;}

    public int getCowBlockEatHealAmount() {return cowBlockEatHealAmount;}

    public int getSheepBlockEatHealAmount() {return sheepBlockEatHealAmount;}

    public void updateConfigs(HungryCowsConfig config) {
        grassEatProbability = config.getGrassEatProbability();
        milkNutritionValue = config.getMilkNutritionValue();
        milkSaturationModifier = config.getMilkSaturationModifier();
        milkableModelHidden = config.isMilkableModelHidden();
        averageFoodForMilkabilityRegainAmount = config.getAverageFoodForMilkabilityRegainAmount();
        cowBlockEatGrowthAmount = config.getCowBlockEatGrowthAmount();
        cowBlockEatHealAmount = config.getCowBlockEatHealAmount();
        sheepBlockEatHealAmount = config.getSheepBlockEatHealAmount();
    }
}