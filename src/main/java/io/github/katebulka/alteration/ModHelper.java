package io.github.katebulka.alteration;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//? fabric {

//?} neoforge {
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.ModList;
//?} forge {

//?}

public class ModHelper {
	public static boolean isModLoaded(String modId) {
		//? if fabric {
		/*return FabricLoader.getInstance().isModLoaded(modId);
		 *///?} else {
		return ModList.get().isLoaded(modId);
		//?}
	}

	public static Path getConfigDirectory() {
		//? if fabric {
		/*return FabricLoader.getInstance().getConfigDir();
		 *///?} else {
		return FMLPaths.CONFIGDIR.get();
		//?}
	}

	public static double getSafeFallDistance(Entity self, double d0) {
		if (!ModConfig.INSTANCE.changeGravity) return d0;

		if (self.level().dimension() == Level.OVERWORLD) {
			double y = self.getY();

			double minY = ModConfig.INSTANCE.changeGravityMinY;
			double maxY = ModConfig.INSTANCE.changeGravityMaxY;

			if (maxY <= minY || y <= minY) return d0;

			if (y >= maxY) return d0 * 2.0D;

			double t = (y - minY) / (maxY - minY);

			return d0 * (1.0D + t);
		}

		return d0;
	}

	public static double getGravity(Entity self, double d0) {
		if (!ModConfig.INSTANCE.changeGravity) return d0;

		if (self.level().dimension() == Level.OVERWORLD) {
			double y = self.getY();

			double minY = ModConfig.INSTANCE.changeGravityMinY;
			double maxY = ModConfig.INSTANCE.changeGravityMaxY;
			double gravity = ModConfig.INSTANCE.gravity;

			if (y <= minY) return d0;
			if (y >= maxY) return d0 * gravity;

			double t = (y - minY) / (maxY - minY);

			return d0 * (1.0D - gravity * t);
		}

		return d0;
	}

	public static float getParticleGravity(Particle self, float v) {
		if (!ModConfig.INSTANCE.changeGravity || !ModConfig.INSTANCE.gravityAffectParticles) return v;

		if (Minecraft.getInstance().level.dimension() == Level.OVERWORLD) {
			Vec3 vec = self.getPos();
			double y = vec.y;

			double minY = ModConfig.INSTANCE.changeGravityMinY;
			double maxY = ModConfig.INSTANCE.changeGravityMaxY;
			float gravity = (float) ModConfig.INSTANCE.gravity;

			if (y <= minY) return v;
			if (y >= maxY) return v * gravity;

			double t = (y - minY) / (maxY - minY);

			return (float) ((float) v * (1.0F - gravity * t));
		}

		return (float) v;
	}

	public static int getHoverTextColor(AbstractWidget widget, int c) {
		if (!ModConfig.INSTANCE.hoverTextColorEnabled) return c;

		if (widget.isHoveredOrFocused() && widget.isActive()) {
			return ModConfig.INSTANCE.hoverTextColor.getRGB();
		}

		return c;
	}

	public static class Config {

	}
}
