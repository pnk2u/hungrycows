package de.pnku.hungrycows;

import de.pnku.hungrycows.config.HungryCowsLegacyConfigJsonHelper;
import de.pnku.hungrycows.item.HungryCowsDispenseItemBehaviors;
import de.pnku.hungrycows.jade.MilkabilityUIItems;
import de.pnku.hungrycows.sound.HungryCowsSoundEvents;
import de.pnku.hungrycows.util.HungryCowsCompatibilityHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.pnku.hungrycows.config.HungryCowsConfigHelper.CONFIG;

public class HungryCows implements ModInitializer {
	public static final String MOD_ID = "hungrycows";
	public static EntityDataAccessor<Boolean> IS_MILKED;
	public static EntityDataAccessor<Boolean> IS_MILKED_MOOSHROOM;
	public static EntityDataAccessor<Boolean> IS_MILKED_GOAT;
	public static EntityDataAccessor<Integer> FED_TIMER;
	public static EntityDataAccessor<Integer> FED_TIMER_SHEEP;
	public static EntityDataAccessor<Integer> FED_TIMER_GOAT;

	@Override
	public void onInitialize() {
		getLogger().info("Cows are hungry!");
		HungryCowsLegacyConfigJsonHelper.init();
		CONFIG.load(); // Reload after migrating legacy config to new config
		HungryCowsDispenseItemBehaviors.registerBucketBehavior();
		MilkabilityUIItems.initUISpriteItem();
		HungryCowsCompatibilityHelper.init();
		HungryCowsSoundEvents.registerSoundEvents();
	}

	public static ResourceLocation withModId(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	public static Logger getLogger() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			return LoggerFactory.getLogger(HungryCows.class);
		} else {
			return LoggerFactory.getLogger("Hungry Cows");
		}
	}

}