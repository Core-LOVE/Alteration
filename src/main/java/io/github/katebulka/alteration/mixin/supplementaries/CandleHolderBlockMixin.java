package io.github.katebulka.alteration.mixin.supplementaries;

import io.github.katebulka.alteration.client.loader.CandleFlameLoader;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.LightUpWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(
		value = CandleHolderBlock.class,
		remap = false
)
public abstract class CandleHolderBlockMixin extends LightUpWaterBlock {
	@Shadow
	@Final
	public Supplier<ParticleType<? extends ParticleOptions>> particle;

	@Shadow
	@Final
	public Function<BlockState, List<Vec3>> particleOffsets;

	@Shadow
	@Final
	public static EnumProperty<AttachFace> FACE;

	@Shadow
	@Final
	public static DirectionProperty FACING;

	protected CandleHolderBlockMixin(Properties properties) {
		super(properties);
	}

	@Unique
	@Nullable
	private static ParticleType<?> getFlame(BlockState block) {
		ResourceLocation flame = CandleFlameLoader.getFlameBlock(block);

		if (flame == null) return null;

		ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(flame);

		return type;
	}

	@Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
	private void alteration$animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand, CallbackInfo ci) {
		if (!(state.getBlock() instanceof CandleHolderBlock)) {
			return;
		}

		AttachFace face = state.getValue(FACE);

		if (face == null) {
			return;
		}

		byte attach = 0;

		switch (face) {
			case CEILING -> attach = 2;
			case FLOOR -> attach = 1;
			case WALL -> {
				Direction facing = state.getValue(FACING).getOpposite();

				if (facing == null) return;

				attach = switch (facing) {
					case SOUTH -> 3;
					case WEST -> 5;
					case EAST -> 6;
					default -> 4;
				};
			}
			default -> {
				return;
			}
		}

		if (attach == 0) return;

		ParticleType<?> type = null;

		if (attach == 1) { // FLOOR
			BlockPos belowPos = pos.below();
			BlockState block = level.getBlockState(belowPos);
			type = getFlame(block);
		} else if (attach == 2) { // TOP
			BlockPos belowPos = pos.above();
			BlockState block = level.getBlockState(belowPos);
			type = getFlame(block);
		} else if (attach == 3) { // SOUTH
			BlockPos belowPos = pos.south();
			BlockState block = level.getBlockState(belowPos);
			type = getFlame(block);
		} else if (attach == 4) { // NORTH
			BlockPos p = pos.north();
			BlockState block = level.getBlockState(p);
			type = getFlame(block);
		} else if (attach == 5) { // WEST
			BlockPos p = pos.west();
			BlockState block = level.getBlockState(p);
			type = getFlame(block);
		} else if (attach == 6) { // EAST
			BlockPos p = pos.east();
			BlockState block = level.getBlockState(p);
			type = getFlame(block);
		}

		if (type == null) return;

		List<Vec3> offsets = this.particleOffsets.apply(state);

		for (Vec3 v : offsets) {
			Vec3 world = v.add(pos.getX(), pos.getY(), pos.getZ());
			float f = rand.nextFloat();

			if (f < 0.3F) {
				level.addParticle(ParticleTypes.SMOKE, world.x, world.y, world.z, 0, 0, 0);

				if (f < 0.17F) {
					level.playLocalSound(world.x + 0.5, world.y + 0.5, world.z + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
				}
			}

			level.addParticle((ParticleOptions) type, world.x, world.y, world.z, 0, 0, 0);
		}

		ci.cancel();
	}
}
