package de.pnku.hungrycows.jade;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.*;

@WailaPlugin
public class HungryCowsPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(EdibleBlockComponentProvider.INSTANCE, Block.class);
        registration.registerEntityComponent(MilkabilityEntityComponentProvider.INSTANCE, Animal.class);
        registration.registerEntityComponent(FeedabilityEntityComponentProvider.INSTANCE, Animal.class);
        registration.registerEntityComponent(SuspiciousEntityComponentProvider.INSTANCE, MushroomCow.class);
    }
}