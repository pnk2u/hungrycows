package de.pnku.hungrycows.util;

public interface ICowEntity {

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
}
