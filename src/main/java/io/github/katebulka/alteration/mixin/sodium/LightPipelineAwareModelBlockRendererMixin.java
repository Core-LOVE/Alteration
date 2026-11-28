package io.github.katebulka.alteration.mixin.sodium;

import net.neoforged.neoforge.client.model.lighting.LightPipelineAwareModelBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(
		value = LightPipelineAwareModelBlockRenderer.class
)
public abstract class LightPipelineAwareModelBlockRendererMixin {
}
