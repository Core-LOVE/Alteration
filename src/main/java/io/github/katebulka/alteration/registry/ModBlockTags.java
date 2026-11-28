package io.github.katebulka.alteration.registry;

import io.github.katebulka.alteration.AlterationMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
	private static TagKey<Block> register(String name) {
		return TagKey.create(Registries.BLOCK, AlterationMod.id(name));
	}

	public static final TagKey<Block> ITEM_PASS_THROUGH = register("item_pass_through");
	public static final TagKey<Block> REPLACEABLE = register("replaceable");

	public static final TagKey<Block> BUST_DOORS = register("bust_doors");
	public static final TagKey<Block> BUST_TRAPDOORS = register("bust_trapdoors");

	public static final TagKey<Block> SOUNDBLOCKERS = register("soundblockers");
	public static final TagKey<Block> STICKY_CHAINS = register("sticky_chains");

	public static final TagKey<Block> LIGHTNING_FIRE_BASE_BLOCKS = register("lightning_fire_base_blocks");
	public static final TagKey<Block> PREVENT_LIGHTNING_STRIKE_BASE_BLOCKS = register("prevent_lightning_strike_base_blocks");

	public static void register() {

	}
}
