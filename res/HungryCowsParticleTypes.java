package de.pnku.hungrycows.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import static de.pnku.hungrycows.HungryCows.withModId;

public class HungryCowsParticleTypes {

    public static final SimpleParticleType DRIPPING_MILK = FabricParticleTypes.simple();
    public static final SimpleParticleType FALLING_MILK = FabricParticleTypes.simple();
    public static final SimpleParticleType LANDING_MILK = FabricParticleTypes.simple();
//    public static final SimpleParticleType DRIPPING_MUSHROOM_STEW = FabricParticleTypes.simple();
//    public static final SimpleParticleType FALLING_MUSHROOM_STEW = FabricParticleTypes.simple();
//    public static final SimpleParticleType LANDING_MUSHROOM_STEW = FabricParticleTypes.simple();

    public static void registerParticles() {
        registerParticle(DRIPPING_MILK, "dripping_milk");
        registerParticle(FALLING_MILK, "falling_milk");
        registerParticle(LANDING_MILK, "landing_milk");
//        registerParticle(DRIPPING_MUSHROOM_STEW, "dripping_mushroom_stew");
//        registerParticle(FALLING_MUSHROOM_STEW, "falling_mushroom_stew");
//        registerParticle(LANDING_MUSHROOM_STEW, "landing_mushroom_stew");

        ParticleFactoryRegistry.getInstance().register(DRIPPING_MILK, HungryCowsParticleTypes::createMilkHangParticle);
        ParticleFactoryRegistry.getInstance().register(FALLING_MILK, HungryCowsParticleTypes::createMilkFallParticle);
        ParticleFactoryRegistry.getInstance().register(LANDING_MILK, HungryCowsParticleTypes::createMilkLandParticle);
//        ParticleFactoryRegistry.getInstance().register(DRIPPING_MUSHROOM_STEW, HungryCowsParticleTypes::createMushroomStewHangParticle);
//        ParticleFactoryRegistry.getInstance().register(FALLING_MUSHROOM_STEW, HungryCowsParticleTypes::createMushroomStewFallParticle);
//        ParticleFactoryRegistry.getInstance().register(LANDING_MUSHROOM_STEW, HungryCowsParticleTypes::createMushroomStewLandParticle);
    }

    private static void registerParticle(SimpleParticleType simpleParticleType, String name) {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, withModId(name), simpleParticleType);
    }

    public static TextureSheetParticle createMilkHangParticle(
            SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
    ) {
        HungryCowsDripParticle.HungryCowsDripHangParticle dripHangParticle = new HungryCowsDripParticle.HungryCowsDripHangParticle(level, x, y, z, Fluids.EMPTY, FALLING_MILK);
        dripHangParticle.gravity *= 0.01F;
        dripHangParticle.setLifetime(100);
        dripHangParticle.setColor(0.51171875F, 0.03125F, 0.890625F);
        return dripHangParticle;
    }

    public static TextureSheetParticle createMilkFallParticle(
            SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
    ) {
        HungryCowsDripParticle dripParticle = new HungryCowsDripParticle.HungryCowsFallAndLandParticle(level, x, y, z, Fluids.EMPTY, LANDING_MILK);
        dripParticle.isGlowing = true;
        dripParticle.gravity = 0.01F;
        dripParticle.setColor(0.51171875F, 0.03125F, 0.890625F);
        return dripParticle;
    }

    public static TextureSheetParticle createMilkLandParticle(
            SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
    ) {
        HungryCowsDripParticle dripParticle = new HungryCowsDripParticle.HungryCowsDripLandParticle(level, x, y, z, Fluids.EMPTY);
        dripParticle.isGlowing = true;
        dripParticle.setLifetime((int) (28.0 / (Math.random() * 0.8 + 0.2)));
        dripParticle.setColor(0.51171875F, 0.03125F, 0.890625F);
        return dripParticle;
    }

    @Environment(EnvType.CLIENT)
    public static class HungryCowsDripParticle extends TextureSheetParticle {
        private final Fluid type;
        protected boolean isGlowing;
        protected double gravity;

        HungryCowsDripParticle(ClientLevel level, double x, double y, double z, Fluid type) {
            super(level, x, y, z);
            this.setSize(0.01F, 0.01F);
            this.gravity = 0.06F;
            this.type = type;
        }

        protected Fluid getType() {
            return this.type;
        }

        @Override
        public @NotNull ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
        }

        @Override
        public int getLightColor(float partialTick) {
            return this.isGlowing ? 240 : super.getLightColor(partialTick);
        }

        @Override
        public void tick() {
            this.xo = this.x;
            this.yo = this.y;
            this.zo = this.z;
            this.preMoveUpdate();
            if (!this.removed) {
                this.yd = this.yd - (double) this.gravity;
                this.move(this.xd, this.yd, this.zd);
                this.postMoveUpdate();
                if (!this.removed) {
                    this.xd *= 0.98F;
                    this.yd *= 0.98F;
                    this.zd *= 0.98F;
                    if (this.type != Fluids.EMPTY) {
                        BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);
                        FluidState fluidState = this.level.getFluidState(blockPos);
                        if (fluidState.getType() == this.type && this.y < (double) ((float) blockPos.getY() + fluidState.getHeight(this.level, blockPos))) {
                            this.remove();
                        }
                    }
                }
            }
        }

        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
            }
        }

        protected void postMoveUpdate() {
        }

        static class HungryCowsDripHangParticle extends HungryCowsDripParticle {
            private final ParticleOptions fallingParticle;


            HungryCowsDripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle) {
                super(level, x, y, z, type);
                this.fallingParticle = fallingParticle;
                this.gravity *= 0.02F;
                this.setLifetime(40);
            }

            @Override
            protected void preMoveUpdate() {
                if (this.lifetime-- <= 0) {
                    this.remove();
                    this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
                }
            }

            @Override
            protected void postMoveUpdate() {
                this.xd *= 0.02;
                this.yd *= 0.02;
                this.zd *= 0.02;
            }
        }

        @Environment(EnvType.CLIENT)
        static class  HungryCowsFallAndLandParticle extends HungryCowsFallingParticle {
            protected final ParticleOptions landParticle;

            HungryCowsFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle) {
                super(level, x, y, z, type);
                this.landParticle = landParticle;
            }

            @Override
            protected void postMoveUpdate() {
                if (this.onGround) {
                    this.remove();
                    this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
                }
            }
        }

        @Environment(EnvType.CLIENT)
        static class HungryCowsFallingParticle extends HungryCowsDripParticle {
            HungryCowsFallingParticle(ClientLevel level, double x, double y, double z, Fluid type) {
                this(level, x, y, z, type, (int)(64.0 / (Math.random() * 0.8 + 0.2)));
            }

            HungryCowsFallingParticle(ClientLevel level, double x, double y, double z, Fluid type, int lifetime) {
                super(level, x, y, z, type);
                this.lifetime = lifetime;
            }

            @Override
            protected void postMoveUpdate() {
                if (this.onGround) {
                    this.remove();
                }
            }
        }

        @Environment(EnvType.CLIENT)
        static class HungryCowsDripLandParticle extends HungryCowsDripParticle {
            HungryCowsDripLandParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid) {
                super(clientLevel, d, e, f, fluid);
                this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
            }
        }
    }
}
