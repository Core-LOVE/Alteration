package io.github.katebulka.alteration.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.ModHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
		value = Entity.class
)
public abstract class EntityMixin {
	@ModifyReturnValue(method = "getDefaultGravity", at = @At("RETURN"))
	private double alteration$getDefaultGravity(double d0) {
		Entity self = (Entity) (Object) this;
		return ModHelper.getGravity(self, d0);
	}
}
