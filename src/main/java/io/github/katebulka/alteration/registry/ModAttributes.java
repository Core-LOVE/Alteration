package io.github.katebulka.alteration.registry;

import com.google.common.base.Suppliers;
import io.github.katebulka.alteration.AlterationMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class ModAttributes {
	public static final HashMap<ResourceLocation, Supplier<Attribute>> MAP = new HashMap<>();
}
