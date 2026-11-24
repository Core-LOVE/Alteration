package io.github.katebulka.alteration;
import net.neoforged.fml.ModList;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//? fabric {

//?} neoforge {
import net.neoforged.fml.loading.FMLPaths;
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
}
