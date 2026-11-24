package io.github.katebulka.alteration.registry;

import com.google.common.base.Suppliers;
import io.github.katebulka.alteration.AlterationMod;
import io.github.katebulka.alteration.content.block.SupportBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.function.Supplier;

public class ModBlocks {
	public static final HashMap<ResourceLocation, Supplier<Block>> MAP = new HashMap<>();

	private static Supplier<Block> registerSimple(String path, BlockBehaviour.Properties properties) {
		ResourceLocation id = AlterationMod.id(path);

		Supplier<Block> supplier = Suppliers.memoize(() -> {
			return new Block(properties);
		});

		ModItems.registerBlockItem(path, supplier.get());

		MAP.put(id, supplier);
		return supplier;
	}

	private static Supplier<Block> register(String path, Block block) {
		ResourceLocation id = AlterationMod.id(path);

		Supplier<Block> supplier = Suppliers.memoize(() -> {
			return block;
		});

		ModItems.registerBlockItem(path, supplier.get());

		MAP.put(id, supplier);
		return supplier;
	}

	public static final Supplier<Block> SUPPORT_BLOCK = register("support", new SupportBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.SCAFFOLDING)
	));
}
