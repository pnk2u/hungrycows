package de.pnku.hungrycows;

import de.pnku.hungrycows.util.HungryCowsCompatibilityHelper;
import net.fabricmc.api.ClientModInitializer;

public class HungryCowsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HungryCowsCompatibilityHelper.init();
        HungryCowsCompatibilityHelper.clientInit();
    }
}
