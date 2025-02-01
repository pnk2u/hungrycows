package de.pnku.hungrycows;

import de.pnku.hungrycows.config.HungryCowsOwoConfig;
import de.pnku.hungrycows.config.HungryCowsLegacyConfigJsonHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HungryCows implements ModInitializer {
	public static final String MOD_ID = "hungrycows";
    public static final Logger LOGGER = LoggerFactory.getLogger("hungrycows");
	public static EntityDataAccessor<Byte> IS_MILKED;
	public static EntityDataAccessor<Byte> IS_MILKED_MOOSHROOM;
	public static EntityDataAccessor<Integer> FED_TIMER;
	public static EntityDataAccessor<Integer> FED_TIMER_SHEEP;
	public static EntityDataAccessor<Boolean> UNMILK_FLAG;
	public static EntityDataAccessor<Boolean> UNMILK_MOOSHROOM_FLAG;
	public static EntityDataAccessor<Boolean> UNSHEAR_FLAG;
	public static final HungryCowsOwoConfig CONFIG = HungryCowsOwoConfig.createAndLoad();
	public static final HungryCowsOwoConfig.MilkabilitySettings_ milkabilitySettings = CONFIG.milkabilitySettings;
	public static final HungryCowsOwoConfig.BlockEatSettings_ blockEatSettings = CONFIG.blockEatSettings;
	public static final HungryCowsOwoConfig.SheepSettings_ sheepSettings = CONFIG.sheepSettings;

	@Override
	public void onInitialize() {
		LOGGER.info("Cows are hungry!");
		HungryCowsLegacyConfigJsonHelper.init();
		CONFIG.load(); // Reload after migrating legacy config to new config
	}

	public static ResourceLocation withModId(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}