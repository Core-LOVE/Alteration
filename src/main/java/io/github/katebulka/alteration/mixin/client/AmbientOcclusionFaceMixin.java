package io.github.katebulka.alteration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.katebulka.alteration.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.renderer.block.ModelBlockRenderer$AmbientOcclusionFace")
public abstract class AmbientOcclusionFaceMixin {
	@ModifyExpressionValue(method = "calculate", at = @At(value = "INVOKE", target = "Ljava/util/BitSet;get(I)Z", ordinal = 3))
	private boolean alteration$calculate(boolean original) {
		return ModConfig.INSTANCE.darkerAmbientOcclusion ? false : original;
	}
}
