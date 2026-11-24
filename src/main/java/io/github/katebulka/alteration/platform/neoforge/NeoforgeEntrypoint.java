package io.github.katebulka.alteration.platform.neoforge;

//? neoforge {

import io.github.katebulka.alteration.AlterationMod;
import io.github.katebulka.alteration.registry.ModSoundEvents;
import net.minecraft.core.registries.BuiltInRegistries;
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

@Mod(AlterationMod.MOD_ID)
public class NeoforgeEntrypoint {
	static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, AlterationMod.MOD_ID);

	public static void register(IEventBus modEventBus) {
		final Map<String, DeferredRegister> SOUNDS = new HashMap<>();

		ModSoundEvents.MAP.forEach((id, supplier) -> {
			if (!SOUNDS.containsKey(id.getNamespace())) {
				SOUNDS.put(id.getNamespace(), DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, id.getNamespace()));
			}

			SOUNDS.get(id.getNamespace()).register(id.getPath(), supplier);
		});

		SOUNDS.forEach((namespace, reg) -> {
			reg.register(modEventBus);
		});
	}

	public NeoforgeEntrypoint(IEventBus modEventBus, ModContainer modContainer) throws IOException {
		AlterationMod.onInitialize();
	}
}
//?}
