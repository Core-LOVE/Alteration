package io.github.katebulka.alteration.mixin.cnc;

import com.teamabnormals.caverns_and_chasms.common.block.GunpowderBlock;
import io.github.katebulka.alteration.ModConfig;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SugarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// https://modrinth.com/mod/charm
@Mixin(
		value = GunpowderBlock.class
)
public abstract class GunpowderBlockMixin {
	@Inject(method = "neighborChanged", at = @At("HEAD"), cancellable = true)
	private void alteration$neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean isMoving, CallbackInfo cl) {
		if (ModConfig.INSTANCE.gunpowderBlockReaction == ModConfig.GunpowderBlockInteraction.NONE) return;

		if (!alteration$tryTouchLava(level, pos, state)) {
			return;
		} else {
			cl.cancel();
		}
	}

	@Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
	private void alteration$onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo cl) {
		if (ModConfig.INSTANCE.gunpowderBlockReaction == ModConfig.GunpowderBlockInteraction.NONE) return;

		if (!alteration$tryTouchLava(level, pos, state)) {
			return;
		} else {
			cl.cancel();
		}
	}

	@Unique
	private boolean alteration$tryTouchLava(Level level, BlockPos pos, BlockState state) {
		boolean lavaBelow = false;

		for (var facing : Direction.values()) {
			if (facing != Direction.DOWN) {
				BlockPos below = pos.relative(facing);

				if (level.getBlockState(below).getFluidState().is(FluidTags.LAVA)) {
					lavaBelow = true;
					break;
				}
			}
		}

		if (ModConfig.INSTANCE.gunpowderBlockReaction == ModConfig.GunpowderBlockInteraction.NONE) {
			lavaBelow = false;
		}

		if (lavaBelow) {
			if (ModConfig.INSTANCE.gunpowderBlockReaction == ModConfig.GunpowderBlockInteraction.EXPLODE) {
				GunpowderBlock.explode(level, pos);
			} else {
				level.globalLevelEvent(2001, pos, net.minecraft.world.level.block.Block.getId(level.getBlockState(pos)));
				level.destroyBlock(pos, false);
			}
		}

		return lavaBelow;
	}
}
