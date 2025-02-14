package de.pnku.hungrycows.util;

import net.minecraft.world.item.ItemStack;

public interface IHungryCows {

float hungrycows$getNeckAngle(float delta);
float hungrycows$getHeadAngle(float delta);
boolean hungrycows$isMilkable();
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
