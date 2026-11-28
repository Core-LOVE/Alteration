package io.github.katebulka.alteration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.katebulka.alteration.ModHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
		value = ClientLevel.class
)
public abstract class ClientLevelMixin {
//	@ModifyReturnValue(method = "getStarBrightness", at = @At("RETURN"))
//	private float alteration$getStarBrightness(float original, float partialTick) {
//		ClientLevel self = (ClientLevel) (Object) this;
//		Minecraft mc = Minecraft.getInstance();
//
//		if (mc.gameRenderer == null || mc.gameRenderer.getMainCamera() == null) {
//			return original;
//		}
//
//		double y = mc.gameRenderer.getMainCamera().getPosition().y;
//		float t = ModHelper.getNightSkyFactor(self, y);
//
//		if (t <= 0.0F) {
//			return original;
//		}
//
//		float stars = 0.5F;
//
//		return Math.max(original, Mth.lerp(t, original, stars));
//	}
}
