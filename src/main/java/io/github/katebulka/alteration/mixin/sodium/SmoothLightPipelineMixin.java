package io.github.katebulka.alteration.mixin.sodium;

import io.github.katebulka.alteration.ModConfig;
import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.caffeinemc.mods.sodium.client.model.light.data.QuadLightData;
import net.caffeinemc.mods.sodium.client.model.light.flat.FlatLightPipeline;
import net.caffeinemc.mods.sodium.client.model.light.smooth.SmoothLightPipeline;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = SmoothLightPipeline.class,
		remap = false,
		priority = 1500
)
public abstract class SmoothLightPipelineMixin {
	@Unique private FlatLightPipeline alteration$flatPipeline;
	@Unique private final QuadLightData alteration$flatLightData = new QuadLightData();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void alteration$init(LightDataAccess cache, CallbackInfo ci) {
		this.alteration$flatPipeline = new FlatLightPipeline(cache);
	}

	@Inject(method = "calculate", at = @At("TAIL"))
	private void alteration$calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction cullFace, Direction lightFace, boolean shade, boolean enhanced, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.mixedLights) {
			return;
		}

		this.alteration$flatPipeline.calculate(quad, pos, alteration$flatLightData, cullFace, lightFace, shade, enhanced);

		final float smoothWeight = 1.0f - ModConfig.INSTANCE.flatStrength;
		final float flatWeight = ModConfig.INSTANCE.flatStrength;

		for (int i = 0; i < 4; i++) {
			out.br[i] = out.br[i] * smoothWeight + alteration$flatLightData.br[i] * flatWeight;
		}

		for (int i = 0; i < 4; i++) {
			int smoothLm = out.lm[i];
			int flatLm = alteration$flatLightData.lm[i];

			int smoothBlock = smoothLm & 0xFFFF;
			int smoothSky = (smoothLm >> 16) & 0xFFFF;
			int flatBlock = flatLm & 0xFFFF;
			int flatSky = (flatLm >> 16) & 0xFFFF;

			int blendedBlock = Math.round(smoothBlock * smoothWeight + flatBlock * flatWeight);
			int blendedSky = Math.round(smoothSky * smoothWeight + flatSky * flatWeight);

			out.lm[i] = (blendedSky << 16) | (blendedBlock & 0xFFFF);
		}
	}
}
