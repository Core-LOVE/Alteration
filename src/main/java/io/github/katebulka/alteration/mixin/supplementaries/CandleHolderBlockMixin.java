package io.github.katebulka.alteration.mixin.supplementaries;

import io.github.katebulka.alteration.client.loader.CandleFlameLoader;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
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

import java.util.function.Supplier;

@Mixin(
		value = CandleHolderBlock.class,
		remap = false
)
public abstract class CandleHolderMixin {
	@Shadow
	@Final
	public Supplier<ParticleType<? extends ParticleOptions>> particle;

	@Shadow
	@Final
	public static EnumProperty<AttachFace> FACE;

	@Shadow
	@Final
	public static DirectionProperty FACING;

	@Unique
	@Nullable
	private static ParticleType<?> getFlame(BlockState block) {
		ResourceLocation flame = CandleFlameLoader.getFlameBlock(block);

		if (flame == null) return null;

		ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(flame);

		return type;
	}

	@Inject(method = "addParticlesAndSound", at = @At("HEAD"), cancellable = true)
	private void alteration$addParticlesAndSound(Level level, Vec3 offset, RandomSource random, CallbackInfo cl) {
		BlockPos pos = BlockPos.containing(offset);
		BlockState state = level.getBlockState(pos);

		byte attach = 0;

		switch ((AttachFace) state.getValue(FACE)) {
			case CEILING:
				attach = 2; break;
			case FLOOR:
				attach = 1; break;
			case WALL:
				switch ((Direction)state.getValue(FACING)) {
					case SOUTH:
						attach = 3; break;
					case WEST:
						attach = 4; break;
					case EAST:
						attach = 5; break;
					default:
						attach = 6; break;
				}
			default:
				throw new MatchException((String)null, (Throwable)null);
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
			BlockPos belowPos = pos.north();
			BlockState block = level.getBlockState(belowPos);
			type = getFlame(block);
		} else if (attach == 5) { // WEST
			BlockPos belowPos = pos.west();
			BlockState block = level.getBlockState(belowPos);
			type = getFlame(block);
		} else if (attach == 6) { // EAST
			BlockPos belowPos = pos.east();
			BlockState block = level.getBlockState(belowPos);
			type = getFlame(block);
		}

		if (type == null) return;

		float f = random.nextFloat();

		if (f < 0.3F) {
			level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, (double)0.0F, (double)0.0F, (double)0.0F);
			if (f < 0.17F) {
				level.playLocalSound(offset.x + (double)0.5F, offset.y + (double)0.5F, offset.z + (double)0.5F, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		}

		level.addParticle((ParticleOptions) type, offset.x, offset.y, offset.z, (double)0.0F, (double)0.0F, (double)0.0F);
		cl.cancel();
	}
}
