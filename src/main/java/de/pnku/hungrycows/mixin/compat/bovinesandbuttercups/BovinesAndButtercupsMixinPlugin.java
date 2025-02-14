package de.pnku.hungrycows.mixin.compat.bovinesandbuttercups;

import de.pnku.hungrycows.HungryCows;
import de.pnku.hungrycows.util.HungryCowsCompatibilityHelper;
import house.greenhouse.bovinesandbuttercups.content.entity.BovinesEntityTypes;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static de.pnku.hungrycows.util.HungryCowsCompatibilityHelper.isBnBLoaded;

public class BovinesAndButtercupsMixinPlugin  implements IMixinConfigPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("bovinesandbuttercupsmixinplugin");

    @Override
    public void onLoad(String mixinPackage) {
        if (FabricLoader.getInstance().isModLoaded("bovinesandbuttercups")) {
            isBnBLoaded = true;
            LOGGER.info("Moos are blooming... and hungry!");
        }
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return isBnBLoaded;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}