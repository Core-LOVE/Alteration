package io.github.katebulka.alteration.mixin.client;

import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.CandleBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(
		value = AbstractCandleBlock.class
)
public class CandleBlockMixin {
}
