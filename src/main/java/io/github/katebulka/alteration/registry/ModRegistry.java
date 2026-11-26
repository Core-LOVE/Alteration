package io.github.katebulka.alteration.registry;

import io.github.katebulka.alteration.AlterationMod;
import io.github.katebulka.alteration.ModHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

//? fabric {

//?} neoforge {
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
//?} forge {

//?}

public class ModRegistry {
	public static void initializeFabric() {
		ModSoundEvents.MAP.forEach((id, supplier) -> {
			Registry.register(BuiltInRegistries.SOUND_EVENT, id, supplier.get());
		});

		ModAttributes.MAP.forEach((id, supplier) -> {
			Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, id, supplier.get());
		});
	}
}
