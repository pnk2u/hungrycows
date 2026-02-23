package de.pnku.hungrycows.mixin.entity.ai;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class GoatVersionCompatMixinPlugin implements IMixinConfigPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("Hungry Cows: Goat-Version-Compat-Mixin-Plugin");
    private boolean usesLegacyMixin = false;

    @Override
    public void onLoad(String s) {
        // Get the current Minecraft version and check if it's 1.21.11 or a major above 1
        String mcVersion = FabricLoader.getInstance().getModContainer("minecraft").map(modContainer -> modContainer.getMetadata().getVersion().getFriendlyString()).orElse("unknown");
        LOGGER.info("Detected Minecraft version: {}", mcVersion);
        String[] versionParts = mcVersion.split("\\.");
        if (versionParts.length < 2) {
            LOGGER.warn("Unexpected Minecraft version format: {}. Defaulting to TemptingSensorMixin.", mcVersion);
            return;
        }
        int major = Integer.parseInt(versionParts[0]);
        int minor = Integer.parseInt(versionParts[1]);
        int patch = versionParts.length > 2 ? Integer.parseInt(versionParts[2].split("-")[0]) : 0;
        if (major > 1 || minor > 21 || (minor == 21 && patch >= 11)) {
            LOGGER.info("Minecraft version is 1.21.11 or above. Using TemptingSensorMixin.");
            usesLegacyMixin = false;
        } else {
            LOGGER.info("Minecraft version is below 1.21.11. Using LegacyGoatAiMixin.");
            usesLegacyMixin = true;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("de.pnku.hungrycows.mixin.entity.ai.TemptingSensorMixin")) {
            return !usesLegacyMixin;
        } else if (mixinClassName.equals("de.pnku.hungrycows.mixin.entity.ai.LegacyGoatAiMixin")) {
            return usesLegacyMixin;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
