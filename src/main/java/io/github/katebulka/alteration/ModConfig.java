package io.github.katebulka.alteration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.katebulka.alteration.annotation.ConfigOption;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ModConfig {
	public static class Keys {
		@ConfigOption(key = "mixedLights", category = "visual")
		public boolean mixedLights = true;

		@ConfigOption(key = "flat_strength", category = "visual", min = 0f, max = 1f)
		public float flatStrength = 0.75f;

		@ConfigOption(key = "renderEmptyBubbles", category = "visual")
		public boolean renderEmptyBubbles = true;

		@ConfigOption(key = "playUiBubblePop", category = "audial")
		public boolean playUiBubblePop = true;

		@ConfigOption(key = "playUiBubblePopOutside", category = "audial")
		public boolean playUiBubblePopOutside = false;

		@ConfigOption(key = "playHealSound", category = "audial")
		public boolean playHealSound = true;
	}

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File CONFIG_FILE = ModHelper.getConfigDirectory().resolve("alteration.json").toFile();
	public static Keys KEYS = new Keys();

	public static void load() {
		if (CONFIG_FILE.exists()) {
			try (FileReader reader = new FileReader(CONFIG_FILE)) {
				KEYS = GSON.fromJson(reader, Keys.class);
				if (KEYS == null) {
					KEYS = new Keys();
				}
			} catch (Exception e) {
				KEYS = new Keys();
				save();
			}
		} else {
			KEYS = new Keys();
			save();
		}
	}

	public static void save() {
		try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
			GSON.toJson(KEYS, writer);
		} catch (IOException e) {
			AlterationMod.LOGGER.error("Failed to save Shadow Drop config!", e);
		}
	}
}
