package io.github.katebulka.alteration;
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
