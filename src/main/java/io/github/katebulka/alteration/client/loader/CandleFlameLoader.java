package io.github.katebulka.alteration.client.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.katebulka.alteration.AlterationMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CandleFlameLoader implements PreparableReloadListener {
	private static final ResourceLocation ID = AlterationMod.id("candles/flames.json");
	private static Map<ResourceLocation, ResourceLocation> map = Map.of();

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller prepProfiler, ProfilerFiller reloadProfiler, Executor bgExecutor, Executor gameExecutor) {
		CompletableFuture<JsonObject> prepare = CompletableFuture.supplyAsync(() -> {
			Optional<Resource> resource = manager.getResource(ID);

			if (resource.isEmpty()) return null;

			try (InputStreamReader reader = new InputStreamReader(resource.get().open())) {
				return JsonParser.parseReader(reader).getAsJsonObject();
			} catch (Exception e) {
				AlterationMod.LOGGER.error("Failed {}", ID, e);
				return null;
			}
		}, bgExecutor);

		return prepare.thenCompose(barrier::wait).thenAcceptAsync(json -> {
			Map<ResourceLocation, ResourceLocation> result = new HashMap<>();

			if (json != null) {
				mergeInto(result, json);
			}

			map = Map.copyOf(result);
		}, gameExecutor);
	}

	private static void mergeInto(Map<ResourceLocation, ResourceLocation> target, JsonObject json) {
		boolean replace = json.has("replace") && json.get("replace").getAsBoolean();
		if (replace) target.clear();

		if (!json.has("values")) return;

		for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("values").entrySet()) {
			String raw = entry.getValue().getAsString();
			ResourceLocation id = ResourceLocation.tryParse(raw);

			if (id == null) {
				AlterationMod.LOGGER.warn("Invalid flame {} for {}", raw, entry.getKey());
				continue;
			}

			target.put(ResourceLocation.tryParse(entry.getKey()), id);
		}
	}

	public static Map<ResourceLocation, ResourceLocation> getMapping() {
		return map;
	}

	public static ResourceLocation getFlame(ResourceLocation blockTagId) {
		return map.get(blockTagId);
	}

	public static ResourceLocation getFlameBlock(BlockState state) {
		Block block = state.getBlock();

		for (TagKey<Block> tag : block.builtInRegistryHolder().tags().toList()) {
			ResourceLocation flame = CandleFlameLoader.getFlame(tag.location());
			if (flame != null) return flame;
		}

		return null;
	}
}
