package io.github.katebulka.alteration.mixin;

import io.github.katebulka.alteration.ModHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = PointedDripstoneBlock.class
)
public abstract class PointedDripstoneBlockMixin extends Block {
	public PointedDripstoneBlockMixin(Properties p_49795_) {
		super(p_49795_);
	}

	@Inject(method = "spawnFallingStalactite", at = @At("HEAD"), cancellable = true)
	private static void alteration$spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos, CallbackInfo ci) {
		if (ModHelper.getChained(level, pos, state)) {
			ci.cancel();
		}
	}
}
