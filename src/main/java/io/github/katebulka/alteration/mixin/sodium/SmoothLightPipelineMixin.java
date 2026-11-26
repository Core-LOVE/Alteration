package io.github.katebulka.alteration.mixin.sodium;

import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.ModHelper;
import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.caffeinemc.mods.sodium.client.model.light.data.QuadLightData;
import net.caffeinemc.mods.sodium.client.model.light.flat.FlatLightPipeline;
import net.caffeinemc.mods.sodium.client.model.light.smooth.SmoothLightPipeline;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.BooleanSupplier;

@Mixin(
		value = SmoothLightPipeline.class,
		remap = false,
		priority = 1500
)
public abstract class SmoothLightPipelineMixin implements IMixinConfigPlugin {
	@Unique private FlatLightPipeline alteration$flatPipeline;
	@Unique private final QuadLightData alteration$flatLightData = new QuadLightData();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void alteration$init(LightDataAccess cache, CallbackInfo ci) {
		this.alteration$flatPipeline = new FlatLightPipeline(cache);
	}

	@Inject(method = "calculate", at = @At("TAIL"))
	private void alteration$calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction cullFace, Direction lightFace, boolean shade, boolean enhanced, CallbackInfo ci) {
		final float flatWeight = Mth.clamp(ModConfig.INSTANCE.flatStrength, 0f, 1f);

		if (!ModConfig.INSTANCE.mixedLights || flatWeight <= 0f) {
			return;
		}

		if (flatWeight >= 1f) {
			this.alteration$flatPipeline.calculate(quad, pos, out, cullFace, lightFace, shade, enhanced);
			return;
		}

		this.alteration$flatPipeline.calculate(quad, pos, this.alteration$flatLightData, cullFace, lightFace, shade, enhanced);

		final float smoothWeight = (1f - flatWeight);

		for (int i = 0; i < 4; i++) {
			out.br[i] = out.br[i] * smoothWeight + this.alteration$flatLightData.br[i] * flatWeight;

			final int smoothLm = out.lm[i];
			final int flatLm = this.alteration$flatLightData.lm[i];

			final int smoothBlock = smoothLm & 0xFFFF;
			final int smoothSky = (smoothLm >>> 16) & 0xFFFF;
			final int flatBlock = flatLm & 0xFFFF;
			final int flatSky = (flatLm >>> 16) & 0xFFFF;

			final int blendedBlock = Math.round(smoothBlock * smoothWeight + flatBlock * flatWeight);
			final int blendedSky = Math.round(smoothSky * smoothWeight + flatSky * flatWeight);

			out.lm[i] = ((blendedSky & 0xFFFF) << 16) | (blendedBlock & 0xFFFF);
		}
	}
}
