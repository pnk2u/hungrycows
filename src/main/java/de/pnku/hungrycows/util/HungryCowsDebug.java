package de.pnku.hungrycows.util;

import de.pnku.hungrycows.HungryCows;
import net.minecraft.world.level.Level;

import java.util.Map;

public final class HungryCowsDebug {
    private HungryCowsDebug() {
    }

    private static final Map<String, String> MESSAGES = Map.ofEntries(
            Map.entry("MOOSHROOM_INTERACT", "Player interacted with a mooshroom while holding: %s"),
            Map.entry("MOOSHROOM_VARIANT", "Mooshroom variant: %s"),
            Map.entry("MOOSHROOM_SYNCED_FLOWER", "Mooshroom synced flower stack: %s"),
            Map.entry("MOOSHROOM_STEW_EFFECTS", "Mooshroom stew effects (server-only): %s"),
            Map.entry("MOOSHROOM_FEED_FLOWER", "IF(1): Feeding a brown mooshroom with an edible flower, adding synced flower stack"),
            Map.entry("MOOSHROOM_CLIENT_SYNC_FIX", "Client has a synced suspicious flower but no stewEffects yet desync! -> writing flower effects to the client-side stewEffects."),
            Map.entry("MOOSHROOM_NO_BOWL", "IF(2): Player is not holding a bowl, passing interaction"),
            Map.entry("MOOSHROOM_NOT_MILKABLE", "IF(3): The player is holding a bowl, but the mooshroom is not milkable, passing interaction"),
            Map.entry("MOOSHROOM_BOWL_SERVER", "Bowl interaction on server; hasSyncedSuspicious=%s"),
            Map.entry("EATBLOCK_TEST", "Testing if the blockState %s is edible for %s"),
            Map.entry("EATBLOCK_ATE_FLOWER", "Ate an edible flower as a brown mooshroom"),
            Map.entry("EATBLOCK_ALREADY_EFFECTS", "The Mooshroom already had stew effects, no new effects will be added"),
            Map.entry("EATBLOCK_TRY_EFFECTS", "The Mooshroom did not have any stew effects, trying to get new effects from the eaten flower: %s"),
            Map.entry("EATBLOCK_GOT_EFFECTS", "The eaten flower had stew effects, adding them to the mooshroom"),
            Map.entry("EATBLOCK_TEST_REFLECTION", "Testing if SpellParticleOption class exists to determine which particle to use for applying the stew effects"),
            Map.entry("EATBLOCK_REFLECTION_SUCCESS", "SpellParticleOption class found, using the spell particle for the stew effect application particles"),
            Map.entry("EATBLOCK_REFLECTION_FAIL", "SpellParticleOption class not found, defaulting to using ParticleTypes.EFFECT directly for the stew effect application particles"),
            Map.entry("EATBLOCK_NO_EFFECTS", "The eaten flower did not have any stew effects, no new effects will be added"),
            Map.entry("EATBLOCK_FINISHED", "Finished processing the eaten flower for the brown mooshroom, the resulting saved flower Stack in the mooshroom is: %s")
    );

    public static void log(Level level, String key, Object... args) {
        HungryCows.getLogger().debug(prefix(level) + format(key, args));
    }

    public static void log(String key, Object... args) {
        HungryCows.getLogger().debug(format(key, args));
    }

    private static String format(String key, Object... args) {
        String template = MESSAGES.getOrDefault(key, key);
        return args.length == 0 ? template : String.format(template, args);
    }

    private static String prefix(Level level) {
        return level.isClientSide() ? "(Client): " : "(Server): ";
    }
}
