package io.github.katebulka.alteration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.registry.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(
		value = PistonStructureResolver.class
)
public abstract class PistonStructureResolverMixin {
	@Shadow @Final private Direction pushDirection;

	@Unique
	private boolean alteration$canConnect(BlockState chainState, BlockState otherState, Direction stepDir) {
		if (!chainState.is(ModBlockTags.STICKY_CHAINS) || !chainState.hasProperty(ChainBlock.AXIS)) return false;

		Direction.Axis axis = chainState.getValue(ChainBlock.AXIS);

		if (stepDir.getAxis() != axis) return false;

		if (otherState.hasProperty(ChainBlock.AXIS)) return otherState.getValue(ChainBlock.AXIS) == axis;

		return true;
	}

	@WrapOperation(method = "addBlockLine",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isStickyBlock()Z"))
	private boolean alteration$stickyChainBlock(BlockState state, Operation<Boolean> original) {
		boolean og = original.call(state);

		if (!ModConfig.INSTANCE.stickyChains) {
			return og;
		}

		return og || state.is(ModBlockTags.STICKY_CHAINS);
	}

	@WrapOperation(method = "addBlockLine",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canStickTo(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean alteration$stickiness(BlockState self, BlockState other, Operation<Boolean> original, BlockPos startPos, Direction stepDir) {
		if (original.call(self, other)) return true;
		if (!ModConfig.INSTANCE.stickyChains) return false;

		Direction dir = (stepDir != null) ? stepDir : this.pushDirection.getOpposite();
		return alteration$canConnect(self, other, dir) || alteration$canConnect(other, self, dir.getOpposite());
	}

	@WrapOperation(method = "addBranchingBlocks",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canStickTo(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean alteration$branch(BlockState self, BlockState other, Operation<Boolean> original, BlockPos originPos, @Local Direction branchDir) {
		if (original.call(self, other)) return true;
		if (!ModConfig.INSTANCE.stickyChains) return false;

		return alteration$canConnect(self, other, branchDir) || alteration$canConnect(other, self, branchDir.getOpposite());
	}
}
