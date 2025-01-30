package de.pnku.hungrycows.util;

public interface ICowEntity {

float hungrycows$getNeckAngle(float delta);
float hungrycows$getHeadAngle(float delta);
boolean hungrycows$isMilkable();
boolean hungrycows$isMilked();
void hungrycows$setMilked(boolean milked);
boolean hungrycows$isMilkedMooshroom();
void hungrycows$setMilkedMooshroom(boolean milked);
}
