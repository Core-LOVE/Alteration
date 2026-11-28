package io.github.katebulka.alteration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.github.katebulka.alteration.annotation.ConfigOption;
import org.jetbrains.annotations.NotNull;

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
	private static final TypeAdapter<Color> COLOR_ADAPTER = new TypeAdapter<>() {
		@Override
		public void write(JsonWriter out, Color value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}

			out.value(value.getRGB());
		}

		@Override
		public Color read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}

			return new Color(in.nextInt(), true);
		}
	};

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(Color.class, COLOR_ADAPTER)
			.create();

	private static final File CONFIG_FILE = ModHelper.getConfigDirectory().resolve("alteration.json").toFile();
	public static ModConfig INSTANCE = new ModConfig();

	public enum ServerEntryRenderPos {
		AFTER_NAME,
		BEHIND_COUNT,
		LEFT,
		RIGHT
	}

	public enum WorldEntryRenderPos {
		TOP_RIGHT,
		LEFT,
		RIGHT
	}

	public enum GunpowderBlockInteraction {
		NONE,
		DESTROY,
		EXPLODE
	}

	//

	@ConfigOption(key = "burst_door", category = "gameplay")
	public boolean burstDoor = true;

	@ConfigOption(key = "close_doors", category = "gameplay")
	public boolean closeDoors = true;

	@ConfigOption(key = "affect_trapdoors", category = "gameplay")
	public boolean affectTrapdoors = true;

	@ConfigOption(key = "affect_nearby_doors", category = "gameplay")
	public boolean affectNearbyDoors = true;

	@ConfigOption(key = "change_gravity", category = "gameplay")
	public boolean changeGravity = true;

	@ConfigOption(key = "change_gravity_min_y", category = "gameplay", min = -9999F, max = 9999F)
	public int changeGravityMinY = 256;

	@ConfigOption(key = "change_gravity_max_y", category = "gameplay", min = -9999F, max = 9999F)
	public int changeGravityMaxY = 320;

	@ConfigOption(key = "gravity", category = "gameplay", min = 0f, max = 1f)
	public double gravity = 0.3D;

	@ConfigOption(key = "affect_particles", category = "visual")
	public boolean gravityAffectParticles = true;

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

	@ConfigOption(key = "better_trim_tooltip", category = "visual")
	public boolean betterTrimTooltip = true;

	@ConfigOption(key = "banner_tooltip", category = "visual")
	public boolean bannerTooltip = true;

	@ConfigOption(key = "book_tooltip", category = "visual")
	public boolean bookTooltip = true;

	@ConfigOption(key = "book_tooltip_lines", category = "visual", min = 1f, max = 14f)
	public int bookTooltipLines = 7;

	@ConfigOption(key = "tooltip_can_expand", category = "visual")
	public boolean tooltipCanExpand = true;

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

	@ConfigOption(key = "sound_proof", category = "audial")
	public boolean soundProof = true;

	@ConfigOption(key = "sound_proof_distance", category = "audial", min = 0F, max = 1F)
	public float soundProofDistance = 1.0F;

	@ConfigOption(key = "sound_proof_interval", category = "audial", min = 0F, max = 1F)
	public float soundProofInterval = 0.5F;

	@ConfigOption(key = "ao_falling_blocks", category = "visual")
	public boolean aoFallingBlocks = true;

	@ConfigOption(key = "sticky_chains", category = "gameplay")
	public boolean stickyChains = true;

//	@ConfigOption(key = "play_time", category = "visual")
//	public boolean playTime = true;
//
//	@ConfigOption(key = "play_time_color", category = "visual")
//	public Color playTimeColor = new Color(128, 128, 128);

//	@ConfigOption(key = "world_play_time_position", category = "visual")
//	public WorldEntryRenderPos worldPlayTimePosition = WorldEntryRenderPos.TOP_RIGHT;

//	@ConfigOption(key = "server_play_time_position", category = "visual")
//	public ServerEntryRenderPos serverPlayTimePosition = ServerEntryRenderPos.AFTER_NAME;

	@ConfigOption(key = "chains_suspend_falling", category = "gameplay")
	public boolean chainsSuspendFalling = true;

	@ConfigOption(key = "darker_ao", category = "visual")
	public boolean darkerAmbientOcclusion = true;

	@ConfigOption(key = "darker_water", category = "visual")
	public boolean darkerWater = true;

	@ConfigOption(key = "shady_faces", category = "visual")
	public boolean shadyFaces = true;

	@ConfigOption(key = "gunpowder_block_reaction", category = "integration")
	public GunpowderBlockInteraction gunpowderBlockReaction = GunpowderBlockInteraction.DESTROY;

	@ConfigOption(key = "stripped_bonemeal", category = "gameplay")
	public boolean strippedLogBonemeal = true;

	@ConfigOption(key = "lightning_prevention", category = "gameplay")
	public boolean lightningPrevention = true;

//	@ConfigOption(key = "gunpowder_interaction", category = "interaction")
//	public enum gunpowderInteraction {
//
//	}

//	@ConfigOption(key = "chainmailed", category = "gameplay")
//	public boolean chainmailed = true;
//
//	@ConfigOption(key = "chainmailed_sounds", category = "gameplay")
//	public boolean chainmailedSounds = true;

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
