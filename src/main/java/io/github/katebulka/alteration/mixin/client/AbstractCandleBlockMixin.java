package io.github.katebulka.alteration.mixin.client;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.katebulka.alteration.client.loader.CandleFlameLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(
		value = AbstractCandleBlock.class
)
public abstract class AbstractCandleBlockMixin extends Block {
	public AbstractCandleBlockMixin(Properties p_49795_) {
		super(p_49795_);
	}

	@Inject(method = "addParticlesAndSound", at = @At("HEAD"), cancellable = true)
	private static void alteration$addParticlesAndSound(Level level, Vec3 vec, RandomSource rand, CallbackInfo cl) {
		BlockPos belowPos = BlockPos.containing(vec).below();
		BlockState block = level.getBlockState(belowPos);

		ResourceLocation flame = CandleFlameLoader.getFlameBlock(block);

		if (flame == null) return;

		ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(flame);

		if (type == null) return;

		float f = rand.nextFloat();

		if (f < 0.3F) {
			level.addParticle(ParticleTypes.SMOKE, vec.x, vec.y, vec.z, (double)0.0F, (double)0.0F, (double)0.0F);

			if (f < 0.17F) {
				level.playLocalSound(vec.x + (double)0.5F, vec.y + (double)0.5F, vec.z + (double)0.5F, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
			}
		}

		level.addParticle((SimpleParticleType) type, vec.x, vec.y, vec.z, (double)0.0F, (double)0.0F, (double)0.0F);

		cl.cancel();
	}
}
