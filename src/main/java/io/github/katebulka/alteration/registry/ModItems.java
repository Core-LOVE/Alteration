package io.github.katebulka.alteration.registry;

import com.google.common.base.Suppliers;
import io.github.katebulka.alteration.AlterationMod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.function.Supplier;

public class ModItems {
	public static final HashMap<ResourceLocation, Supplier<Item>> MAP = new HashMap<>();

	private static Supplier<Item> registerSimple(String path, Item.Properties properties) {
		ResourceLocation id = AlterationMod.id(path);

		Supplier<Item> supplier = Suppliers.memoize(() -> {
			return new Item(properties);
		});

		MAP.put(id, supplier);
		return supplier;
	}

	private static Supplier<Item> register(String path, Item item) {
		ResourceLocation id = AlterationMod.id(path);

		Supplier<Item> supplier = Suppliers.memoize(() -> {
			return item;
		});

		MAP.put(id, supplier);
		return supplier;
	}

	public static Supplier<Item> registerBlockItem(String path, Block block) {
		return register(path, new BlockItem(block, new Item.Properties()));
	}
}
