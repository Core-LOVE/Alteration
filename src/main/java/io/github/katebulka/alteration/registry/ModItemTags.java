package io.github.katebulka.alteration.registry;

import io.github.katebulka.alteration.AlterationMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModItemTags {
	private static TagKey<Item> register(String name) {
		return TagKey.create(Registries.ITEM, AlterationMod.id(name));
	}

	public static final TagKey<Item> FIRELESS = register("fireless");

	public static void register() {

	}
}
