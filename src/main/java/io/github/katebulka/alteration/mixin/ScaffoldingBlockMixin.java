package io.github.katebulka.alteration.mixin;

import io.github.katebulka.alteration.ModHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = ScaffoldingBlock.class
)
public abstract class ScaffoldingBlockMixin {
	@Inject(method = "tick(Lnet/minecraft/entity/player/BlockState;Lnet/minecraft/entity/player/ServerLevel;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/RandomSource;)V",
			at = @At("HEAD"), cancellable = true)
	private void alteration$tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (ModHelper.getChained(level, pos, state)) {
			ci.cancel();
		}
	}
}
