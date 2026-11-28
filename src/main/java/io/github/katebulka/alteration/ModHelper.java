package io.github.katebulka.alteration;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//? fabric {

//?} neoforge {
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
//?} forge {

//?}

public class ModHelper {
	public static boolean isModLoaded(String modId) {
		//? if fabric {
		/*return FabricLoader.getInstance().isModLoaded(modId);
		 *///?} else {
		return FMLLoader.getLoadingModList().getModFileById(modId) != null;
		//?}
	}

	public static Path getConfigDirectory() {
		//? if fabric {
		/*return FabricLoader.getInstance().getConfigDir();
		 *///?} else {
		return FMLPaths.CONFIGDIR.get();
		//?}
	}

	public static boolean isShifting() {
		Minecraft minecraft = Minecraft.getInstance();

		if (!minecraft.isSameThread()) {
			return false;
		}

		//? if >=1.21.9 {
		/*return minecraft.hasShiftDown();
		 *///?} else {
		return Screen.hasShiftDown();
		//?}
	}

	public static boolean getChained(Level level, BlockPos pos, BlockState state) {
		if (!ModConfig.INSTANCE.chainsSuspendFalling) return false;

		for (Direction dir : Direction.values()) {
			BlockPos neighborPos = pos.relative(dir);
			BlockState neighbor = level.getBlockState(neighborPos);

			if (!neighbor.is(ModBlockTags.STICKY_CHAINS) || !neighbor.hasProperty(BlockStateProperties.AXIS)) {
				continue;
			}

			Direction.Axis axis = neighbor.getValue(BlockStateProperties.AXIS);

			if (axis == Direction.Axis.Y && dir.getAxis() == Direction.Axis.Y) {
				return true;
			}

			if (axis != Direction.Axis.Y && dir.getAxis() == axis) {
				return true;
			}
		}

		return false;
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

	private static final ResourceLocation TIME_ICON = ResourceLocation.fromNamespaceAndPath(AlterationMod.MOD_ID, "textures/gui/sprites/time_icon.png");

	public static @Nullable Component getPlayTimeComponent(int ticks) {
		if (ticks <= 0) {
			return null;
		}

		double hours = (ticks / 20.0) / 3600.0;

		return Component.translatable("worldplaytime.format", Component.literal(hours >= 100.0 ? String.valueOf((int)hours) : String.format(Locale.US, "%.1f", hours)));
	}

	public static int getWholePlayTimeWidth(int ticks) {
		Component component = getPlayTimeComponent(ticks);

		if (component == null) {
			return 0;
		}

		Minecraft minecraft = Minecraft.getInstance();

		return minecraft.font.width(component) + 11;
	}

	public static void renderPlayTime(GuiGraphics guiGraphics, int x, int y, int playTimeTicks, Color color) {
		Minecraft minecraft = Minecraft.getInstance();
		Component component = getPlayTimeComponent(playTimeTicks);

		if (component == null) {
			return;
		}

		guiGraphics.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		guiGraphics.blit(TIME_ICON, x, y, 0.f, 0.f, 9, 9, 9, 9);
		guiGraphics.setColor(1.f, 1.f, 1.f, 1.f);
		guiGraphics.drawString(minecraft.font, component, x + 11, y + 1, color.getRGB(), false);
	}

	public static class Config {

	}
}
