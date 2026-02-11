package de.pnku.hungrycows.util;

import net.minecraft.world.item.ItemStack;

public interface IHungryCows {

float hungrycows$getNeckAngle(float delta);
float hungrycows$getNeckAngle();
float hungrycows$getHeadAngle(float delta);
float hungrycows$getHeadAngle();
float hungrycows$headAngle = 0;
void hungrycows$setHeadAngle(float angle);
float hungrycows$neckAngle = 0;
void hungrycows$setNeckAngle(float angle);
boolean hungrycows$isEating();
boolean hungrycows$isMilkable();
boolean hungrycows$isMilkable = false;
void hungrycows$setMilkable(boolean milkable);
boolean hungrycows$isMilked();
boolean hungrycows$isMooshroom();
boolean hungrycows$isCow();
String hungrycows$getName();
void hungrycows$setMilked(boolean milked);
int hungrycows$getCowHasBeenFedManuallyTimer();
void hungrycows$setCowHasBeenFedManuallyTimer(int time);
int hungrycows$getSheepHasBeenFedManuallyTimer();
void hungrycows$setSheepHasBeenFedManuallyTimer(int time);
int hungrycows$getGoatHasBeenFedManuallyTimer();
void hungrycows$setGoatHasBeenFedManuallyTimer(int time);
int hungrycows$getFeedableHasBeenFedManuallyTimer();
void hungrycows$setFeedableHasBeenFedManuallyTimer(int time);
ItemStack hungrycows$getEdibleMilk();
}
