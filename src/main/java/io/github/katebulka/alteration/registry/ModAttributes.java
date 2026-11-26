package io.github.katebulka.alteration.registry;

import com.google.common.base.Suppliers;
import io.github.katebulka.alteration.AlterationMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

public class ModAttributes {
	public static final HashMap<ResourceLocation, Supplier<Attribute>> MAP = new HashMap<>();

	@Nullable
	public static Holder<Attribute> IMPACT_RESISTANCE_HOLDER;

	private static Supplier<Attribute> register(String path, Attribute attribute) {
		ResourceLocation id = AlterationMod.id(path);
		Supplier<Attribute> supplier = Suppliers.memoize(() -> attribute);
		MAP.put(id, supplier);
		return supplier;
	}

//	public static final Supplier<Attribute> IMPACT_RESISTANCE = register(
//			"impact_resistance",
//			new RangedAttribute("attribute.alteration.impact_resistance", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE)
//					.setSyncable(true)
//	);
}
