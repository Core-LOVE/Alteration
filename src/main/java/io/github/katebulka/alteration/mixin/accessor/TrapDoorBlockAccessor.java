package io.github.katebulka.alteration.mixin.accessor;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = TrapDoorBlock.class
)
public interface TrapDoorBlockAccessor {
	@Accessor("type")
	BlockSetType alteration$getType();
}
