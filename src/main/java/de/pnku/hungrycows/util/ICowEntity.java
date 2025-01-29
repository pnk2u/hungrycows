package de.pnku.hungrycows.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface ICowEntity {

float hungrycows$getNeckAngle(float delta);
float hungrycows$getHeadAngle(float delta);
boolean hungrycows$isMilkable();
boolean hungrycows$isMilked();
void hungrycows$setMilked(boolean milked);
TagKey<Item> hungrycows$COW_FOOD();
TagKey<Item> hungrycows$SHEEP_FOOD();
}
