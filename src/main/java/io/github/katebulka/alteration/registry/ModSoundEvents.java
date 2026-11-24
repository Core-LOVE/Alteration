package io.github.katebulka.alteration.registry;

import com.google.common.base.Suppliers;
import io.github.katebulka.alteration.AlterationMod;
import io.github.katebulka.alteration.ModHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.HashMap;
import java.util.function.Supplier;

public class ModSoundEvents {
	public static final HashMap<ResourceLocation, Supplier<SoundEvent>> MAP = new HashMap<>();

	private static Supplier<SoundEvent> register(String namespace, String path) {
		ResourceLocation id = AlterationMod.id(namespace, path);

		Supplier<SoundEvent> supplier = Suppliers.memoize(() -> {
			return SoundEvent.createVariableRangeEvent(id);
		});

		MAP.put(id, supplier);
		return supplier;
	}

	public static final Supplier<SoundEvent> BUBBLE_POP = register("minecraft", "ui.hud.bubble_pop");
	public static final Supplier<SoundEvent> HP_HEAL = register("alteration", "ui.hud.hp_heal");
}
