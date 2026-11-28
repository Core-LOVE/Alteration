package io.github.katebulka.alteration.mixin;

import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.mixin.accessor.AxeItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

// https://modrinth.com/mod/revertbark
@Mixin(
		value = BoneMealItem.class
)
public abstract class BonemealItemMixin {
	@Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
	private void alteration$useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> info) {
		if (!ModConfig.INSTANCE.strippedLogBonemeal) return;

		Level level = useOnContext.getLevel();
		BlockPos pos = useOnContext.getClickedPos();
		BlockState state = level.getBlockState(pos);
		Block clickedBlock = state.getBlock();

		Block block = null;

		for (Map.Entry<Block, Block> entry : AxeItemAccessor.getStrippables().entrySet()) {
			if (entry.getValue() == clickedBlock) {
				block = entry.getKey();
				break;
			}
		}

		if (block != null && state.hasProperty(BlockStateProperties.AXIS)) {
			BlockState newState = block.defaultBlockState().setValue(BlockStateProperties.AXIS, state.getValue(BlockStateProperties.AXIS));

			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, newState);
				level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);

				if (level instanceof ServerLevel serverLevel) {
					serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.0);
				}

				if (!useOnContext.getPlayer().getAbilities().instabuild) {
					useOnContext.getItemInHand().shrink(1);
				}
			}

			info.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
		}
	}
}
