package io.github.katebulka.alteration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import io.github.katebulka.alteration.annotation.ConfigOption;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ModConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File CONFIG_FILE = ModHelper.getConfigDirectory().resolve("alteration.json").toFile();
	public static ModConfig INSTANCE = new ModConfig();

	//

	@ConfigOption(key = "item_ignore_blocks", category = "gameplay")
	public boolean itemIgnoreBlocks = true;

	@ConfigOption(key = "easy_block_replace", category = "gameplay")
	public boolean easyBlockReplace = true;

	@ConfigOption(key = "fire_immune_items", category = "gameplay")
	public boolean fireImmuneItems = true;

	@ConfigOption(key = "mixed_lights", category = "visual")
	public boolean mixedLights = true;

	@ConfigOption(key = "flat_strength", category = "visual", min = 0f, max = 1f)
	public float flatStrength = 0.75f;

	@ConfigOption(key = "render_empty_air", category = "visual")
	public boolean renderEmptyBubbles = true;

	@ConfigOption(key = "play_ui_bubble_pop", category = "audial")
	public boolean playUiBubblePop = true;

	@ConfigOption(key = "play_ui_bubble_pop_outside", category = "audial")
	public boolean playUiBubblePopOutside = false;

	@ConfigOption(key = "play_heal_sound", category = "audial")
	public boolean playHealSound = true;

	@ConfigOption(key = "hover_text_color_enabled", category = "visual")
	public boolean hoverTextColorEnabled = true;

	@ConfigOption(key = "hover_text_color", category = "visual")
	public Color hoverTextColor = new Color(255, 255, 84);

	//

	public static void load() {
		if (CONFIG_FILE.exists()) {
			try (FileReader reader = new FileReader(CONFIG_FILE)) {
				INSTANCE = GSON.fromJson(reader, ModConfig.class);
				if (INSTANCE == null) {
					INSTANCE = new ModConfig();
				}
			} catch (Exception e) {
				INSTANCE = new ModConfig();
				save();
			}
		} else {
			INSTANCE = new ModConfig();
			save();
		}
	}

	public static void save() {
		try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
			GSON.toJson(INSTANCE, writer);
		} catch (IOException e) {
			AlterationMod.LOGGER.error("Failed to save Alteration config!", e);
		}
	}
}
