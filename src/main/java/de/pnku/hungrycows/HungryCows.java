package de.pnku.hungrycows;

import de.pnku.hungrycows.config.HungryCowsConfigJsonHelper;
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

	@Override
	public void onInitialize() {
		LOGGER.info("Cows are hungry!");
		HungryCowsConfigJsonHelper.init();
	}

	public static ResourceLocation withModId(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}