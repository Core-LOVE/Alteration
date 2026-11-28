package io.github.katebulka.alteration.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(
		value = BeehiveBlockEntity.class
)
public abstract class BeehiveBlockEntityMixin extends BlockEntity {
	public BeehiveBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
		super(p_155228_, p_155229_, p_155230_);
	}
}
