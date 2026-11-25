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

	public static void register() {

	}
}
