package io.github.katebulka.alteration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.katebulka.alteration.ModHelper;
import net.minecraft.client.particle.Particle;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = Particle.class
)
public class ParticleMixin {
	@Shadow
	protected float gravity;

	@ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/particle/Particle;gravity:F", opcode = Opcodes.GETFIELD))
	private float alteration$gravity(float original) {
		Particle self = (Particle) (Object) this;
		return ModHelper.getParticleGravity(self, original);
	}
}
