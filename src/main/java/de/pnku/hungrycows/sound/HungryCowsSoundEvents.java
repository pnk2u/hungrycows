package de.pnku.hungrycows.sound;

import de.pnku.hungrycows.HungryCows;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class HungryCowsSoundEvents {
    public static final SoundEvent COW_EAT = SoundEvent.createVariableRangeEvent(HungryCows.withModId("entity.cow.eat"));
    public static final SoundEvent SHEEP_EAT = SoundEvent.createVariableRangeEvent(HungryCows.withModId("entity.sheep.eat"));
    public static final SoundEvent MOOSHROOM_MILK = SoundEvent.createVariableRangeEvent(HungryCows.withModId("entity.mooshroom.milk"));

    private HungryCowsSoundEvents() {}

    public static void registerSoundEvents() {
        register(COW_EAT);
        register(SHEEP_EAT);
    }

    private static void register(SoundEvent soundEvent) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, soundEvent.getLocation(), soundEvent);
    }
}
