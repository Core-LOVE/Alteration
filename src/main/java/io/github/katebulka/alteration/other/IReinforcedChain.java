package io.github.katebulka.alteration.other;

import net.minecraft.nbt.CompoundTag;

public interface IReinforcedChain {
	default boolean hasChainmailed(CompoundTag tag) {
		return tag.contains("reinforcedChain", CompoundTag.TAG_COMPOUND);
	}
}
