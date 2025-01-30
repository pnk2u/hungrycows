package de.pnku.hungrycows.renderer;

import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
import net.minecraft.world.entity.animal.MushroomCow;

public class HungryMushroomCowRenderState extends MushroomCowRenderState {
    public MushroomCow.Variant variant;
    public float headAngle;
    public float neckAngle;
    public boolean isMilkable;
}
