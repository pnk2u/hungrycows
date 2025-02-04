package de.pnku.hungrycows.util;

import net.minecraft.world.item.ItemStack;

public interface HungryCowsEntityInterface {

float hungrycows$getNeckAngle(float delta);
float hungrycows$getHeadAngle(float delta);
boolean hungrycows$isMilkable();
boolean hungrycows$isMilked();
boolean hungrycows$isMooshroom();
void hungrycows$setMilked(boolean milked);
int hungrycows$getCowHasBeenFedManuallyTimer();
void hungrycows$setCowHasBeenFedManuallyTimer(int time);
int hungrycows$getSheepHasBeenFedManuallyTimer();
void hungrycows$setSheepHasBeenFedManuallyTimer(int time);
int hungrycows$getGoatHasBeenFedManuallyTimer();
void hungrycows$setGoatHasBeenFedManuallyTimer(int time);
ItemStack hungrycows$getEdibleMilk();
}
