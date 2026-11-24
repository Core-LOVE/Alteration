package io.github.katebulka.alteration.platform.neoforge;

//? neoforge {

import io.github.katebulka.alteration.AlterationMod;
import io.github.katebulka.alteration.registry.ModBlocks;
import io.github.katebulka.alteration.registry.ModItems;
import io.github.katebulka.alteration.registry.ModSoundEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import io.github.katebulka.alteration.registry.ModRegistry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod(AlterationMod.MOD_ID)
public class NeoforgeEntrypoint {
	private static <T> void registerBus(IEventBus modEventBus, Registry registries, HashMap<ResourceLocation, Supplier<T>> map) {
		Map<String, DeferredRegister> ARR = new HashMap<>();

		map.forEach((id, supplier) -> {
			if (!ARR.containsKey(id.getNamespace())) {
				ARR.put(id.getNamespace(), DeferredRegister.create(registries, id.getNamespace()));
			}

			ARR.get(id.getNamespace()).register(id.getPath(), supplier);
		});

		ARR.forEach((namespace, reg) -> {
			reg.register(modEventBus);
		});
	}

	public static void register(IEventBus modEventBus) {
		registerBus(modEventBus, BuiltInRegistries.SOUND_EVENT, ModSoundEvents.MAP);
		registerBus(modEventBus, BuiltInRegistries.BLOCK, ModBlocks.MAP);
		registerBus(modEventBus, BuiltInRegistries.ITEM, ModItems.MAP);
	}

	public NeoforgeEntrypoint(IEventBus modEventBus, ModContainer modContainer) throws IOException {
		AlterationMod.onInitialize();
	}
}
//?}
